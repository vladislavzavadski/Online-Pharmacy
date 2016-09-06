package by.training.online_pharmacy.dao.impl.database;


import by.training.online_pharmacy.dao.UserDescriptionDAO;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.domain.user.UserDescription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladislav on 18.06.16.
 */
public class DatabaseUserDescriptionDAO implements UserDescriptionDAO {
    private static final String INSERT_DESCRIPTION_QUERY = "INSERT INTO staff_descriptions (sd_user_login, sd_specialization, sd_description, sd_login_via) VALUES (?, ?, ?, ?);";
    private static final String GET_DOCTORS_SPECIALIZATION = "select distinct sd_specialization from staff_descriptions order by sd_specialization";
    private static final String IS_SPECIALIZATION_EXIST = "select distinct sd_specialization from staff_descriptions where sd_specialization=?;";
    @Override
    public void insertUserDescription(UserDescription userDescription) throws DaoException {
        try {
            DatabaseOperation databaseOperation = new DatabaseOperation(INSERT_DESCRIPTION_QUERY);
            databaseOperation.setParameter(1, userDescription.getUserLogin());
            databaseOperation.setParameter(2, userDescription.getSpecialization());
            databaseOperation.setParameter(3, userDescription.getDescription());
            databaseOperation.setParameter(4, userDescription.getRegistrationType().toString().toLowerCase());
            databaseOperation.invokeWriteOperation();
            databaseOperation.endTransaction();
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Can not insert new doctors description="+userDescription, e);
        }
    }

    @Override
    public boolean isSpecializationExist(String specialization) throws DaoException {
        DatabaseOperation databaseOperation;
        boolean result;
        try {
            databaseOperation = new DatabaseOperation(IS_SPECIALIZATION_EXIST);
            databaseOperation.setParameter(1, specialization);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            result = resultSet.next();
            return result;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Can not check is specialization exist", e);
        }

    }

    @Override
    public UserDescription getUserDescriptionByLogin(String userLogin) throws DaoException {
        return null;
    }

    @Override
    public void updateUserDescription(UserDescription userDescription) throws DaoException {

    }

    @Override
    public void deleteUserDescription(String userLogin) throws DaoException {

    }

    @Override
    public List<UserDescription> getAllSpecializations() throws DaoException {
        List<UserDescription> result = new ArrayList<>();
        DatabaseOperation databaseOperation = null;
        try{
            databaseOperation = new DatabaseOperation(GET_DOCTORS_SPECIALIZATION);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            while (resultSet.next()){
                UserDescription userDescription = new UserDescription();
                userDescription.setSpecialization(resultSet.getString(TableColumn.USER_SPECIALIZATION));
                result.add(userDescription);
            }
            return result;
        } catch (SQLException | ConnectionPoolException e) {
            if(databaseOperation!=null){
                try {
                    databaseOperation.close();
                } catch (SQLException e1) {
                    throw new DaoException("Can not load doctors specializations from database", e);
                }
            }
            throw new DaoException("Can not load doctors specializations from database", e);
        }
    }
}
