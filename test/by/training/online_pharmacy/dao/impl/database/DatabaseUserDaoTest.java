package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.UserDAO;
import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.*;
import org.junit.*;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static by.training.online_pharmacy.dao.impl.database.Constant.*;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by vladislav on 01.10.16.
 */
public class DatabaseUserDaoTest {

    @BeforeClass
    public static void initConnectionPool() throws ConnectionPoolException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        connectionPool.initConnectionPool();
    }

    @Before
    public void reserveConnection() throws ConnectionPoolException, SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        Connection connection = connectionPool.reserveConnection();

        connection.setAutoCommit(false);
    }

    @After
    public void freeConnection() throws ConnectionPoolException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        connectionPool.freeConnection();
    }

    @AfterClass
    public static void destroyConnectionPool() throws ConnectionPoolException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        connectionPool.dispose();
    }

    @Test
    public void getUserMailBySecretWordTest() throws DaoException {
        User user = new User();

        user.setLogin(USER_LOGIN);

        SecretWord secretWord = new SecretWord();

        secretWord.setUser(user);
        secretWord.setResponse(SECRET_RESPONSE);

        UserDAO userDAO = new DatabaseUserDAO();

        String mail = userDAO.getUserMailBySecretWord(secretWord);

        assertEquals(USER_MAIL, mail);
    }

    @Test
    public void addMoneyToBalanceTest() throws DaoException {
        User expected = new User();

        expected.setLogin(USER_LOGIN);
        expected.setRegistrationType(RegistrationType.NATIVE);
        expected.setBalance(202.23);

        UserDAO userDAO = new DatabaseUserDAO();

        userDAO.addMoneyToBalance(expected, (float) 2.23);

        User actual =  userDAO.userAuthentication(USER_LOGIN, RegistrationType.NATIVE);

        assertThat(actual).isEqualToIgnoringNullFields(expected);
    }

    @Test
    public void getProfileImageTest() throws DaoException {
        User expected = new User();

        expected.setLogin(USER_LOGIN);
        expected.setRegistrationType(RegistrationType.NATIVE);

        UserDAO userDAO = new DatabaseUserDAO();

        InputStream inputStream = userDAO.getProfileImage(expected);

        assertNull(inputStream);
    }

    @Test
    public void getUserDetailsTest() throws DaoException {
        User expected = new User();

        expected.setLogin(DOCTOR_LOGIN);
        expected.setRegistrationType(RegistrationType.NATIVE);
        expected.setFirstName(DOCTOR_FIRST_NAME);
        expected.setSecondName(DOCTOR_SECOND_NAME);
        expected.setMail(USER_MAIL);
        expected.setGender(Gender.UNKNOWN);

        UserDAO userDAO = new DatabaseUserDAO();

        User actual = userDAO.getUserDetails(DOCTOR_LOGIN, RegistrationType.NATIVE);

        assertThat(actual).isEqualToIgnoringNullFields(expected);
    }

    @Test
    public void searchDoctorsTest() throws DaoException {
        List<User> expected = new ArrayList<>();

        User user = new User();

        user.setLogin(DOCTOR_LOGIN1);
        user.setRegistrationType(RegistrationType.NATIVE);
        user.setFirstName(DOCTOR_NAME);
        user.setSecondName(DOCTOR_NAME);

        expected.add(user);

        String[] criteria = new String[2];

        criteria[0] = DOCTOR_NAME;
        criteria[1] = DOCTOR_NAME;

        UserDAO userDAO = new DatabaseUserDAO();

        List<User> actual = userDAO.searchDoctors(criteria, 6, 0);

        assertEquals(expected.size(), actual.size());

        for(int i=0; i<expected.size(); i++){
            assertThat(actual.get(i)).isEqualToIgnoringNullFields(expected.get(i));
        }

    }

    @Test
    public void userAuthenticationTest() throws DaoException {
        User expected = new User();

        expected.setLogin(DOCTOR_LOGIN1);
        expected.setRegistrationType(RegistrationType.NATIVE);
        expected.setFirstName(DOCTOR_NAME);
        expected.setSecondName(DOCTOR_NAME);
        expected.setBalance(200.0);

        UserDAO userDAO = new DatabaseUserDAO();

        User actual = userDAO.userAuthentication(DOCTOR_LOGIN1, RegistrationType.NATIVE);

        assertThat(actual).isEqualToIgnoringNullFields(expected);
    }

    @Test
    public void nativeUserAuthenticationTest() throws DaoException {
        User expected = new User();

        expected.setLogin(DOCTOR_LOGIN1);
        expected.setRegistrationType(RegistrationType.NATIVE);
        expected.setFirstName(DOCTOR_NAME);
        expected.setSecondName(DOCTOR_NAME);
        expected.setBalance(200.0);

        UserDAO userDAO = new DatabaseUserDAO();

        User actual = userDAO.userAuthentication(DOCTOR_LOGIN1, DOCTOR_PASSWORD, RegistrationType.NATIVE);

        assertThat(actual).isEqualToIgnoringNullFields(expected);
    }

    @Test
    public void searchDoctorsBySpecialization() throws DaoException {
        List<User> expected = new ArrayList<>();

        User user = new User();

        user.setLogin(DOCTOR_LOGIN1);
        user.setRegistrationType(RegistrationType.NATIVE);
        user.setFirstName(DOCTOR_NAME);
        user.setSecondName(DOCTOR_NAME);

        expected.add(user);

        UserDescription userDescription = new UserDescription();

        userDescription.setSpecialization(DERMATOLOGIST);

        UserDAO userDAO = new DatabaseUserDAO();

        List<User> actual = userDAO.searchDoctors(userDescription, 6, 0);

        assertEquals(expected.size(), actual.size());

        for(int i=0; i<expected.size(); i++){
            assertThat(actual.get(i)).isEqualToIgnoringNullFields(expected.get(i));
        }
    }

    @Test
    public void insertUserTest() throws DaoException {
        User expected = new User();

        expected.setFirstName(DOCTOR_NAME);
        expected.setSecondName(DOCTOR_NAME);
        expected.setLogin(NEW_LOGIN);
        expected.setRegistrationType(RegistrationType.NATIVE);
        expected.setUserRole(UserRole.CLIENT);
        expected.setBalance(0.0);
        expected.setGender(Gender.FEMALE);
        expected.setPassword(NEW_LOGIN);
        expected.setMail(USER_MAIL);

        UserDAO userDAO = new DatabaseUserDAO();

        userDAO.insertUser(expected);

        User actual = userDAO.userAuthentication(NEW_LOGIN, RegistrationType.NATIVE);

        assertThat(actual).isEqualToIgnoringGivenFields(expected, IGNORED_PASSWORD, IGNORED_DESCRIPTION);
    }

    @Test
    public void isLoginUsedTest() throws DaoException {
        UserDAO userDAO = new DatabaseUserDAO();

        boolean isLoginUsed = userDAO.isLoginUsed(DOCTOR_LOGIN);

        assertEquals(true, isLoginUsed);
    }

    @Test
    public void updatePersonalInformationTest() throws DaoException {
        User expected = new User();

        expected.setLogin(DOCTOR_LOGIN1);
        expected.setRegistrationType(RegistrationType.NATIVE);
        expected.setFirstName(NEW_NAME);
        expected.setSecondName(NEW_NAME);
        expected.setGender(Gender.MALE);
        expected.setBalance(200.0);

        UserDAO userDAO = new DatabaseUserDAO();

        userDAO.updatePersonalInformation(expected);

        User actual = userDAO.userAuthentication(DOCTOR_LOGIN1, RegistrationType.NATIVE);

        assertThat(actual).isEqualToIgnoringNullFields(expected);
    }

    @Test
    public void updateUserPasswordTest() throws DaoException {
        User expected = new User();

        expected.setLogin(DOCTOR_LOGIN1);
        expected.setPassword(DOCTOR_PASSWORD);
        expected.setRegistrationType(RegistrationType.NATIVE);

        UserDAO userDAO = new DatabaseUserDAO();

        userDAO.updateUsersPassword(expected, NEW_PASSWORD);

        User actual = userDAO.userAuthentication(DOCTOR_LOGIN1, NEW_PASSWORD, RegistrationType.NATIVE);

        assertNotNull(actual);
    }

    @Test
    public void updateUserContactsTest() throws DaoException {
        User user = new User();

        user.setLogin(DOCTOR_LOGIN1);
        user.setRegistrationType(RegistrationType.NATIVE);
        user.setMail(NEW_MAIL);
        user.setPhone(NEW_PHONE);

        UserDAO userDAO = new DatabaseUserDAO();

        userDAO.updateUsersContacts(user);

        user = userDAO.userAuthentication(DOCTOR_LOGIN1, RegistrationType.NATIVE);

        assertEquals(NEW_MAIL, user.getMail());
        assertEquals(NEW_PHONE, user.getPhone());
    }

    @Test
    public void updateProfileImage() throws DaoException {
        User user = new User();

        user.setLogin(DOCTOR_LOGIN1);
        user.setRegistrationType(RegistrationType.NATIVE);
        user.setPathToImage(NEW_IMAGE_PATH);

        UserDAO userDAO = new DatabaseUserDAO();

        userDAO.uploadProfileImage(user);

        user = userDAO.userAuthentication(DOCTOR_LOGIN1, RegistrationType.NATIVE);

        assertEquals(NEW_IMAGE_PATH, user.getPathToImage());
    }

    @Test
    public void withdrawMoneyFromBalanceTest() throws DaoException {
        User user = new User();

        user.setLogin(DOCTOR_LOGIN1);
        user.setRegistrationType(RegistrationType.NATIVE);

        UserDAO userDAO = new DatabaseUserDAO();

        userDAO.withdrawMoneyFromBalance(user, 10);

        user = userDAO.userAuthentication(DOCTOR_LOGIN1, RegistrationType.NATIVE);

        assertEquals(190.0, user.getBalance());
    }
}
