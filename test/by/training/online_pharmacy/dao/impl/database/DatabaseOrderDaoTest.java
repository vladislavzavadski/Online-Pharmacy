package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.OrderDAO;
import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.domain.order.OrderStatus;
import by.training.online_pharmacy.domain.order.SearchOrderCriteria;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static by.training.online_pharmacy.dao.impl.database.Constant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by vladislav on 30.09.16.
 */
public class DatabaseOrderDaoTest {
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
    public void setOrderStatusTest() throws DaoException {

        OrderDAO orderDAO = new DatabaseOrderDAO();

        orderDAO.setOrderStatus(OrderStatus.COMPLETED, 3);

        Order order = orderDAO.getOrderById(3);

        assertEquals(OrderStatus.COMPLETED, order.getOrderStatus());
    }

    @Test
    public void setOrderStatusUserTest() throws DaoException {
        User user = new User();

        user.setLogin(USER_LOGIN);
        user.setRegistrationType(RegistrationType.NATIVE);
        user.setFirstName(SENDER_NAME);
        user.setSecondName(SENDER_NAME);

        OrderDAO orderDAO = new DatabaseOrderDAO();

        orderDAO.setOrderStatus(user, OrderStatus.CANCELED, 2);

        Order order = orderDAO.getOrderById(2);

        assertEquals(OrderStatus.CANCELED, order.getOrderStatus());
    }

    @Test
    public void insertOrderTest() throws ParseException, DaoException {
        Order expected = initOrder();

        expected.getClient().setLogin(USER_LOGIN);
        expected.getClient().setRegistrationType(RegistrationType.NATIVE);

        expected.setId(4);
        expected.setDrugCount(22);

        OrderDAO orderDAO = new DatabaseOrderDAO();

        orderDAO.insertOrder(expected);

        Order actual = orderDAO.getOrderById(4);

        assertThat(actual).isEqualToIgnoringGivenFields(expected, IGNORED_CLIENT, IGNORED_SUM, IGNORED_ORDER_DATE);
    }

    @Test
    public void getOrderByIdTest() throws ParseException, DaoException {
        Order expected = initOrder();

        OrderDAO orderDAO = new DatabaseOrderDAO();

        Order actual = orderDAO.getOrderById(3);

        assertEquals(expected, actual);
    }

    @Test
    public void getOrderSumTest() throws DaoException {
        User user = new User();

        user.setLogin(USER_LOGIN);
        user.setRegistrationType(RegistrationType.NATIVE);
        user.setFirstName(SENDER_NAME);
        user.setSecondName(SENDER_NAME);

        OrderDAO orderDAO = new DatabaseOrderDAO();

        float orderSum = orderDAO.getOrderSum(user, 1);

        assertEquals(5.10, orderSum, 0.1);
    }

    @Test
    public void searchOrdersUserTest() throws DaoException, ParseException {
        User user = new User();

        user.setLogin(USER_LOGIN);
        user.setRegistrationType(RegistrationType.NATIVE);
        user.setFirstName(SENDER_NAME);
        user.setSecondName(SENDER_NAME);

        SearchOrderCriteria searchOrderCriteria = new SearchOrderCriteria();
        searchOrderCriteria.setOrderStatus(OrderStatus.PAID.toString().toLowerCase());

        OrderDAO orderDAO = new DatabaseOrderDAO();

        List<Order> actual = orderDAO.searchOrders(user, searchOrderCriteria, 0, 6);

        List<Order> expected = new ArrayList<>();
        expected.add(initOrder());

        assertEquals(expected, actual);
    }

    @Test
    public void searchOrdersTest() throws DaoException, ParseException {
        SearchOrderCriteria searchOrderCriteria = new SearchOrderCriteria();
        searchOrderCriteria.setOrderStatus(OrderStatus.PAID.toString().toLowerCase());

        OrderDAO orderDAO = new DatabaseOrderDAO();

        List<Order> actual = orderDAO.searchOrders(searchOrderCriteria, 0, 6);

        List<Order> expected = new ArrayList<>();
        expected.add(initOrder());

        assertEquals(expected, actual);
    }



    private Order initOrder() throws ParseException {
        Order order = new Order();

        order.setId(3);

        User user = new User();

        user.setFirstName(SENDER_NAME);
        user.setSecondName(SENDER_NAME);

        order.setClient(user);

        Drug drug = new Drug();

        drug.setId(2);
        drug.setName(IODOMARIN);

        order.setDrug(drug);
        order.setDrugCount(1);
        order.setDrugDosage((short) 350);
        order.setTotalSum(0.50);
        order.setOrderStatus(OrderStatus.PAID);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);

        order.setOrderDate(simpleDateFormat.parse(DATE));

        return order;
    }

    private void resetAutoIncrement(Connection connection) throws SQLException {

        Statement statement = connection.createStatement();
        statement.execute(Constant.RESET_ORDERS_QUERY);

    }
}
