package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 23.07.16.
 */
public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Exception cause) {
        super(message, cause);
    }

    public InternalServerException(Exception ex) {
        super(ex);
    }
}
