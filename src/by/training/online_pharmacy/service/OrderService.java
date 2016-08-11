package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.service.exception.AmbiguousValueException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;

/**
 * Created by vladislav on 10.08.16.
 */
public interface OrderService {
    void createOrder(Order order) throws InvalidParameterException, NotFoundException, AmbiguousValueException;
}
