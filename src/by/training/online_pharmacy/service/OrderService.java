package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.*;

import java.util.List;

/**
 * Created by vladislav on 10.08.16.
 */
public interface OrderService {
    void createOrder(Order order) throws InvalidParameterException, NotFoundException, AmbiguousValueException, PrescriptionNotFoundException;

    List<Order> getAllUsersOrders(User user, String orderStatus, String dateFrom, String dateTo, String drugName, int limit, int startFrom) throws InvalidParameterException;

    void cancelOrder(User user, int orderId) throws InvalidParameterException, OrderNotFoundException;
}
