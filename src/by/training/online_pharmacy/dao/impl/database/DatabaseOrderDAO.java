package by.training.online_pharmacy.dao.impl.database;




import by.training.online_pharmacy.dao.OrderDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityNotFoundException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.dao.impl.database.util.exception.ParameterNotFoundException;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.domain.order.OrderStatus;
import by.training.online_pharmacy.domain.order.SearchOrderCriteria;
import by.training.online_pharmacy.domain.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public class DatabaseOrderDAO implements OrderDAO {
    private static final String INSERT_ORDER_QUERY = "INSERT INTO orders (or_client_login, or_login_via, or_drug_id, or_drug_count, or_drug_dosage, or_status, or_sum, or_date) VALUES(?, ?, ?, ?, ?, ?, (select dr_price*? from drugs where dr_id=?),curdate());";
    private static final String CHANGE_ORDER_STATUS = "update orders set or_status=? where or_client_login=? and or_login_via=? and or_id=? and or_status!='paid' and or_status!='completed';";
    private static final String GET_ORDERS_QUERY_PREFIX = "select us_first_name, us_second_name, or_id, or_drug_id, or_drug_count, or_drug_dosage, or_status, or_sum, or_date, dr_name from orders inner join drugs on or_drug_id=dr_id inner join users on us_login=or_client_login and login_via=or_login_via where or_client_login=? and or_login_via=?";
    private static final String DATE_FROM = " and or_date>=? ";
    private static final String DATE_TO = " and or_date<=? ";
    private static final String ORDER_STATUS = " and or_status=? ";
    private static final String DRUG_NAME = "and dr_name like ? ";
    private static final String GET_ORDERS_POSTFIX = " order by or_date desc limit ?,?;";
    private static final String GET_ORDER_SUM_QUERY = "select or_sum from orders where or_id=? and or_client_login=? and or_login_via=?;";
    private static final String GET_ALL_ORDERS_QUERY_PREFIX = "select us_first_name, us_second_name, or_id, or_drug_id, or_drug_count, or_drug_dosage, or_status, or_sum, or_date, dr_name from orders inner join drugs on or_drug_id=dr_id inner join users on us_login=or_client_login and login_via=or_login_via where or_id>=0";
    private static final String COMPLETE_ORDER_QUERY = "update orders set or_status='completed' where or_id=? and or_status='paid';";
    private static final String GET_ORDER_BY_ID_COMMAND = "select us_first_name, us_second_name, or_id, or_drug_id, or_drug_count, or_drug_dosage, or_status, or_sum, or_date, dr_name from orders inner join drugs on or_drug_id=dr_id inner join users on us_login=or_client_login and login_via=or_login_via where or_id=?;";

    @Override
    public void completeOrder(int orderId) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(COMPLETE_ORDER_QUERY)){
            databaseOperation.setParameter(1, orderId);

            if(databaseOperation.invokeWriteOperation()==0){
                throw new EntityNotFoundException("Order with id="+orderId+" was not found");
            }

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not compete order with id="+orderId, e);
        }
    }

    @Override
    public List<Order> getOrderById(int orderId) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_ORDER_BY_ID_COMMAND)){
            databaseOperation.setParameter(1, orderId);

            ResultSet resultSet = databaseOperation.invokeReadOperation();

            return resultSetToOrder(resultSet);

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not search order by id", e);
        }
    }

    @Override
    public float getOrderSum(User user, int orderId) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_ORDER_SUM_QUERY)){

            databaseOperation.setParameter(1, orderId);
            databaseOperation.setParameter(2, user.getLogin());
            databaseOperation.setParameter(3, user.getRegistrationType().toString().toLowerCase());
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            if(resultSet.next()){
                return resultSet.getFloat(TableColumn.ORDER_SUM);
            }
            else {
                throw new EntityNotFoundException("Order with id="+orderId+" was not found");
            }
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Can not load  order sum from database");
        }

    }

    @Override
    public List<Order> searchAllOrders(SearchOrderCriteria searchOrderCriteria, int startFrom, int limit) throws DaoException {
        StringBuilder query = new StringBuilder(GET_ALL_ORDERS_QUERY_PREFIX);

        if(searchOrderCriteria.getDateFrom()!=null&&!searchOrderCriteria.getDateFrom().isEmpty()){
            query.append(DATE_FROM);
        }

        if(searchOrderCriteria.getDateTo()!=null&&!searchOrderCriteria.getDateTo().isEmpty()){
            query.append(DATE_TO);
        }

        if(searchOrderCriteria.getOrderStatus()!=null&&!searchOrderCriteria.getOrderStatus().isEmpty()){
            query.append(ORDER_STATUS);
        }

        if(searchOrderCriteria.getDrugName()!=null&&!searchOrderCriteria.getDrugName().isEmpty()){
            query.append(DRUG_NAME);
        }

        query.append(GET_ORDERS_POSTFIX);

        try (DatabaseOperation databaseOperation = new DatabaseOperation(query.toString())){
            int paramNumber = 1;

            if(searchOrderCriteria.getDateFrom()!=null&&!searchOrderCriteria.getDateFrom().isEmpty()){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Param.DATE_PATTERN);

                Date date = simpleDateFormat.parse(searchOrderCriteria.getDateFrom());

                databaseOperation.setParameter(paramNumber++, date);
            }

            if(searchOrderCriteria.getDateTo()!=null&&!searchOrderCriteria.getDateTo().isEmpty()){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Param.DATE_PATTERN);

                Date date = simpleDateFormat.parse(searchOrderCriteria.getDateFrom());

                databaseOperation.setParameter(paramNumber++, date);
            }

            if(searchOrderCriteria.getOrderStatus()!=null&&!searchOrderCriteria.getOrderStatus().isEmpty()){
                databaseOperation.setParameter(paramNumber++, searchOrderCriteria.getOrderStatus().toLowerCase());
            }

            if(searchOrderCriteria.getDrugName()!=null&&!searchOrderCriteria.getDrugName().isEmpty()){
                databaseOperation.setParameter(paramNumber++, Param.PER_CENT+searchOrderCriteria.getDrugName()+Param.PER_CENT);
            }

            databaseOperation.setParameter(paramNumber++, startFrom);
            databaseOperation.setParameter(paramNumber, limit);

            ResultSet resultSet = databaseOperation.invokeReadOperation();

            return resultSetToOrder(resultSet);
        } catch (SQLException | ConnectionPoolException | ParseException e) {
            throw new DaoException("Can not search orders in database by query="+query, e);
        }
    }

    @Override
    public List<Order> searchUsersOrders(User user, SearchOrderCriteria searchOrderCriteria, int startFrom, int limit) throws DaoException {
        StringBuilder query = new StringBuilder(GET_ORDERS_QUERY_PREFIX);

        if(searchOrderCriteria.getDateFrom()!=null&&!searchOrderCriteria.getDateFrom().isEmpty()){
            query.append(DATE_FROM);
        }

        if(searchOrderCriteria.getDateTo()!=null&&!searchOrderCriteria.getDateTo().isEmpty()){
            query.append(DATE_TO);
        }

        if(searchOrderCriteria.getOrderStatus()!=null&&!searchOrderCriteria.getOrderStatus().isEmpty()){
            query.append(ORDER_STATUS);
        }

        if(searchOrderCriteria.getDrugName()!=null&&!searchOrderCriteria.getDrugName().isEmpty()){
            query.append(DRUG_NAME);
        }

        query.append(GET_ORDERS_POSTFIX);

        try (DatabaseOperation databaseOperation = new DatabaseOperation(query.toString())){
            int paramNumber = 3;

            databaseOperation.setParameter(1, user.getLogin());
            databaseOperation.setParameter(2, user.getRegistrationType().toString().toLowerCase());

            if(searchOrderCriteria.getDateFrom()!=null&&!searchOrderCriteria.getDateFrom().isEmpty()){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Param.DATE_PATTERN);

                Date date = simpleDateFormat.parse(searchOrderCriteria.getDateFrom());

                databaseOperation.setParameter(paramNumber++, date);
            }

            if(searchOrderCriteria.getDateTo()!=null&&!searchOrderCriteria.getDateTo().isEmpty()){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Param.DATE_PATTERN);

                Date date = simpleDateFormat.parse(searchOrderCriteria.getDateFrom());

                databaseOperation.setParameter(paramNumber++, date);
            }

            if(searchOrderCriteria.getOrderStatus()!=null&&!searchOrderCriteria.getOrderStatus().isEmpty()){
                databaseOperation.setParameter(paramNumber++, searchOrderCriteria.getOrderStatus().toLowerCase());
            }

            if(searchOrderCriteria.getDrugName()!=null&&!searchOrderCriteria.getDrugName().isEmpty()){
                databaseOperation.setParameter(paramNumber++, Param.PER_CENT+searchOrderCriteria.getDrugName()+Param.PER_CENT);
            }

            databaseOperation.setParameter(paramNumber++, startFrom);
            databaseOperation.setParameter(paramNumber, limit);

            ResultSet resultSet = databaseOperation.invokeReadOperation();

            List<Order> orders = resultSetToOrder(resultSet);

            return orders;
        } catch (SQLException | ConnectionPoolException|ParseException e) {
            throw new DaoException("Can not search orders in database by query="+query, e);
        }

    }

    @Override
    public void setOrderStatus(User user, OrderStatus orderStatus, int orderId) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(CHANGE_ORDER_STATUS);){
            databaseOperation.beginTransaction();

            databaseOperation.setParameter(TableColumn.ORDER_US_LOGIN, user.getLogin());
            databaseOperation.setParameter(TableColumn.ORDER_LOGIN_VIA, user.getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(TableColumn.ORDER_ID, orderId);
            databaseOperation.setParameter(TableColumn.ORDER_STATUS, orderStatus.toString().toLowerCase());

            if(databaseOperation.invokeWriteOperation()==0){
                throw new EntityNotFoundException("Order with id="+orderId+" was not found");
            }

        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not cancel order with id="+orderId, e);
        }
    }

    @Override
    public void insertOrder(Order order) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_ORDER_QUERY)){

            databaseOperation.setParameter(1, order.getClient().getLogin());
            databaseOperation.setParameter(2, order.getClient().getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(3, order.getDrug().getId());
            databaseOperation.setParameter(4, order.getDrugCount());
            databaseOperation.setParameter(5, order.getDrugDosage());
            databaseOperation.setParameter(6, order.getOrderStatus().toString().toLowerCase());
            databaseOperation.setParameter(7, order.getDrugCount());
            databaseOperation.setParameter(8, order.getDrug().getId());
            databaseOperation.invokeWriteOperation();

            //databaseOperation.endTransaction();

        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Can not insert new order "+ order +" to database", e);
        }
    }

    private List<Order> resultSetToOrder(ResultSet resultSet) throws SQLException {
        List<Order> result = new ArrayList<>();

        while (resultSet.next()) {
            Order order = new Order();
            Drug drug = new Drug();
            User client = new User();

            order.setDrug(drug);
            order.setId(resultSet.getInt(TableColumn.ORDER_ID));
            order.setDrugCount(resultSet.getInt(TableColumn.ORDER_DRUG_COUNT));
            order.setOrderStatus(OrderStatus.valueOf(resultSet.getString(TableColumn.ORDER_STATUS).toUpperCase()));
            order.setTotalSum(resultSet.getDouble(TableColumn.ORDER_SUM));
            order.setOrderDate(resultSet.getDate(TableColumn.ORDER_DATE));

            drug.setId(resultSet.getInt(TableColumn.ORDER_DRUG_ID));
            drug.setName(resultSet.getString(TableColumn.DRUG_NAME));
            order.setDrugDosage(resultSet.getShort(TableColumn.ORDER_DRUG_DOSAGE));

            client.setFirstName(resultSet.getString(TableColumn.USER_FIRST_NAME));
            client.setSecondName(resultSet.getString(TableColumn.USER_SECOND_NAME));

            order.setClient(client);

            result.add(order);
        }
        return result;
    }
}
