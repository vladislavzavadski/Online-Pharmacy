package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.DrugManufacturerDAO;
import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static by.training.online_pharmacy.dao.impl.database.Constant.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by vladislav on 29.09.16.
 */
public class DatabaseDrugManufacturerDaoTest {
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
    public void isManufactureExistTest() throws DaoException {
        DrugManufacturer drugManufacturer = new DrugManufacturer();
        drugManufacturer.setName(MAN_NAME);
        drugManufacturer.setCountry(MAN_COUNTRY);

        DrugManufacturerDAO drugManufacturerDAO = new DatabaseDrugManufacturerDao();

        boolean actual = drugManufacturerDAO.isManufactureExist(drugManufacturer);

        assertEquals(true, actual);
    }

    @Test
    public void insertDrugManufacturerTest() throws DaoException {
        DrugManufacturer drugManufacturer = new DrugManufacturer();

        drugManufacturer.setName(NEW_DRUG_MANUFACTURER_NAME);
        drugManufacturer.setCountry(NEW_DRUG_MANUFACTURER_COUNTRY);
        drugManufacturer.setDescription(NEW_DRUG_MANUFACTURER_DESCRIPTION);

        DrugManufacturerDAO drugManufacturerDAO = new DatabaseDrugManufacturerDao();

        drugManufacturerDAO.insertDrugManufacturer(drugManufacturer);

        List<DrugManufacturer> drugManufacturers = drugManufacturerDAO.getDrugManufactures();

        assertEquals(true, drugManufacturers.contains(drugManufacturer));
    }

    @Test
    public void getDrugManufacturesTest() throws DaoException {
        List<DrugManufacturer> expected = new ArrayList<>();

        DrugManufacturer drugManufacturer = new DrugManufacturer();

        drugManufacturer.setName(MAN_NAME1);
        drugManufacturer.setDescription(MAN_DESCRIPTION1);
        drugManufacturer.setCountry(MAN_COUNTRY1);

        expected.add(drugManufacturer);

        drugManufacturer = new DrugManufacturer();

        drugManufacturer.setName(MAN_NAME);
        drugManufacturer.setDescription(MAN_DESCRIPTION);
        drugManufacturer.setCountry(MAN_COUNTRY);

        expected.add(drugManufacturer);

        drugManufacturer = new DrugManufacturer();

        drugManufacturer.setCountry(MAN_COUNTRY2);
        drugManufacturer.setDescription(MAN_DESCRIPTION2);
        drugManufacturer.setName(MAN_NAME2);

        expected.add(drugManufacturer);

        drugManufacturer = new DrugManufacturer();

        drugManufacturer.setCountry(MAN_COUNTRY3);
        drugManufacturer.setDescription(MAN_DESCRIPTION3);
        drugManufacturer.setName(MAN_NAME3);

        expected.add(drugManufacturer);

        DrugManufacturerDAO drugManufacturerDAO = new DatabaseDrugManufacturerDao();

        List<DrugManufacturer> actual = drugManufacturerDAO.getDrugManufactures();

        assertEquals(expected, actual);
    }
}
