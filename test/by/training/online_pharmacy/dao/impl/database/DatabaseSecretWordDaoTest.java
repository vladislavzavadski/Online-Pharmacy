package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.SecretWordDao;
import by.training.online_pharmacy.dao.UserDAO;
import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.SecretQuestion;
import by.training.online_pharmacy.domain.user.SecretWord;
import by.training.online_pharmacy.domain.user.User;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;

import static by.training.online_pharmacy.dao.impl.database.Constant.DOCTOR_LOGIN;
import static by.training.online_pharmacy.dao.impl.database.Constant.SECRET_RESPONSE;
import static by.training.online_pharmacy.dao.impl.database.Constant.USER_MAIL;
import static org.junit.Assert.assertEquals;

/**
 * Created by vladislav on 01.10.16.
 */
public class DatabaseSecretWordDaoTest {

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
    public void createSecretWordTest() throws DaoException {

        User user = new User();

        user.setLogin(DOCTOR_LOGIN);
        user.setRegistrationType(RegistrationType.NATIVE);

        SecretQuestion secretQuestion = new SecretQuestion();

        secretQuestion.setId(0);

        SecretWord secretWord = new SecretWord();

        secretWord.setUser(user);
        secretWord.setSecretQuestion(secretQuestion);
        secretWord.setResponse(SECRET_RESPONSE);

        SecretWordDao secretWordDao = new DatabaseSecretWordDao();

        secretWordDao.createSecretWord(secretWord);

        UserDAO userDAO = new DatabaseUserDAO();

        String mail = userDAO.getUserMailBySecretWord(secretWord);

        assertEquals(USER_MAIL, mail);

    }
}
