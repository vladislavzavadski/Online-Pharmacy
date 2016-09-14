package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 08.09.16.
 */
public class RequestForPrescriptionNotFoundException extends ServiceException {
    public RequestForPrescriptionNotFoundException(String message) {
        super(message);
    }

    public RequestForPrescriptionNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

    public RequestForPrescriptionNotFoundException(Exception ex) {
        super(ex);
    }
}
