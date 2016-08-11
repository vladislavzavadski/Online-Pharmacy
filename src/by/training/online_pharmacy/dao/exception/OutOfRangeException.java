package by.training.online_pharmacy.dao.exception;

/**
 * Created by vladislav on 11.08.16.
 */
public class OutOfRangeException extends DaoException {
    public OutOfRangeException(String message, Exception ex) {
        super(message, ex);
    }

    public OutOfRangeException(String message) {
        super(message);
    }

    public OutOfRangeException(Exception ex) {
        super(ex);
    }
}
