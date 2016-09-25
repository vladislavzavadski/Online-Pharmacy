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

    /**
     * Change order status in storage
     * @param orderId order identification
     * @param orderStatus new order status
     * @throws DaoException if fail occurs while invoke write operation*/
    void setOrderStatus(OrderStatus orderStatus, int orderId) throws DaoException;

    /**
     * Retrieve orders by id from storage
     * @param orderId order identification
     * @return Order object that represent order with id=orderId
     * @throws DaoException if fail occurs while invoke read operation*/
    Order getOrderById(int orderId) throws DaoException;

    /**
     * Retrieve order sum from storage
     * @return float that contains
     * @param user object that represent user that create new order
     * @param orderId order identification
     * @throws DaoException if fail occurs while invoke read operation*/
    float getOrderSum(User user, int orderId) throws DaoException;

    /**
     * Search all orders in storage by criteria
     * @param searchOrderCriteria object that contains criteria for orders
     * @param startFrom from this number orders will be retrieved from storage
     * @param limit orders count that will be retrieved from storage
     * @return List that contains searched orders
     * @throws DaoException if fail occurs while invoke read operation*/
    List<Order> searchOrders(SearchOrderCriteria searchOrderCriteria, int startFrom, int limit) throws DaoException;

    /**
     * Search orders in storage for specific user
     * @param user for this user orders will be searched in storage
     * @param searchOrderCriteria object that contains criteria for orders
     * @param startFrom from this number orders will be retrieved from storage
     * @param limit orders count that will be retrieved from storage
     * @return List that contains searched orders
     * @throws DaoException if fail occurs while invoke read operation*/
    List<Order> searchOrders(User user, SearchOrderCriteria searchOrderCriteria, int startFrom, int limit) throws DaoException;

    /**
     * Set order status for specific user
     * @param user object for this user order status will be changed
     * @param orderStatus new order status
     * @param orderId order identification
     * @throws DaoException if fail occurs while invoke write operation*/
    void setOrderStatus(User user, OrderStatus orderStatus, int orderId) throws DaoException;

    /**
     * Create new order
     * @param order objact that represent new order
     * @throws DaoException if if fail occurs while invoke write operation*/
    void insertOrder(Order order) throws DaoException;

}
