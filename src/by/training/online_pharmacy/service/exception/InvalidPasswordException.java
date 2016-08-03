package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 01.08.16.
 */
public class InvalidPasswordException extends ServiceException {
    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Exception cause) {
        super(message, cause);
    }

    public InvalidPasswordException(Exception ex) {
        super(ex);
    }
}
