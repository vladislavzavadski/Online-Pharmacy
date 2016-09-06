package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.Period;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.prescription.RequestForPrescriptionCriteria;
import by.training.online_pharmacy.domain.prescription.RequestStatus;
import by.training.online_pharmacy.domain.user.User;

import java.util.Date;
import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public interface RequestForPrescriptionDAO {
    List<RequestForPrescription> getRequestsForPrescriptions(User user, RequestForPrescriptionCriteria criteria, int limit, int startFrom) throws DaoException;
    void insertRequest(RequestForPrescription requestForPrescription) throws DaoException;
}
