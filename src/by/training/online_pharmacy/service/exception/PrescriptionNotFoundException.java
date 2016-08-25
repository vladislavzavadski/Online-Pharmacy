package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 11.08.16.
 */
public class PrescriptionNotFoundException extends ServiceException {
    public PrescriptionNotFoundException(String message) {
        super(message);
    }

    public PrescriptionNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

    public PrescriptionNotFoundException(Exception ex) {
        super(ex);
    }
}
