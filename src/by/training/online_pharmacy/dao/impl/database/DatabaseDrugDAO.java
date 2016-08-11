package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.DrugDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.dao.impl.database.util.exception.ParameterNotFoundException;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.DrugClass;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;
import by.training.online_pharmacy.domain.drug.DrugType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladislav on 15.06.16.
 */
public class DatabaseDrugDAO implements DrugDAO {

    private static final String GET_DRUGS_BY_ID_QUERY = "SELECT dr_id, dr_class, dr_description, dr_image, dr_in_stock, dr_name, dr_prescription_enable, dr_price, dr_type,  dr_dosage, dr_active_substance, dm_id,  dm_name, dm_country, dm_description, dr_class_name, dr_class_description FROM drugs inner join drugs_manufactures on dr_manufacturer = dm_id inner join drug_classes on dr_class = dr_class_name WHERE dr_id=?;";
    private static final String GET_DRUGS_BY_NAME_QUERY = "SELECT dr_id, dr_class, dr_description, dr_image, dr_in_stock, dr_manufacturer, dr_name, dr_prescription_enable, dr_price, dr_type,  dr_dosage, dr_active_substance, dm_id,  dm_name, dm_country, dm_description, dr_class_name, dr_class_description FROM drugs inner join drugs_manufactures on dr_manufacturer = dm_id inner join drug_classes on dr_class = dr_class_name WHERE dr_name LIKE ? LIMIT ?, ?;";
    private static final String GET_DRUGS_BY_CLASS_QUERY = "select dr_price, dr_id, dr_image, dr_description, dr_name, dr_active_substance, dr_class from drugs WHERE dr_class=? order by dr_name LIMIT ?, ?;";
    private static final String GET_DRUGS_BY_ACTIVE_SUBSTANCE_QUERY = "SELECT dr_id, dr_class, dr_description, dr_image, dr_in_stock, dr_manufacturer, dr_name, dr_prescription_enable, dr_price, dr_type,  dr_dosage, dr_active_substance, dm_id, dm_name, dm_country, dm_description, dr_class_name, dr_class_description FROM drugs inner join drugs_manufactures on dr_manufacturer = dm_id inner join drug_classes on dr_class = dr_class_name WHERE dr_active_substance=? LIMIT ?, ?;";
    private static final String INSERT_DRUG_QUERY = "insert into drugs (dr_class, dr_description, dr_image, dr_in_stock, dr_manufacturer, dr_name, dr_prescription_enable, dr_price, dr_type,  dr_dosage, dr_active_substance) values (?,?,?,?,?,?,?,?,?,?,?);";
    private static final String UPDATE_DRUG_QUERY = "update drugs set dr_description = ?, dr_image = ?, dr_in_stock = ?, dr_prescription_enable = ?, dr_dosage = ? where dr_id=?;";
    private static final String DELETE_DRUG_QUERY = "delete from drugs where dr_id=?;";
    private static final String GET_ALL_DRUGS_QUERY = "select dr_price, dr_id, dr_image, dr_description, dr_name, dr_active_substance, dr_class from drugs order by dr_name limit ?, ?;";
    private static final String SEARCH_DRUGS_QUERY = "select dr_price, dr_id, dr_image, dr_description, dr_name, dr_active_substance, dr_class from drugs where dr_name like ? or dr_description like ?  or dr_active_substance like ? order by dr_name limit ?, ?;";
    private static final String EXTENDER_DRUGS_SEARCH_QUERY = "select dr_id, dr_image, dr_description, dr_price, dr_name, dr_active_substance, dr_class from drugs where dr_name like ?  and dr_active_substance like ? order by dr_name limit ?, ?;";
    private static final String PRESCRIPTION = " and dr_prescription_enable=false ";
    private static final String IN_STOCK = " and dr_in_stock>0 ";
    private static final String TYPE = " and dr_type=? ";
    private static final String DR_CLASS = " and dr_class=? ";
    private static final String DR_MANUFACTURE = " and dr_manufacturer=? ";
    private static final String DR_PRICE = " and dr_price<=? ";
    private static final String INSERT_REQUEST = "insert into requests_for_prescriptions (re_client_login, re_drug_id, re_prolong_to, re_status, re_clients_comment, re_user_login_via, re_doctors_comment, re_request_date, re_doctor, re_doctor_login_via) \n" +
            "select 'asd', 2, curdate(), 'in_progress', 'efregerg', 'native', 'ergererg', CURDATE(), doc.doc_login, doc.doc_via from\n" +
            "(select doctors.us_login as doc_login, doctors.login_via as doc_via, \n" +
            "count(re_id) as count from requests_for_prescriptions right join \n" +
            "(select us_login, login_via from users inner join staff_descriptions on us_login=sd_user_login and login_via=sd_login_via where\n" +
            "sd_specialization=(select doctor_specialization from drugs where dr_id=2)) as doctors on re_doctor=doctors.us_login and re_doctor_login_via=doctors.login_via where re_status='in_progress' or re_status is NULL group by re_doctor,\n" +
            "re_doctor_login_via  order by count limit 1) as doc;\n" +
            "#Есть таблица request_for_prescriptions. Из множеста пользователей из предыдушего запроса выбрать пользователя на которого не ссылается ни одна\n" +
            "#запись из request_for_prescriptions или(если на каждого  пользователя есть ссылка) выбрать пользователя с минимальным числом ссылок\n";
    private static final int INSERT_PLACE = 121;

