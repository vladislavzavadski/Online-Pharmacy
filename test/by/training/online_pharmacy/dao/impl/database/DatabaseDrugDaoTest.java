package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.DrugDAO;
import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.drug.*;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import org.junit.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static by.training.online_pharmacy.dao.impl.database.Constant.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;


/**
 * Created by vladislav on 29.09.16.
 */
public class DatabaseDrugDaoTest {

    @BeforeClass
    public static void initConnectionPool() throws ConnectionPoolException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        connectionPool.initConnectionPool();
    }

    @Before
    public void reserveConnection() throws ConnectionPoolException, SQLException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        Connection connection = connectionPool.reserveConnection();

        resetAutoIncrement(connection);

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
    public void extendedSearchingTest() throws DaoException {

        List<Drug> expectedResult = new ArrayList<>();

        Drug drug = new Drug();

        drug.setId(1);
        drug.setName(AMBROXOL);
        drug.setPrice((float) 1.70);
        drug.setPathToImage(AMBROXOL_IMAGE);
        drug.setDescription(AMBROXOL);
        drug.setActiveSubstance(AMBROXOL);

        DrugClass drugClass = new DrugClass();
        drug.setDrugClass(drugClass);

        drugClass.setName(ANTIBIOTIK);

        expectedResult.add(drug);

        drug = new Drug();

        drug.setId(2);
        drug.setName(IODOMARIN);
        drug.setPrice((float)0.5);
        drug.setPathToImage(IODOMARIN_IMAGE);
        drug.setDescription(IOD);
        drug.setActiveSubstance(IOD);

        drugClass = new DrugClass();

        drugClass.setName(ANTIBIOTIK);

        drug.setDrugClass(drugClass);

        expectedResult.add(drug);

        SearchDrugsCriteria searchDrugsCriteria = new SearchDrugsCriteria();

        DrugDAO drugDAO = new DatabaseDrugDAO();

        List<Drug> realResult = drugDAO.extendedSearching(searchDrugsCriteria, 0, 6);

        assertEquals(expectedResult, realResult);

    }

    @Test
    public void reduceDrugCountByReestablishedOrderTest() throws DaoException {
        User user = new User();

        user.setLogin(USER_LOGIN);
        user.setRegistrationType(RegistrationType.NATIVE);

        DrugDAO drugDAO = new DatabaseDrugDAO();

        Drug drug = drugDAO.getDrugById(1);

        assertNotSame(4, drug.getDrugsInStock());
        drugDAO.reduceDrugCountByReestablishedOrder(user, 1);

        drug = drugDAO.getDrugById(1);

        assertEquals(4, drug.getDrugsInStock());

    }

    @Test
    public void reduceDrugCountByNewOrderTest() throws DaoException {

        DrugDAO drugDAO = new DatabaseDrugDAO();

        Drug drug = drugDAO.getDrugById(2);

        assertNotSame(4, drug.getDrugsInStock());

        drugDAO.reduceDrugCountByNewOrder(3, 2);

        drug = drugDAO.getDrugById(2);

        assertEquals(4, drug.getDrugsInStock());
    }

    @Test(expected = FileNotFoundException.class)
    public void getDrugImageTest() throws IOException, DaoException {
        DrugDAO drugDAO = new DatabaseDrugDAO();

        drugDAO.getDrugImage(2);

    }

    @Test
    public void getDrugByIdTest() throws DaoException {
        Drug expected = new Drug();

        expected.setId(1);
        expected.setName(AMBROXOL);
        expected.setPrice((float) 1.70);
        expected.setPathToImage(AMBROXOL_IMAGE);
        expected.setDescription(AMBROXOL);
        expected.setActiveSubstance(AMBROXOL);
        expected.setDrugsInStock(7);
        expected.setPrescriptionEnable(false);
        expected.setType(DrugType.SYROP);

        expected.setInStock(true);

        List<Integer> dosages = new ArrayList<>();

        dosages.add(100);
        dosages.add(300);
        dosages.add(350);

        expected.setDosages(dosages);

        DrugManufacturer drugManufacturer = new DrugManufacturer();

        drugManufacturer.setName(MAN_NAME);
        drugManufacturer.setDescription(MAN_DESCRIPTION);
        drugManufacturer.setCountry(MAN_COUNTRY);


        expected.setDrugManufacturer(drugManufacturer);

        DrugClass drugClass = new DrugClass();
        expected.setDrugClass(drugClass);

        drugClass.setName(ANTIBIOTIK);
        drugClass.setDescription(ANTIBIO_DESCR);

        DrugDAO drugDAO = new DatabaseDrugDAO();
        Drug actual = drugDAO.getDrugById(1);

        assertEquals(expected, actual);
    }

    @Test
    public void isPrescriptionEnableTest() throws DaoException {

        DrugDAO drugDAO = new DatabaseDrugDAO();

        boolean actual = drugDAO.isPrescriptionEnable(1);

        assertEquals(false, actual);
    }

    @Test
    public void isPrescriptionEnableByOrderTest() throws DaoException {

        DrugDAO drugDAO = new DatabaseDrugDAO();

        boolean actual = drugDAO.isPrescriptionEnableByOrder(1);

        assertEquals(false, actual);
    }

    @Test
    public void updateDrugCountByCanceledOrderTest() throws DaoException {
        User user = new User();

        user.setLogin(USER_LOGIN);
        user.setRegistrationType(RegistrationType.NATIVE);

        DrugDAO drugDAO = new DatabaseDrugDAO();

        Drug drug = drugDAO.getDrugById(1);

        assertNotSame(10, drug.getDrugsInStock());

        drugDAO.updateDrugCountByCanceledOrder(user, 1);

        drug = drugDAO.getDrugById(1);

        assertEquals(10, drug.getDrugsInStock());
    }

    @Test
    public void searchDrugsTest() throws DaoException {
        List<Drug> expectedResult = new ArrayList<>();

        Drug drug = new Drug();

        drug.setId(1);
        drug.setName(AMBROXOL);
        drug.setPrice((float) 1.70);
        drug.setPathToImage(AMBROXOL_IMAGE);
        drug.setDescription(AMBROXOL);
        drug.setActiveSubstance(AMBROXOL);

        DrugClass drugClass = new DrugClass();
        drug.setDrugClass(drugClass);

        drugClass.setName(ANTIBIOTIK);

        expectedResult.add(drug);

        drug = new Drug();

        drug.setId(2);
        drug.setName(IODOMARIN);
        drug.setPrice((float)0.5);
        drug.setPathToImage(IODOMARIN_IMAGE);
        drug.setDescription(IOD);
        drug.setActiveSubstance(IOD);

        drugClass = new DrugClass();

        drugClass.setName(ANTIBIOTIK);

        drug.setDrugClass(drugClass);

        expectedResult.add(drug);

        DrugDAO drugDAO = new DatabaseDrugDAO();

        List<Drug> actual = drugDAO.searchDrugs(SEARCH_QUERY, 6, 0);

        assertEquals(expectedResult, actual);
    }

    @Test
    public void deleteDrugTest() throws DaoException {
        DrugDAO drugDAO = new DatabaseDrugDAO();

        List<Drug> drugs = drugDAO.searchDrugs(SEARCH_QUERY, 6, 0);

        assertNotSame(1, drugs.size());

        drugDAO.deleteDrug(1);

        drugs = drugDAO.searchDrugs(SEARCH_QUERY, 6, 0);

        assertEquals(1, drugs.size());
    }

    @Test
    public void getDrugCountTest() throws DaoException {
        DrugDAO drugDAO = new DatabaseDrugDAO();

        int drugsInStock = drugDAO.getDrugCountInStock(1);

        assertEquals(7, drugsInStock);
    }

    @Test
    public void insertDrugTest() throws DaoException {

        Drug drug = getInitDrug();

        DrugDAO drugDAO = new DatabaseDrugDAO();

        drugDAO.insertDrug(drug);

        drug.setDoctorSpecialization(null);

        Drug actual = drugDAO.getDrugById(3);

        assertEquals(drug, actual);

    }

    @Test
    public void updateDrugTest() throws DaoException {
        Drug drug = getInitDrug();
        drug.setId(1);

        DrugDAO drugDAO = new DatabaseDrugDAO();

        drugDAO.updateDrug(drug);

        drug.setDoctorSpecialization(null);

        Drug actual = drugDAO.getDrugById(1);

        assertEquals(drug, actual);
    }

    private void resetAutoIncrement(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(RESET_QUERY);
    }

    private Drug getInitDrug(){
        Drug drug = new Drug();

        drug.setId(3);
        drug.setName(NEW_DRUG_NAME);
        drug.setPrice((float)2.50);
        drug.setPathToImage(AMBROXOL_IMAGE);
        drug.setPrescriptionEnable(true);
        drug.setDescription(NEW_DRUG_DESCRIPTION);
        drug.setDrugsInStock(10);
        drug.setActiveSubstance(NEW_DRUG_ACTIVE_SUBSTANCE);
        drug.setDoctorSpecialization(NEW_DRUG_SPECIALIZATION);
        drug.setType(DrugType.CAPSULE);
        drug.setInStock(true);

        List<Integer> dosages = new ArrayList<>();
        dosages.add(100);
        dosages.add(200);

        drug.setDosages(dosages);

        DrugClass drugClass = new DrugClass();

        drugClass.setName(ANTIBIOTIK);
        drugClass.setDescription(ANTIBIO_DESCR);

        drug.setDrugClass(drugClass);

        DrugManufacturer drugManufacturer = new DrugManufacturer();

        drugManufacturer.setName(MAN_NAME);
        drugManufacturer.setCountry(MAN_COUNTRY);
        drugManufacturer.setDescription(MAN_DESCRIPTION);
        drug.setDrugManufacturer(drugManufacturer);

        return drug;
    }

}
