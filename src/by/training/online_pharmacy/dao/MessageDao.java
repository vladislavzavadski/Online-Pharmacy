package by.training.online_pharmacy.dao;

import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.message.Message;
import by.training.online_pharmacy.domain.message.SearchMessageCriteria;
import by.training.online_pharmacy.domain.user.User;

import java.util.List;

/**
 * Created by vladislav on 14.08.16.
 */
public interface MessageDao {
    void sendMessage(Message message) throws DaoException;

    List<Message> getMessages(User user, SearchMessageCriteria searchMessageCriteria, int startFrom, int limit) throws DaoException;

    void markMessageAsReaded(User user, int messageId) throws DaoException;

    int getNewMessagesCount(User user) throws DaoException;
}
