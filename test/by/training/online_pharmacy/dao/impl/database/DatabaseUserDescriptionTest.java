package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.UserDAO;
import by.training.online_pharmacy.dao.UserDescriptionDAO;
import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserDescription;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static by.training.online_pharmacy.dao.impl.database.Constant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by vladislav on 01.10.16.
 */
public class DatabaseUserDescriptionTest {

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
    public void insertUserDescriptionTest() throws DaoException {
        UserDescription userDescription = new UserDescription();

        userDescription.setUserLogin(USER_LOGIN11);
        userDescription.setRegistrationType(RegistrationType.NATIVE);
        userDescription.setSpecialization(SOME_SPECIALIZATION);
        userDescription.setDescription(SOME_DESCRIPTION);

        UserDescriptionDAO userDescriptionDAO = new DatabaseUserDescriptionDAO();

        userDescriptionDAO.insertUserDescription(userDescription);

        UserDAO userDAO = new DatabaseUserDAO();

        User user = userDAO.getUserDetails(USER_LOGIN11, RegistrationType.NATIVE);

        assertThat(userDescription).isEqualToIgnoringNullFields(user.getUserDescription());

    }

    @Test
    public void isSpecializationExistTest() throws DaoException {

        UserDescriptionDAO userDescriptionDAO = new DatabaseUserDescriptionDAO();

        boolean isExist = userDescriptionDAO.isSpecializationExist(OPHTHALMOLOGIST);

        assertEquals(true, isExist);
    }

    @Test
    public void updateUserDescription() throws DaoException {
        UserDescription userDescription = new UserDescription();

        userDescription.setUserLogin(DOCTOR_LOGIN1);
        userDescription.setRegistrationType(RegistrationType.NATIVE);
        userDescription.setSpecialization(SOME_SPECIALIZATION);
        userDescription.setDescription(SOME_DESCRIPTION);

        UserDescriptionDAO userDescriptionDAO = new DatabaseUserDescriptionDAO();

        userDescriptionDAO.updateUserDescription(userDescription);

        UserDAO userDAO = new DatabaseUserDAO();

        User user = userDAO.getUserDetails(DOCTOR_LOGIN1, RegistrationType.NATIVE);

        assertThat(userDescription).isEqualToIgnoringNullFields(user.getUserDescription());
    }

    @Test
    public void getAllSpecializationsTest() throws DaoException {

        List<UserDescription> expected = new ArrayList<>();

        UserDescription userDescription = new UserDescription();

        userDescription.setSpecialization(DERMATOLOGIST);

        expected.add(userDescription);

        userDescription = new UserDescription();
        userDescription.setSpecialization(LOHR);

        expected.add(userDescription);

        userDescription = new UserDescription();
        userDescription.setSpecialization(OPHTHALMOLOGIST);

        expected.add(userDescription);
        userDescription = new UserDescription();
        userDescription.setSpecialization(THERAPIST);

        expected.add(userDescription);
        userDescription = new UserDescription();
        userDescription.setSpecialization(SURGEON);

        expected.add(userDescription);

        userDescription = new UserDescription();
        userDescription.setSpecialization(ENDOCRINOLOGIST);

        expected.add(userDescription);

        UserDescriptionDAO userDescriptionDAO = new DatabaseUserDescriptionDAO();

        List<UserDescription> actual = userDescriptionDAO.getAllSpecializations();

        assertEquals(expected, actual);
    }
}
