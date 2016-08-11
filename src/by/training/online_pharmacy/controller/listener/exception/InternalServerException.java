package by.training.online_pharmacy.controller.listener.exception;

/**
 * Created by vladislav on 05.08.16.
 */
public class InternalServerException extends RuntimeException {
    public InternalServerException() {
    }

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerException(Throwable cause) {
        super(cause);
    }

}
