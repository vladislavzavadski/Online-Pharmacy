package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 31.08.16.
 */
public class SpecializationNotFoundException extends ServiceException {
    public SpecializationNotFoundException(String message) {
        super(message);
    }

    public SpecializationNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

    public SpecializationNotFoundException(Exception ex) {
        super(ex);
    }
}
