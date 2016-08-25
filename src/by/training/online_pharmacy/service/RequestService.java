package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.prescription.RequestForPrescriptionCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;

import java.util.List;

/**
 * Created by vladislav on 10.08.16.
 */
public interface RequestService {
    void createRequest(RequestForPrescription requestForPrescription) throws InvalidParameterException, NotFoundException;

    List<RequestForPrescription> searchRequests(User user, RequestForPrescriptionCriteria criteria, int limit, int startFrom) throws InvalidParameterException;
}
