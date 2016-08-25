package by.training.online_pharmacy.dao.exception;


/**
 * Created by vladislav on 11.08.16.
 */
public class EntityNotFoundException extends DaoException {
    public EntityNotFoundException(String message, Exception ex) {
        super(message, ex);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Exception ex) {
        super(ex);
    }
}
