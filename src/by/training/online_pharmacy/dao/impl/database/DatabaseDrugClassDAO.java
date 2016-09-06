package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.DrugClassDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.domain.drug.DrugClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public class DatabaseDrugClassDAO implements DrugClassDAO {
    private static final String GET_CLASS_BY_NAME_QUERY = "select dr_class_name, dr_class_description from drug_classes where dr_class_name=? LIMIT 1;";
    private static final String INSERT_DRUG_CLASS_QUERY = "insert into drug_classes (dr_class_name, dr_class_description) VALUES(?,?);";
    private static final String UPDATE_DRUG_CLASS_QUERY = "update drug_classes set dr_class_name=?, dr_class_description=? WHERE dr_class_name=?;";
    private static final String DELETE_DRUG_CLASS_QUERY = "delete from drug_classes where dr_class_name=?;";
    private static final String GET_ALL_CLASSES = "select dr_class_name, dr_class_description from drug_classes order by dr_class_name;";
    private static final String IS_DRUG_CLASS_EXIST_QUERY = "select dr_class_name from drug_classes where dr_class_name=?";
    @Override
    public DrugClass getDrugClassByName(String name) throws DaoException {
        return null;
    }

    @Override
    public void insertDrugClass(DrugClass drugClass) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_DRUG_CLASS_QUERY)){
            databaseOperation.setParameter(1, drugClass.getName());
            databaseOperation.setParameter(2, drugClass.getDescription());
            databaseOperation.invokeWriteOperation();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not insert new drug class", e);
        }
    }

    @Override
    public void updateDrugClass(DrugClass drugClass, String oldDrugClassName) throws DaoException {

    }

    @Override
    public void deleteDrugClass(String name) throws DaoException {

    }

    @Override
    public List<DrugClass> getAllDrugClasses() throws DaoException{
        DatabaseOperation databaseOperation = null;
        try {
            databaseOperation = new DatabaseOperation(GET_ALL_CLASSES);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            return resultSetToDrugClass(resultSet);
        } catch (ConnectionPoolException | SQLException e) {
            if(databaseOperation!=null){
                try {
                    databaseOperation.close();
                } catch (SQLException e1) {
                    throw new DaoException("Can not load drug classes from database", e);
                }
            }
            throw new DaoException("Can not load drug classes from database", e);
        }
    }

    @Override
    public boolean isDrugClassExist(String className) throws DaoException {
        try (DatabaseOperation databaseOperation = new DatabaseOperation(IS_DRUG_CLASS_EXIST_QUERY)){
            databaseOperation.setParameter(1, className);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            return resultSet.next();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not check is drug class exist", e);
        }
    }


    private List<DrugClass> resultSetToDrugClass(ResultSet resultSet) throws SQLException {
        List<DrugClass> result = new ArrayList<>();
        while (resultSet.next()) {
            DrugClass drugClass = new DrugClass();
            drugClass.setName(resultSet.getString(TableColumn.DRUG_CLASS_NAME));
            drugClass.setDescription(resultSet.getString(TableColumn.DRUG_CLASS_DESCRIPTION));
            result.add(drugClass);
        }
        return result;

    }
}
