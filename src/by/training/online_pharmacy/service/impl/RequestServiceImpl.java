package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.RequestForPrescriptionDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.exception.EntityNotFoundException;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.prescription.RequestForPrescriptionCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserRole;
import by.training.online_pharmacy.service.RequestService;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.InvalidUserStatusException;
import by.training.online_pharmacy.service.exception.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;

import java.util.List;

/**
 * Created by vladislav on 10.08.16.
 */
public class RequestServiceImpl implements RequestService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void createRequest(RequestForPrescription requestForPrescription) throws InvalidParameterException, NotFoundException {
        if(requestForPrescription==null){
            throw new InvalidParameterException("Parameter RequestforPrescription is invalid");
        }
        if(requestForPrescription.getClient()==null){
            throw new InvalidParameterException("Parameter Client is invalid");
        }
        if(requestForPrescription.getClient().getLogin()==null||requestForPrescription.getClient().getLogin().isEmpty()){
            throw new InvalidParameterException("Parameter Client is invalid");
        }
        if(requestForPrescription.getClient().getRegistrationType()==null){
            throw new InvalidParameterException("Parameter Registration type is invalid");
        }
        if(requestForPrescription.getDrug()==null||requestForPrescription.getDrug().getId()<=0){
            throw new InvalidParameterException("Parameter Drug is invalid");
        }
        if(requestForPrescription.getClientComment()==null||requestForPrescription.getClientComment().isEmpty()){
            throw new InvalidParameterException("Parameter Client Comment is invalid");
        }
        if(requestForPrescription.getProlongDate()==null){
            throw new InvalidParameterException("Parameter prolong date is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        RequestForPrescriptionDAO requestForPrescriptionDAO = daoFactory.getRequestForPrescriptionDAO();
        try {
            requestForPrescriptionDAO.insertRequest(requestForPrescription);
        } catch (EntityDeletedException e){
            throw new NotFoundException("Drug with id="+requestForPrescription.getDrug().getId()+" was not found or you can buy it without prescription or you allready have request in progress to this drug", e);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to insert request", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public List<RequestForPrescription> searchRequests(User user, RequestForPrescriptionCriteria criteria, int limit, int startFrom) throws InvalidParameterException {
        if(user==null||user.getLogin()==null||user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }
        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }
        if(user.getUserRole()==null||user.getUserRole()==UserRole.PHARMACIST){
            throw new InvalidParameterException("Invalid parameter user role. Only clients and doctors can search requests");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        RequestForPrescriptionDAO requestForPrescriptionDAO = daoFactory.getRequestForPrescriptionDAO();
        try {
            if(user.getUserRole()==UserRole.DOCTOR) {
                return requestForPrescriptionDAO.getDoctorsRequest(user, criteria, limit, startFrom);
            }
            else {
                return requestForPrescriptionDAO.getClientRequests(user, criteria, limit, startFrom);
            }
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to load requests from DB", e);
            throw new InternalServerException(e);
        }
    }
}
