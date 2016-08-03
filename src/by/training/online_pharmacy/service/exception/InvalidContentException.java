package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 01.08.16.
 */
public class InvalidContentException extends ServiceException {
    public InvalidContentException(String message) {
        super(message);
    }

    public InvalidContentException(String message, Exception cause) {
        super(message, cause);
    }

    public InvalidContentException(Exception ex) {
        super(ex);
    }
}