    @Override
    public Drug getDrugById(int drugId) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(GET_DRUGS_BY_ID_QUERY)) {
            databaseOperation.setParameter(TableColumn.DRUG_ID, drugId);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            List<Drug> result = resultSetToDrug(resultSet);
            if(!result.isEmpty()){
                return result.get(0);
            }
            return null;
        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Something went wrong when trying to get drug by id", e);
        } catch (Exception e) {
            throw new DaoException("Something went wrong when trying to get drug by id", e);
        }
    }

    @Override
    public List<Drug> extendedSearching(Drug pattern, int limit, int startFrom) throws DaoException {
        StringBuilder query = new StringBuilder(EXTENDER_DRUGS_SEARCH_QUERY);
        if(!pattern.isPrescriptionEnable()){
            query.insert(INSERT_PLACE, PRESCRIPTION);
        }
        if(pattern.isInStock()){
            query.insert(INSERT_PLACE, IN_STOCK);
        }
        if(pattern.getType()!=null){
            query.insert(INSERT_PLACE, TYPE);
        }
        if(pattern.getDrugManufacturer()!=null){
            query.insert(INSERT_PLACE, DR_MANUFACTURE);
        }
        if(pattern.getDrugClass()!=null){
            query.insert(INSERT_PLACE, DR_CLASS);
        }
        if(pattern.getPrice()>0.0){
            query.insert(INSERT_PLACE,DR_PRICE);
        }
        try (DatabaseOperation databaseOperation = new DatabaseOperation(query.toString())){
            databaseOperation.setParameter(TableColumn.DRUG_NAME, Param.PER_CENT+pattern.getName()+Param.PER_CENT);
            if(pattern.getDrugManufacturer()!=null) {
                databaseOperation.setParameter(TableColumn.MANUFACTURE, pattern.getDrugManufacturer().getId());
            }
            if(pattern.getPrice()>0.0) {
                databaseOperation.setParameter(TableColumn.DRUG_PRICE, pattern.getPrice());
            }
            if(pattern.getDrugClass()!=null) {
                databaseOperation.setParameter(TableColumn.DR_CLASS_COLUMN, pattern.getDrugClass().getName());
            }
            databaseOperation.setParameter(TableColumn.DRUG_ACTIVE_SUBSTANCE, Param.PER_CENT+pattern.getActiveSubstance()+Param.PER_CENT);
            if(pattern.getType()!=null){
                databaseOperation.setParameter(TableColumn.DRUG_TYPE, pattern.getType().toString().toLowerCase());
            }

            databaseOperation.setParameter(TableColumn.LIMIT, 1, startFrom);
            databaseOperation.setParameter(TableColumn.LIMIT, 2, limit);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            return resultSetToDomainOnSearch(resultSet);
        } catch (SQLException | ConnectionPoolException | ParameterNotFoundException e) {
            throw new DaoException("Can not load drugs with extended searching form database", e);
        } catch (Exception e) {
            throw new DaoException("Can not load drugs with extended searching form database", e);
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
        } catch (Exception e) {
            throw new DaoException("Can not find drugs with params "+query);
        }
    }

    @Override
    public List<Drug> getAllDrugs(int limit, int startFrom) throws DaoException {
        List<Drug> result;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_ALL_DRUGS_QUERY)){
            databaseOperation.setParameter(TableColumn.LIMIT, 1, startFrom);
            databaseOperation.setParameter(TableColumn.LIMIT, 2, limit);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            result = resultSetToDomainOnSearch(resultSet);
            return result;
        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not get drugs with limit = "+limit+" and startFrom = "+startFrom, e);
        } catch (Exception e) {
            throw new DaoException("Can not get drugs with limit = "+limit+" and startFrom = "+startFrom, e);
        }
    }

    @Override
    public List<Drug> getDrugsByName(String name, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public List<Drug> getDrugsByClass(String drugClass, int limit, int startFrom) throws DaoException {
        List<Drug> result;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_DRUGS_BY_CLASS_QUERY)){
            databaseOperation.setParameter(TableColumn.DR_CLASS_COLUMN, drugClass);
            databaseOperation.setParameter(TableColumn.LIMIT, 1, startFrom);
            databaseOperation.setParameter(TableColumn.LIMIT, 2, limit);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            result = resultSetToDomainOnSearch(resultSet);
            return result;
        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
            throw new DaoException("Can not load drugs with class "+drugClass, e);
        } catch (Exception e) {
            throw new DaoException("Can not load drugs with class "+drugClass, e);
        }
    }

    @Override
    public List<Drug> getDrugsByActiveSubstance(String activeSubstance, int limit, int startFrom) throws DaoException {
        return null;
    }

    @Override
    public void insertDrug(Drug drug) throws DaoException {

    }

    @Override
    public void updateDrug(Drug drug) throws DaoException {

    }

    @Override
    public void deleteDrug(int drugId) throws DaoException {

    }


    /*@Override
    public Drug getDrugById(int drugId) throws DaoException {

        Drug drug = null;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_DRUGS_BY_ID_QUERY, drugId)){

            ResultSet resultSet = databaseOperation.invokeReadOperation();
            List<Drug> result = resultSetToDrug(resultSet);
            if(!result.isEmpty()){
                drug = result.get(0);
            }
            return drug;
        } catch (Exception e) {
            throw new DaoException("Can not load drug with id = \'"+drugId+"\' from database", e);
        }

    }

    @Override
    public List<Drug> getDrugsByName(String name, int limit, int startFrom) throws DaoException {
        List<Drug> drugs;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_DRUGS_BY_NAME_QUERY, "%"+name+"%", limit, startFrom);){
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            drugs = resultSetToDrug(resultSet);
            return drugs;
        } catch (Exception e) {
            throw new DaoException("Can not load drugs with name like \'"+name+"\' from database", e);
        }
    }

    @Override
    public List<Drug> getDrugsByClass(String drugClass, int limit, int startFrom) throws DaoException {
        List<Drug> drugs;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_DRUGS_BY_CLASS_QUERY, drugClass, limit, startFrom);){

            ResultSet resultSet = databaseOperation.invokeReadOperation();
            drugs = resultSetToDrug(resultSet);
            return drugs;
        } catch (Exception e) {
            throw new DaoException("Can not load drugs with class = \'"+drugClass+"\' from database", e);
        }

    }

    @Override
    public List<Drug> getDrugsByActiveSubstance(String activeSubstance, int limit, int startFrom) throws DaoException {
        List<Drug> drugs;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_DRUGS_BY_ACTIVE_SUBSTANCE_QUERY, activeSubstance, limit, startFrom)){

            ResultSet resultSet = databaseOperation.invokeReadOperation();
            drugs = resultSetToDrug(resultSet);
            return drugs;
        } catch (Exception e) {
            throw new DaoException("Can not load drugs with activeSubstance = \'"+activeSubstance+"\' from database", e);
        }

    }


    @Override
    public void insertDrug(Drug drug) throws DaoException {
        String dosages = "";
        for(int i:drug.getDosages()){
            dosages+=","+i;
        }
        try(DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_DRUG_QUERY, drug.getDrugClass().getName(), drug.getDescription(), drug.getDrugImage(), drug.isInStock()
                ,drug.getDrugManufacturer().getId(), drug.getName(), drug.isPrescriptionEnable(), drug.getPrice(), drug.getType().toString().toLowerCase(), dosages.substring(1), drug.getActiveSubstance())) {

            databaseOperation.invokeWriteOperation();

        } catch (Exception e) {
            throw new DaoException("Can not insert drug into database", e);
        }

    }

    @Override
    public void updateDrug(Drug drug) throws DaoException {
        String dosages = "";
        for (int i : drug.getDosages()) {
            dosages += "," + i;
        }
        try (DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_DRUG_QUERY, drug.getDescription(), drug.getDrugImage(), drug.isInStock(), drug.isPrescriptionEnable(), dosages.substring(1), drug.getId());){
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Can not update drug into database", e);
        }

    }

    @Override
    public void deleteDrug(int drugId) throws DaoException {
        try(DatabaseOperation databaseOperation = new DatabaseOperation(DELETE_DRUG_QUERY, drugId)) {
            databaseOperation.invokeWriteOperation();
        } catch (Exception e) {
            throw new DaoException("Can not delete drug with id = \'"+drugId+"\' from database", e);
        }

    }*/

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
            String[] dosages = resultSet.getString(TableColumn.DRUG_DOSAGE).split(",");
            for (String dosage : dosages) {
                drug.getDosages().add(Integer.parseInt(dosage));
            }
            drugManufacturer.setId(resultSet.getInt(TableColumn.DRUG_MANUFACTURE_ID));
            drugManufacturer.setName(resultSet.getString(TableColumn.DRUG_MANUFACTURE_NAME));
            drugManufacturer.setDescription(resultSet.getString(TableColumn.DRUG_MANUFACTURE_DESCRIPTION));
            drugManufacturer.setCountry(resultSet.getString(TableColumn.DRUG_MANUFACTURE_COUNTRY));
            drugClass.setName(resultSet.getString(TableColumn.DRUG_CLASS_NAME));
            drugClass.setDescription(resultSet.getString(TableColumn.DRUG_CLASS_DESCRIPTION));
            result.add(drug);
        }
        return result;

    }
}
