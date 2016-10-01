package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.PrescriptionDAO;
import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.prescription.Prescription;
import by.training.online_pharmacy.domain.prescription.PrescriptionCriteria;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import static by.training.online_pharmacy.dao.impl.database.Constant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladislav on 01.10.16.
 */
public class DatabasePrescriptionDaoTest {

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
    public void createPrescriptionTest() throws ParseException, DaoException {
        Prescription expected = new Prescription();

        expected.setDrugCount((short) 20);
        expected.setDrugDosage((short) 500);
        expected.setExpirationDate(new SimpleDateFormat(DATE_PATTERN).parse(EXP_DATE));

        User doctor = new User();

        doctor.setLogin(DOCTOR_LOGIN);
        doctor.setRegistrationType(RegistrationType.NATIVE);

        expected.setDoctor(doctor);

        PrescriptionDAO prescriptionDAO = new DatabasePrescriptionDAO();

        prescriptionDAO.createPrescription(expected, 1);

        User client = new User();

        client.setLogin(USER_LOGIN);
        client.setRegistrationType(RegistrationType.NATIVE);

        Prescription actual = prescriptionDAO.getActiveUserPrescription(client, 2);

        assertThat(actual).isEqualToIgnoringGivenFields(expected, IGNORED_DOCTOR, IGNORED_EXPIRATION_DATE);
    }

    @Test
    public void increaseDrugCountByOrderTest() throws DaoException {
        User client = new User();

        client.setLogin(USER_LOGIN);
        client.setRegistrationType(RegistrationType.NATIVE);

        PrescriptionDAO prescriptionDAO = new DatabasePrescriptionDAO();

        prescriptionDAO.increaseDrugCountByOrder(client, 2);

        Prescription prescription = prescriptionDAO.getActiveUserPrescription(client, 2);

        assertEquals(9, prescription.getDrugCount());

    }

    @Test
    public void getActiveUserPrescriptionTest() throws DaoException {
        Prescription expected = new Prescription();

        expected.setDrugCount((short) 5);
        expected.setDrugDosage((short) 400);
        User client = new User();

        client.setLogin(USER_LOGIN);
        client.setRegistrationType(RegistrationType.NATIVE);

        PrescriptionDAO prescriptionDAO = new DatabasePrescriptionDAO();

        Prescription actual = prescriptionDAO.getActiveUserPrescription(client, 2);

        assertEquals(expected, actual);

    }

    @Test
    public void reduceDrugCountTest() throws DaoException {
        User client = new User();

        client.setLogin(USER_LOGIN);
        client.setRegistrationType(RegistrationType.NATIVE);

        PrescriptionDAO prescriptionDAO = new DatabasePrescriptionDAO();

        prescriptionDAO.reduceDrugCount(client, 2);

        Prescription prescription = prescriptionDAO.getActiveUserPrescription(client, 2);

        assertEquals(1, prescription.getDrugCount());
    }

    @Test
    public void reduceDrugCountDrugIdTest() throws DaoException {
        User client = new User();

        client.setLogin(USER_LOGIN);
        client.setRegistrationType(RegistrationType.NATIVE);

        PrescriptionDAO prescriptionDAO = new DatabasePrescriptionDAO();

        prescriptionDAO.reduceDrugCount(client, 2, 1, 400);

        Prescription prescription = prescriptionDAO.getActiveUserPrescription(client, 2);

        assertEquals(4, prescription.getDrugCount());
    }


    @Test
    public void getUserPrescriptionsTest() throws DaoException {
        Prescription prescription = new Prescription();

        User client = new User();

        client.setLogin(USER_LOGIN);
        client.setRegistrationType(RegistrationType.NATIVE);
        client.setFirstName(SENDER_NAME);
        client.setSecondName(SENDER_NAME);

        prescription.setClient(client);

        User doctor = new User();

        doctor.setLogin(DOCTOR_LOGIN);
        doctor.setRegistrationType(RegistrationType.NATIVE);
        doctor.setFirstName(DOCTOR_FIRST_NAME);
        doctor.setSecondName(DOCTOR_SECOND_NAME);

        prescription.setDoctor(doctor);

        Drug drug = new Drug();

        drug.setId(2);
        drug.setName(IODOMARIN);

        prescription.setDrug(drug);
        prescription.setDrugDosage((short) 400);
        prescription.setDrugCount((short) 5);

        List<Prescription> expected = new ArrayList<>();

        expected.add(prescription);

        PrescriptionCriteria prescriptionCriteria = new PrescriptionCriteria();

        prescriptionCriteria.setDrugName(IODOMARIN);

        PrescriptionDAO prescriptionDAO = new DatabasePrescriptionDAO();

        List<Prescription> actual = prescriptionDAO.getUserPrescriptions(client, prescriptionCriteria, 6, 0);

        assertEquals(expected.size(), actual.size());

        for(int i=0; i<expected.size(); i++){
            assertThat(actual.get(i)).isEqualToIgnoringNullFields(expected.get(i));
        }

    }

    @Test
    public void getDoctorPrescriptionsTest() throws DaoException {
        Prescription prescription = new Prescription();

        User client = new User();

        client.setLogin(USER_LOGIN);
        client.setRegistrationType(RegistrationType.NATIVE);
        client.setFirstName(SENDER_NAME);
        client.setSecondName(SENDER_NAME);

        prescription.setClient(client);

        User doctor = new User();

        doctor.setLogin(DOCTOR_LOGIN);
        doctor.setRegistrationType(RegistrationType.NATIVE);
        doctor.setFirstName(DOCTOR_FIRST_NAME);
        doctor.setSecondName(DOCTOR_SECOND_NAME);

        prescription.setDoctor(doctor);

        Drug drug = new Drug();

        drug.setId(2);
        drug.setName(IODOMARIN);

        prescription.setDrug(drug);
        prescription.setDrugDosage((short) 400);
        prescription.setDrugCount((short) 5);

        List<Prescription> expected = new ArrayList<>();

        expected.add(prescription);

        PrescriptionCriteria prescriptionCriteria = new PrescriptionCriteria();

        prescriptionCriteria.setDrugName(IODOMARIN);

        PrescriptionDAO prescriptionDAO = new DatabasePrescriptionDAO();

        List<Prescription> actual = prescriptionDAO.getDoctorPrescriptions(doctor, prescriptionCriteria, 6, 0);

        assertEquals(expected.size(), actual.size());

        for(int i=0; i<expected.size(); i++){
            assertThat(actual.get(i)).isEqualToIgnoringNullFields(expected.get(i));
        }

    }

}
