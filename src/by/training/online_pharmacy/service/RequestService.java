package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.prescription.Prescription;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.prescription.RequestForPrescriptionCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.*;

import java.util.List;

/**
 * Created by vladislav on 10.08.16.
 */
public interface RequestService {
    void createRequest(RequestForPrescription requestForPrescription) throws InvalidParameterException, NotFoundException;

    List<RequestForPrescription> searchRequests(User user, RequestForPrescriptionCriteria criteria, int limit, int startFrom) throws InvalidParameterException;

    void sendResponseForRequestForPrescription(User user, Prescription prescription, RequestForPrescription requestForPrescription) throws InvalidParameterException, InvalidUserStatusException, RequestForPrescriptionNotFoundException;

    int getRequestsCount(User user) throws InvalidParameterException, InternalServerException, InvalidUserStatusException;
}
