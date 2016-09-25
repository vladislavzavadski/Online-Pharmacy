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

    /**
     * Insert new prescription to database, if prescription already exist
     * existing prescription will be updated
     * @param prescription object that represent new prescription
     * @param requestForPrescriptionId request for prescription identification
     * @throws DaoException if fail occurs while invoke write operation*/
    void createPrescription(Prescription prescription, int requestForPrescriptionId) throws DaoException;

    /**
     * Increase drug count in prescription
     * @param user object that represent owner of prescription
     * @param orderId order identification
     * @throws DaoException if fail occurs while invoke write operation*/
    void increaseDrugCountByOrder(User user, int orderId) throws DaoException;

    /**
     * Retrieve prescription from storage
     * @param user object that represent prescription's owner
     * @param drugId drug identification
     * @return Prescription that represent user prescription and return <code>null</code> if prescription not found
     * @throws DaoException if fail occurs while invoke read operation*/
    Prescription getUserPrescription(User user, int drugId) throws DaoException;

    /**
     * Reduce drug count by order identification
     * @param user object that represent prescription owner
     * @param orderId order identification
     * @throws DaoException if fail occurs while invoke write operation*/
    void reduceDrugCount(User user, int orderId) throws DaoException;

    /**
     * Reduce drug count
     * @param user object that represent owner of prescription
     * @param drugId  drug identification
     * @param drugCount drug count
     * @param drugDosage drug dosage
     * @throws DaoException if fail occurs while invoke write operation*/
    void reduceDrugCount(User user, int drugId, int drugCount, int drugDosage) throws DaoException;

    /**
     * Retrieve prescriptions from storage by criteria
     * @param user object that represent prescription owner
     * @param prescriptionCriteria object that represent prescription criteria
     * @param limit prescriptions count that will retrieved from storage
     * @param startFrom number of first entity
     * @return List that contains prescriptions
     * @throws DaoException if fail occurs while invoke read operation*/
    List<Prescription> getUserPrescriptions(User user, PrescriptionCriteria prescriptionCriteria, int limit, int startFrom) throws DaoException;

    /**
     * Retrieve prescriptions from storage by criteria
     * @param user object that represent prescription doctor
     * @param prescriptionCriteria object that represent prescription criteria
     * @param limit prescriptions count that will retrieved from storage
     * @param startFrom number of first entity
     * @return List that contains prescriptions
     * @throws DaoException if fail occurs while invoke read operation*/
    List<Prescription> getDoctorPrescriptions(User user, PrescriptionCriteria prescriptionCriteria, int limit, int startFrom) throws DaoException;

}
