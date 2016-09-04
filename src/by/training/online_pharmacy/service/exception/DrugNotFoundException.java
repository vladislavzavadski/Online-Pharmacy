package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 03.09.16.
 */
public class DrugNotFoundException extends ServiceException {
    public DrugNotFoundException(String message) {
        super(message);
    }

    public DrugNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

    public DrugNotFoundException(Exception ex) {
        super(ex);
    }
}
