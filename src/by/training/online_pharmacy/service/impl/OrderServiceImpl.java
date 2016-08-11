package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.OrderDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.exception.OutOfRangeException;
import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.domain.order.OrderStatus;
import by.training.online_pharmacy.service.OrderService;
import by.training.online_pharmacy.service.exception.AmbiguousValueException;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by vladislav on 10.08.16.
 */
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void createOrder(Order order) throws InvalidParameterException, NotFoundException, AmbiguousValueException {
        if(order.getClient()==null){
            throw new InvalidParameterException("Parameter order is invalid");
        }
        if(order.getClient().getLogin()==null||order.getClient().getLogin().isEmpty()||order.getClient().getRegistrationType()==null){
            throw new InvalidParameterException("Parameter client is invalid");
        }
        if(order.getDrug()==null||order.getDrug().getId()<=0){
            throw new InvalidParameterException("Parameter drug is invalid");
        }
        if(order.getDrugDosage()<=0){
            throw new InvalidParameterException("Parameter dosage is invalid");
        }
        if (order.getDrugCount()<0){
            throw new InvalidParameterException("Parameter drug count is invalid");
        }
        order.setOrderStatus(OrderStatus.ORDERED);
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        OrderDAO orderDAO = daoFactory.getOrderDao();
        try {
            orderDAO.insertOrder(order);
        } catch (EntityDeletedException e){
            throw new NotFoundException("Drug with id="+order.getDrug().getId()+" was not found", e);
        } catch (OutOfRangeException ex){
            throw new AmbiguousValueException("The are not "+order.getDrugCount()+" count of drug with id="+order.getDrug().getId());
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to insert order", e);
            throw new InternalServerException(e);
        }
    }
}
