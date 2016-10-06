package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.DrugDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.exception.EntityNotFoundException;
import by.training.online_pharmacy.dao.exception.MultipleRecordsException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.dao.impl.database.util.ListConverter;
import by.training.online_pharmacy.dao.impl.database.util.exception.ParameterNotFoundException;
import by.training.online_pharmacy.domain.drug.*;
import by.training.online_pharmacy.domain.user.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladislav on 15.06.16.
 */
public class DatabaseDrugDAO implements DrugDAO {

    private static final String GET_DRUGS_BY_ID_QUERY = "SELECT dr_id, dr_class, dr_description, dr_image, dr_in_stock, dr_name, dr_prescription_enable, dr_price, dr_type,  dr_dosage, dr_active_substance, dm_name, dm_country, dm_description, dr_class_name, dr_class_description FROM drugs inner join drugs_manufactures on dr_man_name = dm_name and dr_man_country=dm_country inner join drug_classes on dr_class = dr_class_name WHERE dr_id=?;";
    private static final String SEARCH_DRUGS_QUERY = "select dr_price, dr_id, dr_image, dr_description, dr_name, dr_active_substance, dr_class from drugs where dr_name like ? or dr_description like ?  or dr_active_substance like ? order by dr_name limit ?, ?;";
    private static final String EXTENDED_DRUGS_SEARCH_QUERY_PREFIX = "select dr_id, dr_image, dr_description, dr_price, dr_name, dr_active_substance, dr_class from drugs ";
    private static final String IS_PRESCRIPTION_ENABLE_QUERY = "select dr_prescription_enable from drugs where dr_id=?;";
    private static final String GET_DRUG_IMAGE_QUERY = "select dr_image from drugs where dr_id=?";
    private static final String INCREASE_DRUG_COUNT_BY_ORDER_QUERY = "update drugs set dr_in_stock=dr_in_stock+(select or_drug_count from orders where or_id=?) where dr_id=(select or_drug_id from orders where or_id=? and or_client_login=? and or_login_via=?)";
    private static final String REDUCE_DRUG_COUNT_BY_ORDER_QUERY = "update drugs set dr_in_stock=dr_in_stock-(select or_drug_count from orders where or_id=?) where dr_id=(select or_drug_id from orders where or_id=? and or_client_login=? and or_login_via=?)";
    private static final String DRUG_NAME = " dr_name like ? ";
    private static final String ACTIVE_SUBSTANCE = " dr_active_substance like ? ";
    private static final String DRUG_PRICE = " dr_price<=? ";
    private static final String DRUG_CLASS = " dr_class=? ";
    private static final String DRUG_MANUFACTURER = " dr_man_name=? and dr_man_country=? ";
    private static final String IN_STOCK = " dr_in_stock>0 ";
    private static final String PRESCRIPTION_ENABLE = " dr_prescription_enable=false ";
    private static final String EXTENDED_DRUG_SEARCH_TAIL = " order by dr_name limit ?, ?;";
    private static final String IS_PRESCRIPTION_ENABLE_BY_ORDER = "select dr_prescription_enable from drugs where dr_id=(select or_drug_id from orders where or_id=?)";
    private static final String INSERT_DRUG_QUERY = "insert into drugs (dr_name, dr_price, dr_image, dr_prescription_enable, dr_description, dr_man_name, dr_man_country, dr_in_stock, dr_class, dr_type, dr_dosage, dr_active_substance, doctor_specialization) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_DRUG_QUERY = "update drugs set dr_name=?, dr_image=?, dr_price=?,  dr_prescription_enable=?, dr_description=?, dr_in_stock=?, dr_class=?, dr_type=?, dr_dosage=?, dr_active_substance=?, doctor_specialization=?, dr_man_name=?, dr_man_country=?  where dr_id=?;";
    private static final String DELETE_DRUG_QUERY = "delete from drugs where dr_id=?;";
    private static final String REDUCE_DRUG_COUNT_BY_NEW_ORDER = "update drugs set dr_in_stock=dr_in_stock-? where dr_id=?;";
    private static final String GET_DRUG_COUNT_IN_STOCK_QUERY = "select dr_in_stock from drugs where dr_id=?;";
    private static final String GET_AUTO_INCREMENT = "SELECT `AUTO_INCREMENT`\n" +
            "FROM  INFORMATION_SCHEMA.TABLES\n" +
            "WHERE TABLE_SCHEMA = 'online_pharmacy'\n" +
            "AND   TABLE_NAME   = 'drugs';";

