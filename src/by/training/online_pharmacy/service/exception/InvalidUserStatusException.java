package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 31.08.16.
 */
public class InvalidUserStatusException extends ServiceException {
    public InvalidUserStatusException(String message) {
        super(message);
    }

    public InvalidUserStatusException(String message, Exception cause) {
        super(message, cause);
    }

    public InvalidUserStatusException(Exception ex) {
        super(ex);
    }
}
