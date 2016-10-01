package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.*;

import java.io.InputStream;
import java.util.List;

/**
 * Created by vladislav on 13.06.16.
 */
public interface UserDAO {

    /**
     * Retrieve user email by secret word criteria
     * @param secretWord object that represent secret word
     * @return user e-mail or return <code>null</code> if user not found
     * @throws DaoException if fail occurs while invoke read operation*/
    String getUserMailBySecretWord(SecretWord secretWord) throws DaoException;

    /**
     * Add money to user balance
     * @param user object that represent balance owner
     * @param payment represent payment
     * @throws DaoException if fail occurs while invoke write operation*/
    void addMoneyToBalance(User user, float payment) throws DaoException;

    /**
     * Retrieve profile image from storage
     * @param user object that represent image owner
     * @return InputStream that contains user image or if user not found return <code>null</code>
     * @throws DaoException if fail occurs while invoke read operation*/
    InputStream getProfileImage(User user) throws DaoException;

    /**
     * Retrieve full information about user from storage
     * @param userLogin user login
     * @param registrationType registration type
     * @return User that contains full info about user
     * @throws DaoException if fail occurs while invoke read operation*/
    User getUserDetails(String userLogin, RegistrationType registrationType) throws DaoException;

    /**
     * Retrieve users from storage by criteria
     * @param criteria represent criteria
     * @param limit max user's count
     * @param startFrom first entity of result
     * @return List that contains searched users
     * @throws DaoException if fail occurs while invoke read operation*/
    List<User> searchDoctors(String[] criteria, int limit, int startFrom) throws DaoException;

    /**
     * User authentication while login with social networks
     * @param login user login
     * @param registrationType user registration type
     * @return User that contains full info about user
     * @throws DaoException if fail occurs while invoke read operation*/
    User userAuthentication(String login, RegistrationType registrationType) throws DaoException;

    /**
     * User authentication while login as native user
     * @param login user login
     * @param registrationType user registration type
     * @param password user password
     * @return User that contains full info about user and <code>null</code> if user not found
     * @throws DaoException if fail occurs while invoke read operation*/
    User userAuthentication(String login, String password, RegistrationType registrationType) throws DaoException;

    /**
     * Retrieve user's from database with role <code>doctor</code>
     * @param userDescription criteria
     * @param limit max result count
     * @param startFrom number of first entity
     * @return List that contains searched users
     * @throws DaoException if fail occurs while invoke read operation*/
    List<User> searchDoctors(UserDescription userDescription, int limit, int startFrom) throws DaoException;

    /**
     * Insert new user into storage
     * @param user object that represent new user
     * @throws DaoException if fail occurs while invoke write operation*/
    void insertUser(User user) throws DaoException;

    /**
     * Check is login exist
     * @param login checked login
     * @return <code>true</code> if login already used and else <code>false</code>
     * @throws DaoException if fail occurs while invoke read operation*/
    boolean isLoginUsed(String login) throws DaoException;

    /**
     * Update personal info
     * @param user object that represent new user info
     * @throws DaoException if fail occurs while invoke write operation*/
    void updatePersonalInformation(User user) throws DaoException;

    /**
     * Update user password
     * @param user object that represent updated user
     * @param newPassword new user's password
     * @throws DaoException if fail occurs while invoke write operation*/
    void updateUsersPassword (User user, String newPassword) throws DaoException;

    /**
     * Update user contacts
     * @param user object that represent updated user
     * @throws DaoException if fail occurs while invoke write operation*/
    void updateUsersContacts(User user) throws DaoException;

    /**
     * Update user image
     * @param user object that represent updated user
     * @throws DaoException if fail occurs while invoke write operation*/
    void uploadProfileImage(User user) throws DaoException;

    /**
     * Withdraw money from user's balance
     * @param user object of current user
     * @param orderSum order sum
     * @throws DaoException if fail occurs while invoke write operation*/
    void withdrawMoneyFromBalance(User user, float orderSum) throws DaoException;
}
