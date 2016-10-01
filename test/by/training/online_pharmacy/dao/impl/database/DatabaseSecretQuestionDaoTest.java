package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.SecretQuestionDao;
import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.SecretQuestion;
import by.training.online_pharmacy.domain.user.User;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static by.training.online_pharmacy.dao.impl.database.Constant.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by vladislav on 01.10.16.
 */
public class DatabaseSecretQuestionDaoTest {

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
    public void getAllQuestionsTest() throws DaoException {

        List<SecretQuestion> expected = new ArrayList<>();

        SecretQuestion secretQuestion = new SecretQuestion();

        secretQuestion.setQuestion(HOW_ARE_YOU_QUESTION);
        secretQuestion.setId(0);

        expected.add(secretQuestion);

        SecretQuestionDao secretQuestionDao = new DatabaseSecretQuestionDao();

        List<SecretQuestion> actual = secretQuestionDao.getAllQuestions();

        assertEquals(expected, actual);
    }

    @Test
    public void getUsersSecretQuestion() throws DaoException {
        User user = new User();

        user.setLogin(USER_LOGIN);
        user.setRegistrationType(RegistrationType.NATIVE);

        SecretQuestion expected = new SecretQuestion();

        expected.setQuestion(HOW_ARE_YOU_QUESTION);
        expected.setId(0);

        SecretQuestionDao secretQuestionDao = new DatabaseSecretQuestionDao();

        SecretQuestion actual = secretQuestionDao.getUsersSecretQuestion(user);

        assertEquals(expected, actual);
    }
}
