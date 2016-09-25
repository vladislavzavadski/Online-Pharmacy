package by.training.online_pharmacy.dao;

import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.message.Message;
import by.training.online_pharmacy.domain.message.MessageStatus;
import by.training.online_pharmacy.domain.message.SearchMessageCriteria;
import by.training.online_pharmacy.domain.user.User;

import java.util.List;

/**
 * Created by vladislav on 14.08.16.
 */
public interface MessageDao {
    /**
     * Insert new message into storage
     * @param message represent new message
     * @throws DaoException if fail occurs while invoke write operation*/
    void insertMessage(Message message) throws DaoException;

    /**
     * Retrieve messages from storage for specific user.
     * @param user object that represent user. For this user messages will be retrieved
     * @param searchMessageCriteria object that represent criteria for retrieved messages
     * @param startFrom from this number orders will be retrieved from storage
     * @param limit orders count that will be retrieved from storage
     * @return List that contains messages
     * @throws DaoException if fail occurs while invoke read operation*/
    List<Message> getMessages(User user, SearchMessageCriteria searchMessageCriteria, int startFrom, int limit) throws DaoException;

    /**
     * Change message status
     * @param user object represent user, that send message
     * @param messageStatus new status of the message
     * @param messageId message identification
     * @throws DaoException if fail occurs while invoke write operation*/
    void setMessageStatus(User user, MessageStatus messageStatus, int messageId) throws DaoException;

    /**
     * Update message. For example when another user send message response
     * @param message object that represent
     * @throws DaoException if fail occurs while invoke write operation*/
    void updateMessage(Message message) throws DaoException;

    /**
     * Retrieve message count from storage for message sender
     * @param user object that represent user. For this user message will be retrieved
     * @param messageStatus message count will retrieved only for this status
     * @return int messages count
     * @throws DaoException if fail occurs while invoke read operation*/
    int getSenderMessageCount(User user, MessageStatus messageStatus) throws DaoException;

    /**
     * Retrieve message count from storage for message receiver
     * @param user object that represent user. For this user message will be retrieved
     * @param messageStatus message count will retrieved only for this status
     * @return int messages count
     * @throws DaoException if fail occurs while invoke read operation*/
    int getReceiverMessageCount(User user, MessageStatus messageStatus) throws DaoException;
}
