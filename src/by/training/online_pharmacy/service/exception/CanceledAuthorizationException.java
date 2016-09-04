package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 23.07.16.
 */
public class CanceledAuthorizationException extends ServiceException {
    public CanceledAuthorizationException(String message) {
        super(message);
    }

    public CanceledAuthorizationException(String message, Exception cause) {
        super(message, cause);
    }

    public CanceledAuthorizationException(Exception ex) {
        super(ex);
    }

}
