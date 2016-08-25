package by.training.online_pharmacy.dao.impl.database;




import by.training.online_pharmacy.dao.OrderDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.exception.EntityNotFoundException;
import by.training.online_pharmacy.dao.exception.OutOfRangeException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.dao.impl.database.util.DrugCountUpdater;
import by.training.online_pharmacy.dao.impl.database.util.PrescriptionCountUpdater;
import by.training.online_pharmacy.dao.impl.database.util.exception.ParameterNotFoundException;
import by.training.online_pharmacy.domain.Period;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.domain.order.OrderStatus;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import org.apache.poi.hssf.record.formula.functions.T;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public class DatabaseOrderDAO implements OrderDAO {
    private static final String INSERT_ORDER_QUERY = "INSERT INTO orders (or_client_login, or_login_via, or_drug_id, or_drug_count, or_drug_dosage, or_status, or_sum, or_date) VALUES(?, ?, ?, ?, ?, ?, (select dr_price*? from drugs where dr_id=?),curdate());";
    private static final String CANCEL_ORDER_QUERY = "update orders set or_status='canceled' where or_client_login=? and or_login_via=? and or_status='ordered' and or_id=?;";
    private static final String GET_ORDERS_QUERY_PREFIX = "select or_id, or_drug_id, or_drug_count, or_drug_dosage, or_status, or_sum, or_date, dr_name from orders inner join drugs on or_drug_id=dr_id where or_client_login=? and or_login_via=?";
    private static final String DATE_FROM = " and or_date>=? ";
    private static final String DATE_TO = " and or_date<=? ";
    private static final String ORDER_STATUS = " and or_status=? ";
    private static final String DRUG_NAME = "and dr_name like ? ";
    private static final String GET_ORDERS_POSTFIX = " order by or_date desc limit ?,?;";
    private static final String FK_OR_DRUGS = "fk_or_drugs";
    private static final String FK_OR_USERS = "fk_or_users";

    @Override
    public List<Order> searchOrders(User user, String dateFrom, String dateTo, String orderStatus, String drugName, int limit, int startFrom) throws DaoException {
        StringBuilder query = new StringBuilder(GET_ORDERS_QUERY_PREFIX);
        if(dateFrom!=null&&!dateFrom.isEmpty()){
            query.append(DATE_FROM);
        }
        if(dateTo!=null&&!dateTo.isEmpty()){
            query.append(DATE_TO);
        }
        if(orderStatus!=null&&!orderStatus.isEmpty()){
            query.append(ORDER_STATUS);
        }
        if(drugName!=null&&!drugName.isEmpty()){
            query.append(DRUG_NAME);
        }
        query.append(GET_ORDERS_POSTFIX);

        try (DatabaseOperation databaseOperation = new DatabaseOperation(query.toString())){
            int paramNumber = 3;
            databaseOperation.setParameter(1, user.getLogin());
            databaseOperation.setParameter(2, user.getRegistrationType().toString().toLowerCase());
            if(dateFrom!=null&&!dateFrom.isEmpty()){
                databaseOperation.setParameter(paramNumber++, new Date(dateFrom));
            }
            if(dateTo!=null&&!dateTo.isEmpty()){
                databaseOperation.setParameter(paramNumber++, new Date(dateTo));
            }
            if(orderStatus!=null&&!orderStatus.isEmpty()){
                databaseOperation.setParameter(paramNumber++, orderStatus.toLowerCase());
            }
            if(drugName!=null&&!drugName.isEmpty()){
                databaseOperation.setParameter(paramNumber++, Param.PER_CENT+drugName+Param.PER_CENT);
            }
            databaseOperation.setParameter(paramNumber++, startFrom);
            databaseOperation.setParameter(paramNumber, limit);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            List<Order> orders = resultSetToOrder(resultSet);
            return orders;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not search orders in database by query="+query, e);
        }

    }

    @Override
    public Order getOrderById(int orderId) throws DaoException {
        return null;
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus orderStatus, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public List<Order> getOrdersByDrugId(int drugId, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public List<Order> getOrdersByDate(Date date, Period period, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public void updateOrder(Order order) throws DaoException {

    }

    @Override
    public void cancelOrder(User user, int orderId) throws DaoException {
        try {
            DatabaseOperation databaseOperation = new DatabaseOperation(CANCEL_ORDER_QUERY);
            databaseOperation.beginTransaction();
            databaseOperation.setParameter(TableColumn.ORDER_US_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.ORDER_LOGIN_VIA, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.ORDER_ID, orderId);
            if(databaseOperation.invokeWriteOperation()==0){
                databaseOperation.rollBack();
                throw new EntityNotFoundException("Order with id="+orderId+" was not found");
            }
        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not cancel order with id="+orderId, e);
        }
    }

    @Override
    public void insertOrder(Order order) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_ORDER_QUERY, true)){
            databaseOperation.beginTransaction();
            databaseOperation.setParameter(TableColumn.ORDER_US_LOGIN, order.getClient().getLogin());
            databaseOperation.setParameter(TableColumn.ORDER_LOGIN_VIA, order.getClient().getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.ORDER_DRUG_ID, order.getDrug().getId());
            databaseOperation.setParameter(TableColumn.ORDER_DRUG_COUNT, order.getDrugCount());
            databaseOperation.setParameter(TableColumn.ORDER_DRUG_DOSAGE, order.getDrugDosage());
            databaseOperation.setParameter(TableColumn.ORDER_STATUS, order.getOrderStatus().toString().toLowerCase());
            databaseOperation.setParameter(7, order.getDrugCount());
            databaseOperation.setParameter(8, order.getDrug().getId());
            databaseOperation.invokeWriteOperation();
            DrugCountUpdater drugCountUpdater = new DrugCountUpdater(databaseOperation);
            drugCountUpdater.changeDrugCount(-order.getDrugCount(), order.getDrug().getId());
            if(order.getDrug().isPrescriptionEnable()){
                PrescriptionCountUpdater prescriptionCountUpdater = new PrescriptionCountUpdater(databaseOperation);
                int rowsUpdated = prescriptionCountUpdater.changeDrugCount(order.getDrugCount(), order.getClient().getLogin(),
                        order.getClient().getRegistrationType(), order.getDrug().getId(), order.getDrugDosage());
                if(rowsUpdated==0){
                    databaseOperation.rollBack();
                    throw new EntityNotFoundException("Can not found prescription for drug with id="+order.getDrug().getId());
                }
            }
            databaseOperation.endTransaction();
        } catch (ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not insert new order "+ order +" to database", e);
        } catch (SQLException ex) {
            if(ex.getErrorCode()==ErrorCode.ERROR_CODE_1452&&ex.getMessage().contains(FK_OR_DRUGS)){
                throw new EntityDeletedException("Can not insert new order "+ order +" to database", false, ex);
            }else if(ex.getErrorCode()==ErrorCode.ERROR_CODE_1690){
                throw new OutOfRangeException("Can not insert new order "+ order +" to database", ex);
            } else if(ex.getErrorCode()==ErrorCode.ERROR_CODE_1452&&ex.getMessage().contains(FK_OR_USERS)){
                throw new EntityDeletedException("Can not insert new order "+ order +" to database", true, ex);
            }
            else {
                throw new DaoException("Can not insert new order "+ order +" to database", ex);
            }
        }
    }

    @Override
    public void deleteOrder(int orderId) throws DaoException {

    }


   /* @Override
    public List<Order> getUserOrders(String userLogin, int limit, int startFrom) throws DaoException {
        List<Order> orders = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_USER_ORDERS_QUERY, userLogin, limit, startFrom)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            orders = resultSetToOrder(resultSet);
            return orders;
        } catch (Exception e) {
            throw new DaoException("Cannot load orders with or_client_login = \'"+userLogin+"\' from database", e);
        }

    }

    @Override
    public Order getOrderById(int orderId) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(GET_ORDER_BY_ID_QUERY, orderId)) {
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            List<Order> result = resultSetToOrder(resultSet);
            if(!result.isEmpty()){
                return result.get(0);
            }
        } catch (Exception e) {
            throw new DaoException("Can not read order with id = \'"+orderId+"\' from database", e);
        }
        return null;
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus orderStatus, int limit, int startFrom) throws DaoException {
        List<Order> orders = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_ORDERS_BY_STATUS_QUERY, orderStatus.toString().toLowerCase(), limit, startFrom)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            orders = resultSetToOrder(resultSet);
            return orders;
        } catch (Exception e) {
            throw new DaoException("Can not load orders with status = \'"+orderStatus+"\' from database", e);
        }
    }

    @Override
    public List<Order> getOrdersByDrugId(int drugId, int limit, int startFrom) throws DaoException {
        List<Order> orders = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_ORDERS_BY_DRUG_ID_QUERY, drugId, limit, startFrom)){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            orders = resultSetToOrder(resultSet);
            return orders;
        } catch (Exception e) {
            throw new DaoException("Can not load orders with drugId = \'"+drugId+"\'", e);
        }
    }

    @Override
    public List<Order> getOrdersByDate(Date date, Period period, int limit, int startFrom) throws DaoException {
        List<Order> orders = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation()){
            switch (period) {
                case AFTER_DATE: {
                    databaseOperation.init(GET_ORDERS_BY_DATE_AFTER_QUERY, date, limit, startFrom);
                    break;
                }
                case BEFORE_DATE: {
                    databaseOperation.init(GET_ORDERS_BY_DATE_BEFORE_QUERY, date, limit, startFrom);
                    break;
                }
                case CURRENT_DATE: {
                    databaseOperation.init(GET_ORDERS_BY_DATE_CURRENT_QUERY, date, limit, startFrom);
                    break;
                }
            }
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            orders = resultSetToOrder(resultSet);
        }catch (Exception e){
            throw new DaoException("Can not load orders from database", e);
        }

        return orders;
    }

    @Override
    public void updateOrder(Order order) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_ORDER_QUERY, order.getDrug().getId(), order.getDrugCount(), order.getDrugDosage(),
                order.getOrderStatus().toString().toLowerCase(), order.getId())){
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Can not update order "+order, e);
        }
    }



    @Override
    public void insertOrder(Order order) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_ORDER_QUERY, order.getId(), order.getClient().getLogin(), order.getDrug().getId(),
                order.getDrugCount(), order.getDrugDosage(), order.getOrderStatus().toString().toLowerCase(), order.getOrderDate())){
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Can not insert new order "+ order +" to database", e);
        }
    }

    @Override
    public void deleteOrder(int orderId) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(DELETE_ORDER_QUERY, orderId)){
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Can not delete order with id = \'"+orderId+"\'", e);
        }
    }
*///"select or_id, or_drug_id, or_drug_count, or_drug_dosage, or_status, or_sum, or_date, dr_name, dr_image from orders inner join on or_drug_id=dr_id where us_login=? and login_via=? order by or_date desc limit ?, ?;"

    private List<Order> resultSetToOrder(ResultSet resultSet) throws SQLException {
        List<Order> result = new ArrayList<>();
        while (resultSet.next()) {
            Order order = new Order();
            Drug drug = new Drug();
            order.setDrug(drug);
            order.setId(resultSet.getInt(TableColumn.ORDER_ID));
            order.setDrugCount(resultSet.getInt(TableColumn.ORDER_DRUG_COUNT));
            order.setOrderStatus(OrderStatus.valueOf(resultSet.getString(TableColumn.ORDER_STATUS).toUpperCase()));
            order.setTotalSum(resultSet.getDouble(TableColumn.ORDER_SUM));
            order.setOrderDate(resultSet.getDate(TableColumn.ORDER_DATE));
            drug.setId(resultSet.getInt(TableColumn.ORDER_DRUG_ID));
            drug.setName(resultSet.getString(TableColumn.DRUG_NAME));
            order.setDrugDosage(resultSet.getShort(TableColumn.ORDER_DRUG_DOSAGE));
            result.add(order);
        }
        return result;
    }
}
