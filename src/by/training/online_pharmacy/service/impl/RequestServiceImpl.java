package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.RequestForPrescriptionDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.service.RequestService;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
            throw new NotFoundException("Drug with id="+requestForPrescription.getDrug().getId()+" was not found", e);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to insert request", e);
            throw new InternalServerException(e);
        }

    }
}
