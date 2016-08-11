package by.training.online_pharmacy.service.exception;

import by.training.online_pharmacy.dao.exception.EntityDeletedException;

/**
 * Created by vladislav on 11.08.16.
 */
public class NotFoundException extends ServiceException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, EntityDeletedException cause) {
        super(message, cause);
    }

    public NotFoundException(EntityDeletedException ex) {
        super(ex);
    }

    public boolean isCritical(){
        EntityDeletedException entityDeletedException = (EntityDeletedException)getCause();
        return entityDeletedException.isCritical();
    }
}
