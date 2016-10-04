package by.training.online_pharmacy.dao.exception;

/**
 * Created by vladislav on 02.10.16.
 */
public class MultipleRecordsException extends DaoException {

    public MultipleRecordsException(String message, Exception ex) {
        super(message, ex);
    }

    public MultipleRecordsException(String message) {
        super(message);
    }

    public MultipleRecordsException(Exception ex) {
        super(ex);
    }
}
