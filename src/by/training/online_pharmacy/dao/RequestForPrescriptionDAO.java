package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.prescription.RequestForPrescriptionCriteria;
import by.training.online_pharmacy.domain.prescription.RequestStatus;
import by.training.online_pharmacy.domain.user.User;

import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public interface RequestForPrescriptionDAO {

    /**
     * Retrieve requests count from storage
     * @param user object that represent doctor
     * @param requestStatus request status
     * @return int that contains requests count
     * @throws DaoException if fail occurs while invoke read operation*/
    int getRequestsCount(User user, RequestStatus requestStatus) throws DaoException;

    /**
     * Insert new request into storage
     * @param requestForPrescription object that represent new request for prescriptions
     * @throws DaoException if fail occurs while invoke write operation*/
    void insertRequest(RequestForPrescription requestForPrescription) throws DaoException;

    /**
     * Update request for prescriptions in storage
     * @param requestForPrescription object that represent updated request for prescription
     * @throws DaoException if fail occurs while invoke write operation*/
    void updateRequestForPrescription(RequestForPrescription requestForPrescription) throws DaoException;

    /**
     * Retrieve requests from storage
     * @param user object that represent request client
     * @param criteria object that represent request criteria
     * @param limit request's count that will be retrieved from storage
     * @param startFrom from this number entities will retrieved
     * @return List that contains retrieved requests*/
    List<RequestForPrescription> getDoctorsRequest(User user, RequestForPrescriptionCriteria criteria, int limit, int startFrom) throws DaoException;

    /**
     * Retrieve requests from storage
     * @param user object that represent request doctor
     * @param criteria object that represent request criteria
     * @param limit request's count that will be retrieved from storage
     * @param startFrom from this number entities will retrieved
     * @return List that contains retrieved requests*/
    List<RequestForPrescription> getClientRequests(User user, RequestForPrescriptionCriteria criteria, int limit, int startFrom) throws DaoException;
}
