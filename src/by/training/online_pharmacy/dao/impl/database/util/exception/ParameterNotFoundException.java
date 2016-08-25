package by.training.online_pharmacy.dao.impl.database.util.exception;


/**
 * Created by vladislav on 06.08.16.
 */
public class ParameterNotFoundException extends Exception {

    public ParameterNotFoundException(String message) {
        super(message);
    }

    public ParameterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterNotFoundException(Throwable cause) {
        super(cause);
    }

    public ParameterNotFoundException() {
    }
}
