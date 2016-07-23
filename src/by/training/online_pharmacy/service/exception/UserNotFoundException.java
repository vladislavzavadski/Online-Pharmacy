package by.training.online_pharmacy.service.exception;



/**
 * Created by vladislav on 23.07.16.
 */
public class UserNotFoundException extends ServiceException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

    public UserNotFoundException(Exception ex) {
        super(ex);
    }
}
