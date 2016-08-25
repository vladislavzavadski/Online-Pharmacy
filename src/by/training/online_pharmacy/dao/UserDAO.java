package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserDescription;
import by.training.online_pharmacy.domain.user.UserRole;

import java.io.InputStream;
import java.util.List;

/**
 * Created by vladislav on 13.06.16.
 */
public interface UserDAO {
    InputStream getProfileImage(User user) throws DaoException;

    User getUserDetails(String userLogin, RegistrationType registrationType) throws DaoException;

    List<User> searchUsers(String[] criteria, int limit, int startFrom) throws DaoException;

    User userAuthentication(String login, RegistrationType registrationType) throws DaoException;

    User userAuthentication(String login, String password, RegistrationType registrationType) throws DaoException;
    User getUserByLogin(String login, RegistrationType registrationType) throws DaoException;
    List<User> searchUsersByRole(UserRole userRole, int startFrom, int limit) throws DaoException;

    List<User> getDoctorsBySpecialization(UserDescription userDescription, int limit, int startFrom) throws DaoException;

    List<User> searchUsersByName(String firstName, String secondName, int limit, int startFrom) throws DaoException;
    void insertUser(User user) throws DaoException;
    int deleteUser(User user) throws DaoException;
    boolean isLoginUsed(String login) throws DaoException;
    void updatePersonalInformation(User user) throws DaoException;
    void updateUsersPassword (User user, String newPassword) throws DaoException;
    void updateUsersContacts(User user) throws DaoException;
    void uploadProfileImage(User user) throws DaoException;

    List<User> getAllDoctors(int limit, int startFrom, boolean pageOverload) throws DaoException;
}
