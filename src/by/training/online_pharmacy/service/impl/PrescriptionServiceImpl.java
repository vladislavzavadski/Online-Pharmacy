package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.PrescriptionDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.prescription.*;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserRole;
import by.training.online_pharmacy.service.PrescriptionService;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.InvalidUserStatusException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

/**
 * Created by vladislav on 27.08.16.
 */
public class PrescriptionServiceImpl implements PrescriptionService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public List<Prescription> searchPrescriptions(User user, String drugName, String prescriptionStatus, int limit, int startFrom)
            throws InternalServerException, InvalidParameterException, InvalidUserStatusException {

        if(user==null||user.getLogin()==null||user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }

        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }

        if(user.getUserRole()!= UserRole.DOCTOR&&user.getUserRole()!=UserRole.CLIENT){
            throw new InvalidUserStatusException("Only doctor and client can get prescriptions");
        }

        PrescriptionCriteria prescriptionCriteria = new PrescriptionCriteria();
        prescriptionCriteria.setDrugName(drugName);

        if(prescriptionStatus!=null&&!prescriptionStatus.isEmpty()){
            prescriptionCriteria.setPrescriptionStatus(PrescriptionStatus.valueOf(prescriptionStatus));
        }

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        PrescriptionDAO prescriptionDAO = daoFactory.getPrescriptionDAO();

        try {

            if(user.getUserRole()==UserRole.CLIENT) {
                return prescriptionDAO.getUserPrescriptions(user, prescriptionCriteria, limit, startFrom);
            }
            else if(user.getUserRole()==UserRole.DOCTOR){
                return prescriptionDAO.getDoctorPrescriptions(user, prescriptionCriteria, limit, startFrom);
            }

            return Collections.emptyList();

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to search prescriptions", e);
            throw new InternalServerException(e);

        }

    }

    @Override
    public Prescription getActivePrescription(User user, int drugId) throws InternalServerException, InvalidParameterException {

        if(user==null||user.getLogin()==null||user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(drugId<0){
            throw new InvalidParameterException("Parameter drug id is invalid. It should have non-negative value");
        }

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        PrescriptionDAO prescriptionDAO = daoFactory.getPrescriptionDAO();

        try {
            return prescriptionDAO.getUserPrescription(user, drugId);

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to check", e);
            throw new InternalServerException(e);

        }
    }
}
