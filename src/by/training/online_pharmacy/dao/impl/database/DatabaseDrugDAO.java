package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.DrugDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.dao.impl.database.util.exception.ParameterNotFoundException;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.DrugClass;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;
import by.training.online_pharmacy.domain.drug.DrugType;

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

    private static final String GET_DRUGS_BY_ID_QUERY = "SELECT dr_id, dr_class, dr_description, dr_image, dr_in_stock, dr_name, dr_prescription_enable, dr_price, dr_type,  dr_dosage, dr_active_substance, dm_id,  dm_name, dm_country, dm_description, dr_class_name, dr_class_description FROM drugs inner join drugs_manufactures on dr_manufacturer = dm_id inner join drug_classes on dr_class = dr_class_name WHERE dr_id=?;";
    private static final String GET_DRUGS_BY_NAME_QUERY = "SELECT dr_id, dr_class, dr_description, dr_image, dr_in_stock, dr_manufacturer, dr_name, dr_prescription_enable, dr_price, dr_type,  dr_dosage, dr_active_substance, dm_id,  dm_name, dm_country, dm_description, dr_class_name, dr_class_description FROM drugs inner join drugs_manufactures on dr_manufacturer = dm_id inner join drug_classes on dr_class = dr_class_name WHERE dr_name LIKE ? LIMIT ?, ?;";
    private static final String GET_DRUGS_BY_CLASS_QUERY = "select dr_price, dr_id, dr_image, dr_description, dr_name, dr_active_substance, dr_class from drugs WHERE dr_class=? order by dr_name LIMIT ?, ?;";
    private static final String GET_DRUGS_BY_ACTIVE_SUBSTANCE_QUERY = "SELECT dr_id, dr_class, dr_description, dr_image, dr_in_stock, dr_manufacturer, dr_name, dr_prescription_enable, dr_price, dr_type,  dr_dosage, dr_active_substance, dm_id, dm_name, dm_country, dm_description, dr_class_name, dr_class_description FROM drugs inner join drugs_manufactures on dr_manufacturer = dm_id inner join drug_classes on dr_class = dr_class_name WHERE dr_active_substance=? LIMIT ?, ?;";
    private static final String INSERT_DRUG_QUERY = "insert into drugs (dr_class, dr_description, dr_image, dr_in_stock, dr_manufacturer, dr_name, dr_prescription_enable, dr_price, dr_type,  dr_dosage, dr_active_substance) values (?,?,?,?,?,?,?,?,?,?,?);";
    private static final String UPDATE_DRUG_QUERY = "update drugs set dr_description = ?, dr_image = ?, dr_in_stock = ?, dr_prescription_enable = ?, dr_dosage = ? where dr_id=?;";
    private static final String DELETE_DRUG_QUERY = "delete from drugs where dr_id=?;";
    private static final String GET_ALL_DRUGS_QUERY = "select dr_price, dr_id, dr_image, dr_description, dr_name, dr_active_substance, dr_class from drugs order by dr_name limit ?, ?;";
    private static final String SEARCH_DRUGS_QUERY = "select dr_price, dr_id, dr_image, dr_description, dr_name, dr_active_substance, dr_class from drugs where dr_name like ? or dr_description like ?  or dr_active_substance like ? order by dr_name limit ?, ?;";
    private static final String EXTENDED_DRUGS_SEARCH_QUERY_PREFIX = "select dr_id, dr_image, dr_description, dr_price, dr_name, dr_active_substance, dr_class from drugs where ";
    private static final String IS_PRESCRIPTION_ENABLE_QUERY = "select dr_prescription_enable from drugs where dr_id=?;";
    private static final String GET_DRUG_IMAGE_QUERY = "select dr_image from drugs where dr_id=?";
    private static final String UPDATE_DRUG_COUNT_BY_ORDER_QUERY = "update drugs set dr_in_stock=dr_in_stock+(select or_drug_count from orders where or_id=?) where dr_id=(select or_drug_id from orders where or_id=?)";
    private static final String AND  = " and ";
    private static final String DRUG_NAME = " dr_name like ? ";
    private static final String ACTIVE_SUBSTANCE = " dr_active_substance like ? ";
    private static final String DRUG_PRICE = " dr_price<=? ";
    private static final String DRUG_CLASS = " dr_class=? ";
    private static final String DRUG_MANUFACTURER = " dr_manufacturer=? ";
    private static final String IN_STOCK = " dr_in_stock>0 ";
    private static final String PRESCRIPTION_ENABLE = " dr_prescription_enable=? ";
    private static final String EXTENDED_DRUG_SEARCH_TAIL = " order by dr_name limit ?, ?;";

    @Override
    public InputStream getDrugImage(int drugId) throws DaoException {
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
        } catch (SQLException | ConnectionPoolException | ParameterNotFoundException | FileNotFoundException e) {
            throw new DaoException("Can not load image from database for drug="+drugId, e);
        }
    }

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
    public List<Drug> extendedSearching(String name, String activeSubstance, String drugMaxPrice, String drugClass, String drugManufacture, String onlyInStock, String prescriptionEnable, int limit, int startFrom) throws DaoException {
        List<String> criteria = new ArrayList<>();
        if(name!=null&&!name.isEmpty()){
            criteria.add(DRUG_NAME);
        }
        if(activeSubstance!=null&&!activeSubstance.isEmpty()){
            criteria.add(ACTIVE_SUBSTANCE);
        }
        if(drugMaxPrice!=null&&!drugMaxPrice.isEmpty()){
            criteria.add(DRUG_PRICE);
        }
        if(drugClass!=null&&!drugClass.isEmpty()){
            criteria.add(DRUG_CLASS);
        }
        if(drugManufacture!=null&&!drugManufacture.isEmpty()){
            criteria.add(DRUG_MANUFACTURER);
        }
        if(onlyInStock!=null&&!onlyInStock.isEmpty()&&Boolean.parseBoolean(onlyInStock)){
            criteria.add(IN_STOCK);
        }
        if(prescriptionEnable!=null&&!prescriptionEnable.isEmpty()){
            criteria.add(PRESCRIPTION_ENABLE);
        }
        try (DatabaseOperation databaseOperation = new DatabaseOperation(EXTENDED_DRUGS_SEARCH_QUERY_PREFIX+String.join(AND, criteria)+EXTENDED_DRUG_SEARCH_TAIL)){
            int paramNumber = 1;
            if(name!=null&&!name.isEmpty()){
                databaseOperation.setParameter(paramNumber++, Param.PER_CENT+name+Param.PER_CENT);
            }
            if(activeSubstance!=null&&!activeSubstance.isEmpty()){
                databaseOperation.setParameter(paramNumber++, Param.PER_CENT+activeSubstance+Param.PER_CENT);
            }
            if(drugMaxPrice!=null&&!drugMaxPrice.isEmpty()){
                databaseOperation.setParameter(paramNumber++, Double.parseDouble(drugMaxPrice));
            }
            if(drugClass!=null&&!drugClass.isEmpty()){
                databaseOperation.setParameter(paramNumber++, drugClass);
            }
            if(drugManufacture!=null&&!drugManufacture.isEmpty()){
                databaseOperation.setParameter(paramNumber++, Integer.parseInt(drugManufacture));
            }
            if(prescriptionEnable!=null&&!prescriptionEnable.isEmpty()){
                databaseOperation.setParameter(paramNumber++, Boolean.parseBoolean(prescriptionEnable));
            }
            databaseOperation.setParameter(paramNumber++, startFrom);
            databaseOperation.setParameter(paramNumber, limit);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            return resultSetToDomainOnSearch(resultSet);
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not load drugs with extended searching form database", e);
        }
    }

    @Override
    public boolean isPrescriptionEnable(int drugId) throws DaoException {
        try {
            DatabaseOperation databaseOperation = new DatabaseOperation(IS_PRESCRIPTION_ENABLE_QUERY);
            databaseOperation.setParameter(TableColumn.DRUG_ID, drugId);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            if(resultSet.next()){
                return resultSet.getBoolean(TableColumn.DRUG_PRESCRIPTION_ENABLE);
            }
            throw new EntityDeletedException("Drug with id="+drugId+" was not found", false);
        } catch (ConnectionPoolException | ParameterNotFoundException | SQLException e) {
            throw new DaoException("Can not select prescription enable from database");
        }
    }

    @Override
    public void updateDrugCountByCanceledOrder(int orderId) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(UPDATE_DRUG_COUNT_BY_ORDER_QUERY, true)){
            databaseOperation.setParameter(TableColumn.ORDER_ID, orderId);
            databaseOperation.setParameter(TableColumn.ORDER_ID+1, orderId);
            databaseOperation.invokeWriteOperation();
            databaseOperation.endTransaction();
        } catch (SQLException | ConnectionPoolException | ParameterNotFoundException e) {
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
    public List<Drug> getAllDrugs(int limit, int startFrom, boolean pageOverload) throws DaoException {
        List<Drug> result;
        boolean isConnectionReserved = startFrom==0&&pageOverload;
        try (DatabaseOperation databaseOperation = new DatabaseOperation(GET_ALL_DRUGS_QUERY, isConnectionReserved)){
            databaseOperation.setParameter(TableColumn.LIMIT, 1, startFrom);
            databaseOperation.setParameter(TableColumn.LIMIT, 2, limit);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            result = resultSetToDomainOnSearch(resultSet);
            return result;
        } catch (SQLException | ParameterNotFoundException | ConnectionPoolException e) {
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
