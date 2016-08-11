package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;

/**
 * Created by vladislav on 10.08.16.
 */
public interface RequestService {
    void createRequest(RequestForPrescription requestForPrescription) throws InvalidParameterException, NotFoundException;
}
