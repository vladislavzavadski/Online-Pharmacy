package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.UserDescription;

import java.util.List;

/**
 * Created by vladislav on 18.06.16.
 */
public interface UserDescriptionDAO {
    void insertUserDescription(UserDescription userDescription) throws DaoException;

    boolean isSpecializationExist(String specialization) throws DaoException;

    UserDescription getUserDescriptionByLogin(String userLogin) throws DaoException;
    void updateUserDescription(UserDescription userDescription) throws DaoException;
    void deleteUserDescription(String userLogin) throws DaoException;

    List<UserDescription> getAllSpecializations() throws DaoException;
}
