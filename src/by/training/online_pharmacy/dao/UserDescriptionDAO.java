package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.UserDescription;

import java.util.List;

/**
 * Created by vladislav on 18.06.16.
 */
public interface UserDescriptionDAO {

    /**
     * Insert user description into storage
     * @param userDescription object that represent new user description
     * @throws DaoException if fail occurs while invoke write operation*/
    void insertUserDescription(UserDescription userDescription) throws DaoException;

    /**
     * Check is specialization exist
     * @param specialization string that represent specialization
     * @return <code>true</code> if specialization exist else <code>false</code>*/
    boolean isSpecializationExist(String specialization) throws DaoException;

    /**
     * Update user description in storage
     * @param userDescription object that represent new user description
     * @throws DaoException if fail occurs while invoke write operation*/
    void updateUserDescription(UserDescription userDescription) throws DaoException;

    /**
     * Retrieve all doctors specializations
     * @return List that contains all user descriptions
     * @throws DaoException if fail occurs while invoke read operation*/
    List<UserDescription> getAllSpecializations() throws DaoException;
}
