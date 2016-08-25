package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 17.08.16.
 */
public class OrderNotFoundException extends ServiceException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

    public OrderNotFoundException(Exception ex) {
        super(ex);
    }
}
