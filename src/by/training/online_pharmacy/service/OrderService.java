package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.domain.order.SearchOrderCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.*;

import java.util.List;

/**
 * Created by vladislav on 10.08.16.
 */
public interface OrderService {
    void createOrder(Order order) throws InvalidParameterException, NotFoundException, PrescriptionNotFoundException, AmbiguousValueException, InvalidUserStatusException;

    List<Order> getAllUsersOrders(User user, SearchOrderCriteria searchOrderCriteria, int startFrom, int limit) throws InvalidParameterException, InvalidUserStatusException;

    void completeOrder(User user, int orderId) throws InternalServerException, InvalidParameterException, InvalidUserStatusException, OrderNotFoundException;

    Order getOrderById(User user, int orderId) throws InvalidUserStatusException, InvalidParameterException;

    void reestablishOrder(User user, int orderId) throws InvalidParameterException, OrderNotFoundException;

    void payForOrder(User user, int orderId) throws InvalidParameterException, OrderNotFoundException, AmbiguousValueException;

    void cancelOrder(User user, int orderId) throws InvalidParameterException, OrderNotFoundException;
}
