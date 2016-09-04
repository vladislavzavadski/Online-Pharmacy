package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.MessageDao;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.exception.EntityNotFoundException;
import by.training.online_pharmacy.domain.message.Message;
import by.training.online_pharmacy.domain.message.SearchMessageCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.MessageService;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.MessageNotFoundException;
import by.training.online_pharmacy.service.exception.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

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
        MessageDao messageDAO = daoFactory.getMessageDAO();
        try {
            messageDAO.sendMessage(message);
        } catch (EntityDeletedException ex){
            throw new NotFoundException("Sender="+message.getSender()+" was not found", ex);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to insert message");
            throw new InternalServerException(e);
        }

    }

    @Override
    public List<Message> getMessages(User user, SearchMessageCriteria searchMessageCriteria, int startFrom, int limit) throws InvalidParameterException {
        if(user==null||user.getLogin()==null||user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }
        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }
        if(searchMessageCriteria==null){
            throw new InvalidParameterException("Invalid parameter searchMessageCriteria");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        MessageDao messageDao = daoFactory.getMessageDAO();
        try {
            List<Message> messages = messageDao.getMessages(user, searchMessageCriteria, startFrom, limit);
            return messages;
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get messages", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void markMessageAsReaded(User user, int messageId) throws InvalidParameterException, MessageNotFoundException {
        if(user==null||user.getLogin()==null||user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(messageId<0){
            throw new InvalidParameterException("Message id should have positive value");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        MessageDao messageDao = daoFactory.getMessageDAO();
        try {
            messageDao.markMessageAsReaded(user, messageId);
        } catch (EntityNotFoundException e){
            throw new MessageNotFoundException("Message with id="+messageId+" was not found.");
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to mark message with id="+messageId, e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public int getNewMessageCount(User user) throws InvalidParameterException {
        if(user==null||user.getLogin()==null||user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        DaoFactory daoFactory  = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        MessageDao messageDao = daoFactory.getMessageDAO();
        try {
            return messageDao.getNewMessagesCount(user);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get message count", e);
            throw new InternalServerException(e);
        }
    }
}
