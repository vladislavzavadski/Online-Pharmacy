package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.RequestForPrescriptionDAO;
import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.prescription.RequestForPrescriptionCriteria;
import by.training.online_pharmacy.domain.prescription.RequestStatus;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static by.training.online_pharmacy.dao.impl.database.Constant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by vladislav on 01.10.16.
 */
public class DatabaseRequestForPrescriptionTest {
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
    public void getRequestsCountTest() throws DaoException {
        User doctor = new User();

        doctor.setLogin(DOCTOR_LOGIN);
        doctor.setRegistrationType(RegistrationType.NATIVE);

        RequestForPrescriptionDAO requestForPrescriptionDAO = new DatabaseRequestForPrescriptionDAO();

        int requestsCount = requestForPrescriptionDAO.getRequestsCount(doctor, RequestStatus.IN_PROGRESS);

        assertEquals(1, requestsCount);
    }

    @Test(expected = EntityDeletedException.class)
    public void insertRequestTest() throws DaoException {
        RequestForPrescription requestForPrescription = initRequest();

        RequestForPrescriptionDAO requestForPrescriptionDAO = new DatabaseRequestForPrescriptionDAO();

        requestForPrescriptionDAO.insertRequest(requestForPrescription);

    }

    @Test
    public void updateRequestForPrescriptionTest() throws DaoException {
        RequestForPrescription expected = initRequest();

        RequestForPrescriptionDAO requestForPrescriptionDAO = new DatabaseRequestForPrescriptionDAO();

        requestForPrescriptionDAO.updateRequestForPrescription(expected);

        RequestForPrescriptionCriteria requestForPrescriptionCriteria = new RequestForPrescriptionCriteria();

        RequestForPrescription actual = requestForPrescriptionDAO.getClientRequests(expected.getClient(),
                requestForPrescriptionCriteria, 6, 0).get(0);

        assertThat(actual).isEqualToIgnoringGivenFields(expected, IGNORED_REQUEST_DATE,
                IGNORED_RESPONSE_DATE, IGNORED_PROLONG_DATE, IGNORED_DRUG);

    }

    @Test
    public void getDoctorsRequestsTest() throws DaoException {

        List<RequestForPrescription> expected = new ArrayList<>();

        RequestForPrescription requestForPrescription = initRequest();
        requestForPrescription.setRequestStatus(RequestStatus.IN_PROGRESS);
        requestForPrescription.setDoctorComment(null);

        expected.add(requestForPrescription);
        User doctor = new User();

        doctor.setLogin(DOCTOR_LOGIN);
        doctor.setRegistrationType(RegistrationType.NATIVE);
        doctor.setFirstName(DOCTOR_FIRST_NAME);
        doctor.setSecondName(DOCTOR_SECOND_NAME);

        RequestForPrescriptionCriteria requestForPrescriptionCriteria = new RequestForPrescriptionCriteria();

        RequestForPrescriptionDAO requestForPrescriptionDAO = new DatabaseRequestForPrescriptionDAO();

        List<RequestForPrescription> actual =
                requestForPrescriptionDAO.getDoctorsRequest(doctor, requestForPrescriptionCriteria, 6, 0);

        assertEquals(expected.size(), actual.size());

        for(int i=0; i<expected.size(); i++) {
            assertThat(actual.get(i)).isEqualToIgnoringGivenFields(expected.get(i), IGNORED_REQUEST_DATE,
                    IGNORED_RESPONSE_DATE, IGNORED_PROLONG_DATE, IGNORED_DRUG);
        }
    }

    @Test
    public void getClientsRequestsTest() throws DaoException {
        List<RequestForPrescription> expected = new ArrayList<>();

        RequestForPrescription requestForPrescription = initRequest();
        requestForPrescription.setRequestStatus(RequestStatus.IN_PROGRESS);
        requestForPrescription.setDoctorComment(null);

        expected.add(requestForPrescription);
        User client = new User();

        client.setLogin(USER_LOGIN);
        client.setRegistrationType(RegistrationType.NATIVE);
        client.setFirstName(SENDER_NAME);
        client.setSecondName(SENDER_NAME);

        RequestForPrescriptionCriteria requestForPrescriptionCriteria = new RequestForPrescriptionCriteria();

        RequestForPrescriptionDAO requestForPrescriptionDAO = new DatabaseRequestForPrescriptionDAO();

        List<RequestForPrescription> actual =
                requestForPrescriptionDAO.getClientRequests(client, requestForPrescriptionCriteria, 6, 0);

        assertEquals(expected.size(), actual.size());

        for(int i=0; i<expected.size(); i++) {
            assertThat(actual.get(i)).isEqualToIgnoringGivenFields(expected.get(i), IGNORED_REQUEST_DATE,
                    IGNORED_RESPONSE_DATE, IGNORED_PROLONG_DATE, IGNORED_DRUG);
        }
    }

    private RequestForPrescription initRequest(){

        RequestForPrescription requestForPrescription = new RequestForPrescription();

        User client = new User();

        client.setLogin(USER_LOGIN);
        client.setRegistrationType(RegistrationType.NATIVE);
        client.setFirstName(SENDER_NAME);
        client.setSecondName(SENDER_NAME);

        requestForPrescription.setClient(client);

        User doctor = new User();

        doctor.setLogin(DOCTOR_LOGIN);
        doctor.setRegistrationType(RegistrationType.NATIVE);
        doctor.setFirstName(DOCTOR_FIRST_NAME);
        doctor.setSecondName(DOCTOR_SECOND_NAME);

        requestForPrescription.setDoctor(doctor);

        Drug drug = new Drug();

        drug.setId(2);
        drug.setName(IODOMARIN);

        requestForPrescription.setDrug(drug);
        requestForPrescription.setProlongDate(new Date());
        requestForPrescription.setClientComment(IODOMARIN);
        requestForPrescription.setId(1);
        requestForPrescription.setRequestStatus(RequestStatus.CONFIRMED);
        requestForPrescription.setDoctorComment(IODOMARIN);

        return requestForPrescription;
    }

    private void resetAutoIncrement(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(RESET_REQUEST);
    }
}
