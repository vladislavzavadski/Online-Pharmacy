package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 02.08.16.
 */
public class InvalidParameterException extends ServiceException {
    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(String message, Exception cause) {
        super(message, cause);
    }

    public InvalidParameterException(Exception ex) {
        super(ex);
    }
}
