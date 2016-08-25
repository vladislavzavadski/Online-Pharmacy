package by.training.online_pharmacy.dao;

import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.Period;
import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.domain.order.OrderStatus;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;

import java.util.Date;
import java.util.List;


/**
 * Created by vladislav on 19.06.16.
 */
public interface OrderDAO {
    List<Order> searchOrders(User user, String dateFrom, String dateTo, String orderStatus, String drugName, int limit, int startFrom) throws DaoException;

    Order getOrderById(int orderId) throws DaoException;
    List<Order> getOrdersByStatus(OrderStatus orderStatus, int limit, int startFrom) throws DaoException;
    List<Order> getOrdersByDrugId(int drugId, int limit, int startFrom) throws DaoException;
    List<Order> getOrdersByDate(Date date, Period period, int limit, int startFrom) throws DaoException;
    void updateOrder(Order order) throws DaoException;

    void cancelOrder(User user, int orderId) throws DaoException;

    void insertOrder(Order order) throws DaoException;
    void deleteOrder(int orderId) throws DaoException;
}
