package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.DrugClassDAO;
import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.drug.DrugClass;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static by.training.online_pharmacy.dao.impl.database.Constant.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by vladislav on 28.09.16.
 */
public class DatabaseDrugClassDaoTest {

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
    public void getAllDrugClassesTest() throws DaoException {

        List<DrugClass> expected = new ArrayList<>();

        DrugClass drugClass = new DrugClass();

        drugClass.setName(ANALGETIK);
        drugClass.setDescription(OLD_TYPE);

        expected.add(drugClass);

        drugClass = new DrugClass();

        drugClass.setName(ANTIBIOTIK);
        drugClass.setDescription(ANTIBIO_DESCR);

        expected.add(drugClass);

        drugClass = new DrugClass();

        drugClass.setName(ANTIKOGNESTAT);
        drugClass.setDescription(ANTIKOGNESTAT_DESCR);

        expected.add(drugClass);


        DrugClassDAO databaseDrugClassDAO = new DatabaseDrugClassDAO();

        List<DrugClass> real = databaseDrugClassDAO.getAllDrugClasses();

        assertEquals(expected, real);

    }

    @Test
    public void isDrugClassExistTest() throws DaoException {

        DrugClassDAO drugClassDAO = new DatabaseDrugClassDAO();

        boolean real = drugClassDAO.isDrugClassExist(ANTIBIOTIK);

        assertEquals(true, real);
    }

    @Test
    public void insertDrugClassTest() throws DaoException {

        DrugClass drugClass = new DrugClass();

        drugClass.setName(VITAMIN);
        drugClass.setDescription(VITAMIN);

        DrugClassDAO drugClassDAO = new DatabaseDrugClassDAO();

        drugClassDAO.insertDrugClass(drugClass);

        boolean real = drugClassDAO.isDrugClassExist(VITAMIN);

        assertEquals(true, real);
    }
}
