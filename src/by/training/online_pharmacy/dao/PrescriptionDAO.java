package by.training.online_pharmacy.dao;


import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.Period;
import by.training.online_pharmacy.domain.prescription.Prescription;
import by.training.online_pharmacy.domain.prescription.PrescriptionCriteria;
import by.training.online_pharmacy.domain.user.User;

import java.util.Date;
import java.util.List;

/**
 * Created by vladislav on 13.06.16.
 */
public interface PrescriptionDAO {
    void createPrescription(Prescription prescription, int requestForPrescriptionId) throws DaoException;

    void increaseDrugCountByOrder(User user, int orderId) throws DaoException;

    Prescription getUserPrescription(User user, int drugId) throws DaoException;

    void reduceDrugCount(User user, int orderId) throws DaoException;

    void reduceDrugCount(User user, int drugId, int drugCount, int drugDosage) throws DaoException;

    List<Prescription> getUserPrescriptions(User user, PrescriptionCriteria prescriptionCriteria, int limit, int startFrom) throws DaoException;

    List<Prescription> getDoctorPrescriptions(User user, PrescriptionCriteria prescriptionCriteria, int limit, int startFrom) throws DaoException;

}
