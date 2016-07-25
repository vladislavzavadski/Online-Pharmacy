package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserRole;

import java.util.List;

/**
 * Created by vladislav on 13.06.16.
 */
public interface UserDAO {
    User userAuthentication(String login, String password, RegistrationType registrationType) throws DaoException;
    User userAuthentication(User user) throws DaoException;
    User getUserByLogin(String login) throws DaoException;
    List<User> searchUsersByRole(UserRole userRole, int startFrom, int limit) throws DaoException;
    List<User> searchUsersByName(String firstName, String secondName, int limit, int startFrom) throws DaoException;
    void insertUser(User user) throws DaoException;
    void updateUser(User user) throws DaoException;
    void deleteUser(String login) throws DaoException;
    List<User> getUsersBySpecialization(String specialization, int limit, int startFrom) throws DaoException;

}
