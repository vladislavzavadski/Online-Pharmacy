package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.DrugDAO;
import by.training.online_pharmacy.dao.OrderDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.exception.EntityNotFoundException;
import by.training.online_pharmacy.dao.exception.OutOfRangeException;
import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.domain.order.OrderStatus;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.OrderService;
import by.training.online_pharmacy.service.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by vladislav on 10.08.16.
 */
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void createOrder(Order order) throws InvalidParameterException, NotFoundException, AmbiguousValueException, PrescriptionNotFoundException {
        if (order.getClient() == null) {
            throw new InvalidParameterException("Parameter order is invalid");
        }
        if (order.getClient().getLogin() == null || order.getClient().getLogin().isEmpty() || order.getClient().getRegistrationType() == null) {
            throw new InvalidParameterException("Parameter client is invalid");
        }
        if (order.getDrug() == null || order.getDrug().getId() <= 0) {
            throw new InvalidParameterException("Parameter drug is invalid");
        }
        if (order.getDrugDosage() <= 0) {
            throw new InvalidParameterException("Parameter dosage is invalid");
        }
        if (order.getDrugCount() <= 0) {
            throw new InvalidParameterException("Parameter drug count is invalid");
        }
        order.setOrderStatus(OrderStatus.ORDERED);

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        DrugDAO drugDAO = daoFactory.getDrugDAO();
        try {
            boolean isPrescriptionEnable = drugDAO.isPrescriptionEnable(order.getDrug().getId());
            order.getDrug().setPrescriptionEnable(isPrescriptionEnable);
        }catch (EntityDeletedException ex){
            throw new NotFoundException("Drug with id=" + order.getDrug().getId() + " was not found", ex);
        }catch (DaoException e) {
            logger.error("Something went wrong when trying check is prescription enable", e);
            throw new InternalServerException(e);
        }

        OrderDAO orderDAO = daoFactory.getOrderDao();
        try {
            orderDAO.insertOrder(order);
        } catch (EntityDeletedException e) {
            throw new NotFoundException("Drug with id=" + order.getDrug().getId() + " was not found", e);
        } catch (OutOfRangeException ex) {
            throw new AmbiguousValueException("The are not " + order.getDrugCount() + " count of drug with id=" + order.getDrug().getId());
        } catch (EntityNotFoundException ex){
            throw new PrescriptionNotFoundException("Prescription not found or timeout");
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to insert order", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public List<Order> getAllUsersOrders(User user, String orderStatus, String dateFrom, String dateTo, String drugName, int startFrom, int limit) throws InvalidParameterException {
        if(user==null||user.getLogin()==null||user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }
        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        OrderDAO orderDAO = daoFactory.getOrderDao();
        try {
            return orderDAO.searchOrders(user, dateFrom, dateTo, orderStatus, drugName, limit, startFrom);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to load orders from database", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void cancelOrder(User user, int orderId) throws InvalidParameterException, OrderNotFoundException {
        if(user==null||user.getLogin()==null||user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(orderId<0){
            throw new InvalidParameterException("Parameter orderId is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        OrderDAO orderDAO = daoFactory.getOrderDao();
        try {
            orderDAO.cancelOrder(user, orderId);
            DrugDAO drugDAO = daoFactory.getDrugDAO();
            drugDAO.updateDrugCountByCanceledOrder(orderId);
        } catch (EntityNotFoundException ex){
            throw new OrderNotFoundException("Order was not found", ex);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to delete order with id="+orderId);
            throw new InternalServerException(e);
        }
    }
}
