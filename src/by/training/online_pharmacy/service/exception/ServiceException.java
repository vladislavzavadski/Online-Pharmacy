package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 18.07.16.
 */
public class ServiceException extends Exception {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Exception cause) {
        super(message, cause);
    }

    public ServiceException(Exception ex){super(ex);}
}