    @Override
    public List<Drug> extendedSearching(SearchDrugsCriteria searchDrugsCriteria, int startFrom, int limit)
            throws DaoException {

        List<String> queryCriteria = new ArrayList<>();
        StringBuilder query = new StringBuilder(EXTENDED_DRUGS_SEARCH_QUERY_PREFIX);

        if(searchDrugsCriteria.getName()!=null && !searchDrugsCriteria.getName().isEmpty()){
            queryCriteria.add(DRUG_NAME);
        }

        if(searchDrugsCriteria.getDrugMaxPrice()!=null && !searchDrugsCriteria.getDrugMaxPrice().isEmpty()){
            queryCriteria.add(DRUG_PRICE);
        }

        if(searchDrugsCriteria.getActiveSubstance()!=null && !searchDrugsCriteria.getActiveSubstance().isEmpty()){
            queryCriteria.add(ACTIVE_SUBSTANCE);
        }

        if(searchDrugsCriteria.getDrugClass()!=null && !searchDrugsCriteria.getDrugClass().isEmpty()){
            queryCriteria.add(DRUG_CLASS);
        }

        if(searchDrugsCriteria.getDrugManufacture()!=null){
            queryCriteria.add(DRUG_MANUFACTURER);
        }

        if(searchDrugsCriteria.getOnlyInStock()!=null && !searchDrugsCriteria.getOnlyInStock().isEmpty()
                && Boolean.parseBoolean(searchDrugsCriteria.getOnlyInStock())){
            queryCriteria.add(IN_STOCK);
        }

        if(searchDrugsCriteria.getPrescriptionEnable()!=null && !searchDrugsCriteria.getPrescriptionEnable().isEmpty()
                && !Boolean.parseBoolean(searchDrugsCriteria.getPrescriptionEnable())){
            queryCriteria.add(PRESCRIPTION_ENABLE);
        }

        if(!queryCriteria.isEmpty()){
            query.append(Param.WHERE);
        }

        query.append(String.join(Param.AND, queryCriteria));
        query.append(EXTENDED_DRUG_SEARCH_TAIL);

        try (DatabaseOperation databaseOperation = new DatabaseOperation(query.toString())){
            int paramNumber = 1;

            if(searchDrugsCriteria.getName()!=null && !searchDrugsCriteria.getName().isEmpty()){
                databaseOperation.setParameter(paramNumber++, Param.PER_CENT+searchDrugsCriteria.getName()+Param.PER_CENT);
            }

            if(searchDrugsCriteria.getDrugMaxPrice()!=null && !searchDrugsCriteria.getDrugMaxPrice().isEmpty()){
                databaseOperation.setParameter(paramNumber++, searchDrugsCriteria.getDrugMaxPrice());
            }

            if(searchDrugsCriteria.getActiveSubstance()!=null && !searchDrugsCriteria.getActiveSubstance().isEmpty()){
                databaseOperation.setParameter(paramNumber++, Param.PER_CENT+searchDrugsCriteria.getActiveSubstance()+Param.PER_CENT);
            }

            if(searchDrugsCriteria.getDrugClass()!=null && !searchDrugsCriteria.getDrugClass().isEmpty()){
                databaseOperation.setParameter(paramNumber++, searchDrugsCriteria.getDrugClass());
            }

            if(searchDrugsCriteria.getDrugManufacture()!=null){
                databaseOperation.setParameter(paramNumber++, searchDrugsCriteria.getDrugManufacture().getName());
                databaseOperation.setParameter(paramNumber++, searchDrugsCriteria.getDrugManufacture().getCountry());
            }

            databaseOperation.setParameter(paramNumber++, startFrom);
            databaseOperation.setParameter(paramNumber, limit);

            ResultSet resultSet = databaseOperation.invokeReadOperation();

            return resultSetToDomainOnSearch(resultSet);

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not load drugs from database", e);
        }

    }

