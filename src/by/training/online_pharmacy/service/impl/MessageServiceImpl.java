package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.MessageDao;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityNotFoundException;
import by.training.online_pharmacy.domain.message.Message;
import by.training.online_pharmacy.domain.message.MessageStatus;
import by.training.online_pharmacy.domain.message.SearchMessageCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserRole;
import by.training.online_pharmacy.service.MessageService;
import by.training.online_pharmacy.service.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by vladislav on 11.08.16.
 */
public class MessageServiceImpl implements MessageService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void sendMessage(Message message) throws InternalServerException, InvalidParameterException {

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

        if(message.getSenderMessage()==null||message.getSenderMessage().isEmpty()||message.getSenderMessage().length()>1000){
            throw new InvalidParameterException("Parameter sender message is invalid");
        }

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        MessageDao messageDAO = daoFactory.getMessageDAO();

        try {
            messageDAO.insertMessage(message);

        }  catch (DaoException e) {
            logger.error("Something went wrong when trying to insert message");
            throw new InternalServerException(e);

        }

    }

    @Override
    public List<Message> getMessages(User user, SearchMessageCriteria searchMessageCriteria, int startFrom, int limit)
            throws InternalServerException, InvalidParameterException {

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
    public void markMessageAsReaded(User user, int messageId)
            throws InternalServerException, InvalidParameterException, MessageNotFoundException {

        if(user==null||user.getLogin()==null||user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(messageId<0){
            throw new InvalidParameterException("Message id should have positive value");
        }

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        MessageDao messageDao = daoFactory.getMessageDAO();

        try {
            messageDao.setMessageStatus(user, MessageStatus.COMPLETED, messageId);

        } catch (EntityNotFoundException e){
            throw new MessageNotFoundException("Message with id="+messageId+" was not found.");

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to mark message with id="+messageId, e);
            throw new InternalServerException(e);

        }
    }

    @Override
    public int getMessageCount(User user, MessageStatus messageStatus)
            throws InternalServerException, InvalidParameterException {

        if(user==null||user.getLogin()==null||user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(messageStatus==null){
            throw new InvalidParameterException("Parameter message status is invalid");
        }

        DaoFactory daoFactory  = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        MessageDao messageDao = daoFactory.getMessageDAO();

        try {
            if(user.getUserRole()==UserRole.CLIENT) {
                return messageDao.getSenderMessageCount(user, messageStatus);
            }
            else {
                return messageDao.getReceiverMessageCount(user,messageStatus);
            }

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get message count", e);
            throw new InternalServerException(e);

        }
    }

    @Override
    public void sendResponseToMessage(Message message) throws InternalServerException,
            InvalidParameterException, InvalidUserStatusException, MessageNotFoundException {

        if(message==null){
            throw new InvalidParameterException("Parameter message is invalid");
        }

        if(message.getId()<0){
            throw new InvalidParameterException("Parameter message id is invalid");
        }

        if(message.getReceiverMessage()==null||message.getReceiverMessage().isEmpty()||message.getReceiverMessage().length()>1000){
            throw new InvalidParameterException("Parameter receiver message is invalid");
        }

        if(message.getReceiver()==null){
            throw new InvalidParameterException("Parameter message receiver is invalid");
        }

        if(message.getReceiver().getLogin()==null||message.getReceiver().getLogin().isEmpty()){
            throw new InvalidParameterException("Parameter receiver login is invalid");
        }

        if(message.getReceiver().getRegistrationType()==null){
            throw new InvalidParameterException("Parameter receiver registration type is invalid");
        }

        if(message.getReceiver().getUserRole()!= UserRole.DOCTOR){
            throw new InvalidUserStatusException("Only doctors can send response to message");
        }

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        MessageDao messageDao = daoFactory.getMessageDAO();

        try {
            messageDao.updateMessage(message);

        }catch (EntityNotFoundException e) {
            logger.error("Something went wrong when trying to update message", e);
            throw new MessageNotFoundException(e);

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to update message", e);
            throw new InternalServerException(e);

        }
    }
}
