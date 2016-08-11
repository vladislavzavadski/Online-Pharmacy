package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 11.08.16.
 */
public class AmbiguousValueException extends ServiceException {
    public AmbiguousValueException(String message) {
        super(message);
    }

    public AmbiguousValueException(String message, Exception cause) {
        super(message, cause);
    }

    public AmbiguousValueException(Exception ex) {
        super(ex);
    }
}
