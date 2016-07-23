package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.Period;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.prescription.RequestStatus;

import java.util.Date;
import java.util.List;

/**
 * Created by vladislav on 19.06.16.
 */
public interface RequestForPrescriptionDAO {
    List<RequestForPrescription> getRequestsByClient(String clientLogin, int limit, int startFrom) throws DaoException;
    List<RequestForPrescription> getRequestsByDrugId(int drugId, int limit, int startFrom) throws DaoException;
    List<RequestForPrescription> getRequestsByDoctor(String doctorLogin, int limit, int startFrom) throws DaoException;
    List<RequestForPrescription> getRequestsByStatus(RequestStatus requestStatus, int limit, int startFrom) throws DaoException;
    List<RequestForPrescription> getRequestsByDate(Date requestDate, Period period, int limit, int startFrom) throws DaoException;
    RequestForPrescription getRequestById(int requestId) throws DaoException;
    void insertRequest(RequestForPrescription requestForPrescription) throws DaoException;
    void updateRequest(RequestForPrescription requestForPrescription) throws DaoException;
    void deleteRequest(int requestId) throws DaoException;
}
