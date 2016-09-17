package by.training.online_pharmacy.dao;

import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.domain.order.OrderStatus;
import by.training.online_pharmacy.domain.order.SearchOrderCriteria;
import by.training.online_pharmacy.domain.user.User;

import java.util.List;


/**
 * Created by vladislav on 19.06.16.
 */
public interface OrderDAO {
    void completeOrder(int orderId) throws DaoException;

    List<Order> getOrderById(int orderId) throws DaoException;

    float getOrderSum(User user, int orderId) throws DaoException;

    List<Order> searchAllOrders(SearchOrderCriteria searchOrderCriteria, int startFrom, int limit) throws DaoException;

    List<Order> searchUsersOrders(User user, SearchOrderCriteria searchOrderCriteria, int startFrom, int limit) throws DaoException;

    void setOrderStatus(User user, OrderStatus orderStatus, int orderId) throws DaoException;

    void insertOrder(Order order) throws DaoException;

}