    @Override
    public void reduceDrugCountByReestablishedOrder(User user, int orderId) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(REDUCE_DRUG_COUNT_BY_ORDER_QUERY)){

            databaseOperation.setParameter(1, orderId);
            databaseOperation.setParameter(2, orderId);
            databaseOperation.setParameter(3, user.getLogin());
            databaseOperation.setParameter(4, user.getRegistrationType().toString().toLowerCase());

            databaseOperation.invokeWriteOperation();
            databaseOperation.endTransaction();

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not update drug count by canceled query with id="+orderId, e);

        }
    }

    @Override
    public void reduceDrugCountByNewOrder(int drugCount, int drugId) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(REDUCE_DRUG_COUNT_BY_NEW_ORDER)){
            databaseOperation.setParameter(1, drugCount);
            databaseOperation.setParameter(2, drugId);
            databaseOperation.invokeWriteOperation();

            databaseOperation.endTransaction();

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not reduce drug count", e);

        }
    }

    @Override
    public InputStream getDrugImage(int drugId) throws DaoException, FileNotFoundException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_DRUG_IMAGE_QUERY)){
            databaseOperation.setParameter(TableColumn.DRUG_ID, drugId);
            ResultSet resultSet = databaseOperation.invokeReadOperation();

            if(resultSet.next()){
                String pathToImage = resultSet.getString(TableColumn.DRUG_IMAGE);

                if(pathToImage!=null){
                    return new FileInputStream(pathToImage);
                }

            }

            return null;
        } catch (SQLException | ConnectionPoolException | ParameterNotFoundException e) {
            throw new DaoException("Can not load image from database for drug="+drugId, e);

        }
    }

    @Override
    public Drug getDrugById(int drugId) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_DRUGS_BY_ID_QUERY);){

            databaseOperation.setParameter(TableColumn.DRUG_ID, drugId);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            List<Drug> result = resultSetToDrug(resultSet);

            if(result.size()>1){
                throw new MultipleRecordsException("The are more than 1 drug as searched by id");
            }

            if(!result.isEmpty()){
                return result.get(0);
            }

            return null;
        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Something went wrong when trying to get drug by id", e);

        }
    }


    @Override
    public boolean isPrescriptionEnable(int drugId) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(IS_PRESCRIPTION_ENABLE_QUERY)){
            databaseOperation.beginTransaction();
            databaseOperation.setParameter(TableColumn.DRUG_ID, drugId);
            ResultSet resultSet = databaseOperation.invokeReadOperation();

            if(resultSet.next()){
                return resultSet.getBoolean(TableColumn.DRUG_PRESCRIPTION_ENABLE);
            }

            throw new EntityDeletedException("Drug with id="+drugId+" was not found", false);
        } catch (ConnectionPoolException | ParameterNotFoundException | SQLException e) {
            throw new DaoException("Can not select prescription enable from database", e);

        }
    }

    @Override
    public boolean isPrescriptionEnableByOrder(int orderId) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(IS_PRESCRIPTION_ENABLE_BY_ORDER)){

            databaseOperation.setParameter(1, orderId);
            ResultSet resultSet = databaseOperation.invokeReadOperation();

            if (resultSet.next()) {
                return resultSet.getBoolean(TableColumn.DRUG_PRESCRIPTION_ENABLE);
            }

            throw new EntityDeletedException("Order with id=" + orderId + " was not found", false);
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Can not select prescription enable from database", e);

        }
    }


    @Override
    public void updateDrugCountByCanceledOrder(User user, int orderId) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(INCREASE_DRUG_COUNT_BY_ORDER_QUERY)){

            databaseOperation.setParameter(1, orderId);
            databaseOperation.setParameter(2, orderId);
            databaseOperation.setParameter(3, user.getLogin());
            databaseOperation.setParameter(4, user.getRegistrationType().toString().toLowerCase());

            databaseOperation.invokeWriteOperation();

            databaseOperation.endTransaction();

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not update drug count by canceled query with id="+orderId, e);

        }
    }

    @Override
    public List<Drug> searchDrugs(String query, int limit, int startFrom) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(SEARCH_DRUGS_QUERY)){

            databaseOperation.setParameter(TableColumn.DRUG_NAME, Param.PER_CENT+query+Param.PER_CENT);
            databaseOperation.setParameter(TableColumn.DRUG_ACTIVE_SUBSTANCE, Param.PER_CENT+query+Param.PER_CENT);
            databaseOperation.setParameter(TableColumn.DRUG_DESCRIPTION, Param.PER_CENT+query+Param.PER_CENT);
            databaseOperation.setParameter(TableColumn.LIMIT, 1, startFrom);
            databaseOperation.setParameter(TableColumn.LIMIT, 2, limit);

            ResultSet resultSet = databaseOperation.invokeReadOperation();

            return resultSetToDomainOnSearch(resultSet);

        } catch (ConnectionPoolException | SQLException | ParameterNotFoundException e) {
            throw new DaoException("Can not find drugs with params "+query);

        }
    }

    @Override
    public int insertDrug(Drug drug) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_DRUG_QUERY)){
            int drugId = getInsertedDrugId();

            databaseOperation.setParameter(1, drug.getName());
            databaseOperation.setParameter(2, drug.getPrice());
            databaseOperation.setParameter(3, drug.getPathToImage()+drugId);
            databaseOperation.setParameter(4, drug.isPrescriptionEnable());
            databaseOperation.setParameter(5, drug.getDescription());
            databaseOperation.setParameter(6, drug.getDrugManufacturer().getName());
            databaseOperation.setParameter(7, drug.getDrugManufacturer().getCountry());
            databaseOperation.setParameter(8, drug.getDrugsInStock());
            databaseOperation.setParameter(9, drug.getDrugClass().getName());
            databaseOperation.setParameter(10, drug.getType().toString().toLowerCase());
            databaseOperation.setParameter(11, String.join(Param.COMMA, ListConverter.toStringList(drug.getDosages())));
            databaseOperation.setParameter(12, drug.getActiveSubstance());
            databaseOperation.setParameter(13, drug.getDoctorSpecialization());

            databaseOperation.invokeWriteOperation();

            return drugId;

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not insert new drug in database", e);

        }

    }



    @Override
    public void updateDrug(Drug drug) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_DRUG_QUERY)){

            databaseOperation.setParameter(1, drug.getName());
            databaseOperation.setParameter(2, drug.getPathToImage());
            databaseOperation.setParameter(3, drug.getPrice());
            databaseOperation.setParameter(4, drug.isPrescriptionEnable());
            databaseOperation.setParameter(5, drug.getDescription());
            databaseOperation.setParameter(6, drug.getDrugsInStock());
            databaseOperation.setParameter(7, drug.getDrugClass().getName());
            databaseOperation.setParameter(8, drug.getType().toString().toLowerCase());
            databaseOperation.setParameter(9, String.join(Param.COMMA, ListConverter.toStringList(drug.getDosages())));
            databaseOperation.setParameter(10, drug.getActiveSubstance());
            databaseOperation.setParameter(11, drug.getDoctorSpecialization());
            databaseOperation.setParameter(12, drug.getDrugManufacturer().getName());
            databaseOperation.setParameter(13, drug.getDrugManufacturer().getCountry());
            databaseOperation.setParameter(14, drug.getId());

            if(databaseOperation.invokeWriteOperation()==0){
                throw new EntityNotFoundException("Drug with id="+drug.getId()+" was not found");
            }

        } catch (ConnectionPoolException|SQLException e) {
            throw new DaoException("Can not update drug", e);

        }
    }

    @Override
    public void deleteDrug(int drugId) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(DELETE_DRUG_QUERY)){

            databaseOperation.setParameter(1, drugId);

            databaseOperation.invokeWriteOperation();

        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Can not delete drug with id="+drugId, e);

        }
    }

    @Override
    public int getDrugCountInStock(int drugId) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_DRUG_COUNT_IN_STOCK_QUERY)){

            databaseOperation.beginTransaction();
            databaseOperation.setParameter(1, drugId);
            ResultSet resultSet = databaseOperation.invokeReadOperation();

            if(resultSet.next()){
                return resultSet.getInt(TableColumn.DRUG_IN_STOCK);
            }

            throw new EntityNotFoundException("Drug with id="+drugId+" was not found");

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not get drug count in stock", e);

        }
    }

    private List<Drug> resultSetToDomainOnSearch(ResultSet resultSet) throws SQLException {
        List<Drug> result = new ArrayList<>();

        while (resultSet.next()) {
            Drug drug = new Drug();
            DrugClass drugClass = new DrugClass();
            drug.setDrugClass(drugClass);
            drug.setId(resultSet.getInt(TableColumn.DRUG_ID));
            drug.setName(resultSet.getString(TableColumn.DRUG_NAME));
            drug.setPathToImage(resultSet.getString(TableColumn.DRUG_IMAGE));
            drug.setDescription(resultSet.getString(TableColumn.DRUG_DESCRIPTION));
            drug.setActiveSubstance(resultSet.getString(TableColumn.DRUG_ACTIVE_SUBSTANCE));
            drugClass.setName(resultSet.getString(TableColumn.DR_CLASS_COLUMN));
            drug.setPrice(resultSet.getFloat(TableColumn.DRUG_PRICE));
            result.add(drug);
        }

        return result;
    }

    private int getInsertedDrugId() throws ConnectionPoolException, SQLException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_AUTO_INCREMENT)){

            ResultSet resultSet = databaseOperation.invokeReadOperation();
            resultSet.next();

            return resultSet.getInt(TableColumn.AUTO_INCREMENT);
        }
    }



    private List<Drug> resultSetToDrug(ResultSet resultSet) throws SQLException {
        List<Drug> result = new ArrayList<>();

        while (resultSet.next()) {
            Drug drug = new Drug();
            DrugManufacturer drugManufacturer = new DrugManufacturer();
            DrugClass drugClass = new DrugClass();

            drug.setDrugManufacturer(drugManufacturer);
            drug.setDrugClass(drugClass);
            drug.setId(resultSet.getInt(TableColumn.DRUG_ID));
            drug.setName(resultSet.getString(TableColumn.DRUG_NAME));
            drug.setPathToImage((resultSet.getString(TableColumn.DRUG_IMAGE)));
            drug.setDescription(resultSet.getString(TableColumn.DRUG_DESCRIPTION));
            drug.setPrice(resultSet.getFloat(TableColumn.DRUG_PRICE));
            drug.setActiveSubstance(resultSet.getString(TableColumn.DRUG_ACTIVE_SUBSTANCE));
            drug.setPrescriptionEnable(resultSet.getBoolean(TableColumn.DRUG_PRESCRIPTION_ENABLE));
            drug.setDrugsInStock(resultSet.getInt(TableColumn.DRUG_IN_STOCK));
            drug.setInStock(drug.getDrugsInStock()>0);
            drug.setType(DrugType.valueOf(resultSet.getString(TableColumn.DRUG_TYPE).toUpperCase()));

            drugManufacturer.setName(resultSet.getString(TableColumn.DRUG_MANUFACTURE_NAME));
            drugManufacturer.setDescription(resultSet.getString(TableColumn.DRUG_MANUFACTURE_DESCRIPTION));
            drugManufacturer.setCountry(resultSet.getString(TableColumn.DRUG_MANUFACTURE_COUNTRY));
            drugClass.setName(resultSet.getString(TableColumn.DRUG_CLASS_NAME));
            drugClass.setDescription(resultSet.getString(TableColumn.DRUG_CLASS_DESCRIPTION));

            String[] dosages = resultSet.getString(TableColumn.DRUG_DOSAGE).split(Param.COMMA);

            for (String dosage : dosages) {
                drug.getDosages().add(Integer.parseInt(dosage));
            }

            result.add(drug);
        }

        return result;

    }
}
