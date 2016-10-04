package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.PrescriptionDAO;
import by.training.online_pharmacy.dao.RequestForPrescriptionDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.exception.EntityNotFoundException;
import by.training.online_pharmacy.domain.prescription.Prescription;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.prescription.RequestForPrescriptionCriteria;
import by.training.online_pharmacy.domain.prescription.RequestStatus;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserRole;
import by.training.online_pharmacy.service.RequestService;
import by.training.online_pharmacy.service.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by vladislav on 10.08.16.
 */
public class RequestServiceImpl implements RequestService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void createRequest(RequestForPrescription requestForPrescription)
            throws InternalServerException, InvalidParameterException, NotFoundException {

        if(requestForPrescription==null){
            throw new InvalidParameterException("Parameter Request for Prescription is invalid");
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

        if(requestForPrescription.getClientComment()==null||requestForPrescription.getClientComment().isEmpty()
                ||requestForPrescription.getClientComment().length()>400){
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
            throw new NotFoundException("Drug with id="+requestForPrescription.getDrug().getId()+" was not found or you" +
                    " can buy it without prescription or you allready have request in progress to this drug", e);

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to insert request", e);
            throw new InternalServerException(e);

        }
    }

    @Override
    public List<RequestForPrescription> searchRequests(User user, RequestForPrescriptionCriteria criteria, int limit, int startFrom)
            throws InternalServerException, InvalidParameterException {

        if(user==null||user.getLogin()==null||user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }

        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }

        if(user.getUserRole()==null||(user.getUserRole()!=UserRole.DOCTOR&&user.getUserRole()!=UserRole.CLIENT)){
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

    @Override
    public void sendResponseForRequestForPrescription(User user, Prescription prescription, RequestForPrescription requestForPrescription)
            throws InternalServerException, InvalidParameterException, InvalidUserStatusException, RequestForPrescriptionNotFoundException {

        if(requestForPrescription==null){
            throw new InvalidParameterException("Parameter request for prescription is invalid");
        }

        if(requestForPrescription.getId()<0){
            throw new InvalidParameterException("Parameter request id is invalid. It must have non-negative value");
        }

        if(requestForPrescription.getRequestStatus()==null||requestForPrescription.getRequestStatus()== RequestStatus.IN_PROGRESS){
            throw new InvalidParameterException("Parameter request status is invalid");
        }

        if(requestForPrescription.getDoctorComment()==null||requestForPrescription.getDoctorComment().isEmpty()
                ||requestForPrescription.getDoctorComment().length()>400){
            throw new InvalidParameterException("Parameter doctor comment is empty");
        }

        if(user==null||user.getLogin()==null||user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(requestForPrescription.getRequestStatus()==RequestStatus.CONFIRMED){

            if(prescription==null){
                throw new InvalidParameterException("Parameter prescription is invalid");
            }

            if(prescription.getExpirationDate()==null||prescription.getExpirationDate().compareTo(new Date())<0){
                throw new InvalidParameterException("Parameter expiration date is invalid");
            }

            if(prescription.getDrugCount()<=0||prescription.getDrugCount()>10){
                throw new InvalidParameterException("Parameter drug count is invalid. It must be greater than 0");
            }

            if(prescription.getDrugDosage()<=0){
                throw new InvalidParameterException("Parameter drug dosage is invalid. It must be greater than 0");
            }

        }

        if(user.getUserRole()!= UserRole.DOCTOR){
            throw new InvalidUserStatusException("Only doctors can send response to request for prescription");
        }

        requestForPrescription.setDoctor(user);

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        RequestForPrescriptionDAO requestForPrescriptionDAO = daoFactory.getRequestForPrescriptionDAO();

        try {
            requestForPrescriptionDAO.updateRequestForPrescription(requestForPrescription);

            if(requestForPrescription.getRequestStatus()==RequestStatus.CONFIRMED){
                PrescriptionDAO prescriptionDAO = daoFactory.getPrescriptionDAO();
                prescription.setDoctor(user);
                prescriptionDAO.createPrescription(prescription, requestForPrescription.getId());

            }

        } catch (EntityNotFoundException e) {
            logger.error("Request for prescription was not found", e);
            throw new RequestForPrescriptionNotFoundException(e);

        } catch(DaoException e) {
            logger.error("Something went wrong when trying to create prescription/change request status", e);
            throw new InternalServerException(e);

        }

    }

    @Override
    public int getRequestsCount(User user)
            throws InvalidParameterException, InternalServerException, InvalidUserStatusException {

        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(user.getLogin()==null||user.getLogin().isEmpty()){
            throw new InvalidParameterException("Parameter user login is invalid");
        }

        if(user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter registration type is invalid");
        }

        if(user.getUserRole()!=UserRole.DOCTOR){
            throw new InvalidUserStatusException("Only doctors can get request count");
        }

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        RequestForPrescriptionDAO requestForPrescriptionDAO = daoFactory.getRequestForPrescriptionDAO();

        try {
            return requestForPrescriptionDAO.getRequestsCount(user, RequestStatus.IN_PROGRESS);

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get requests count", e);
            throw new InternalServerException(e);

        }
    }
}
