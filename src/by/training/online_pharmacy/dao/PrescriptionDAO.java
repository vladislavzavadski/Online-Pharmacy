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
    void increaseDrugCountByOrder(User user, int orderId) throws DaoException;

    boolean isPrescriptionExist(User user, int drugId) throws DaoException;

    void reduceDrugCount(User user, int orderId) throws DaoException;

    void reduceDrugCount(User user, int drugId, int drugCount, int drugDosage) throws DaoException;

    List<Prescription> searchPrescriptions(User user, PrescriptionCriteria prescriptionCriteria, int limit, int startFrom) throws DaoException;

    List<Prescription> getUsersPrescriptions(String clientLogin, int limit, int startFrom) throws DaoException;
    List<Prescription> getPrescriptionsByDrugId(int drugId, int limit, int startFrom) throws DaoException;
    List<Prescription> getDoctorsPrescriptions(String doctorLogin, int limit, int startFrom) throws DaoException;
    Prescription getPrescriptionByPrimaryKey(String userLogin, int drugId) throws DaoException;
    List<Prescription> getPrescriptionsByAppointmentDate(Date date, Period period, int limit, int startFrom) throws DaoException;
    List<Prescription> getPrescriptionsByExpirationDate(Date date, Period period, int limit, int startFrom) throws DaoException;
    void insertPrescription(Prescription prescription) throws DaoException;
    void updatePrescription(Prescription prescription) throws DaoException;
    void deletePrescription(String clientLogin, int drugId) throws DaoException;
}
