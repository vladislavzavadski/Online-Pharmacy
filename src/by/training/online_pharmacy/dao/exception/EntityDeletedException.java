package by.training.online_pharmacy.dao.exception;

/**
 * Created by vladislav on 11.08.16.
 */
public class EntityDeletedException extends DaoException {
    private boolean isCritical;

    public EntityDeletedException(String message, boolean isCritical, Exception ex) {

        super(message, ex);
        this.isCritical = isCritical;
    }

    public EntityDeletedException(String message) {
        super(message);
    }

    public EntityDeletedException(Exception ex) {
        super(ex);
    }

    public boolean isCritical(){
        return isCritical;
    }
}
