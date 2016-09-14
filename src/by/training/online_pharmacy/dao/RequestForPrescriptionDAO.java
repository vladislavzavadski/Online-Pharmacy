package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.prescription.RequestForPrescriptionCriteria;
import by.training.online_pharmacy.domain.user.User;

import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public interface RequestForPrescriptionDAO {

    int getInProgressRequestsCount(User user) throws DaoException;

    void insertRequest(RequestForPrescription requestForPrescription) throws DaoException;

    void updateRequestForPrescription(RequestForPrescription requestForPrescription) throws DaoException;

    List<RequestForPrescription> getDoctorsRequest(User user, RequestForPrescriptionCriteria criteria, int limit, int startFrom) throws DaoException;

    List<RequestForPrescription> getClientRequests(User user, RequestForPrescriptionCriteria criteria, int limit, int startFrom) throws DaoException;
}
