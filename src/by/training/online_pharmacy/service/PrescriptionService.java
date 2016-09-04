package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.prescription.Prescription;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.InvalidParameterException;

import java.util.List;

/**
 * Created by vladislav on 11.08.16.
 */
public interface PrescriptionService {

    List<Prescription> searchPrescriptions(User user, String drugName, String prescriptionStatus, int limit, int startFrom) throws InvalidParameterException;

    boolean isPrescriptionExist(User user, int drugId) throws InvalidParameterException;
}
