package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.MessageDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.domain.message.Message;
import by.training.online_pharmacy.service.MessageService;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by vladislav on 11.08.16.
 */
public class MessageServiceImpl implements MessageService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void sendMessage(Message message) throws InvalidParameterException, NotFoundException {
        if(message==null){
            throw new InvalidParameterException("Parameter message is invalid");
        }
        if(message.getReceiver()==null||message.getReceiver().getLogin()==null||message.getReceiver().getLogin().isEmpty()||
                message.getReceiver().getRegistrationType()==null){
            throw new InvalidParameterException("Parameter receiver is invalid");
        }
        if(message.getSender()==null||message.getSender().getLogin()==null||message.getSender().getLogin().isEmpty()||
                message.getSender().getRegistrationType()==null){
            throw new InvalidParameterException("Parameter sender is invalid");
        }
        if(message.getSenderMessage()==null||message.getSenderMessage().isEmpty()){
            throw new InvalidParameterException("Parameter sender message is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        MessageDAO messageDAO = daoFactory.getMessageDAO();
        try {
            messageDAO.insertMessage(message);
        } catch (EntityDeletedException ex){
            throw new NotFoundException("Sender="+message.getSender()+" was not found", ex);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to insert message");
            throw new InternalServerException(e);
        }

    }
}
