package by.training.online_pharmacy.service.exception;

/**
 * Created by vladislav on 22.08.16.
 */
public class MessageNotFoundException extends ServiceException {
    public MessageNotFoundException(String message) {
        super(message);
    }
    public MessageNotFoundException(Exception e){
        super(e);
    }
}
