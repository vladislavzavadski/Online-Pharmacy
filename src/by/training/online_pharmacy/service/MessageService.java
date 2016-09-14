package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.message.Message;
import by.training.online_pharmacy.domain.message.MessageStatus;
import by.training.online_pharmacy.domain.message.SearchMessageCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.InvalidUserStatusException;
import by.training.online_pharmacy.service.exception.MessageNotFoundException;
import by.training.online_pharmacy.service.exception.NotFoundException;

import java.util.List;

/**
 * Created by vladislav on 11.08.16.
 */
public interface MessageService {
    void sendMessage(Message message) throws InvalidParameterException;

    List<Message> getMessages(User user, SearchMessageCriteria searchMessageCriteria, int startFrom, int limit) throws InvalidParameterException;

    void markMessageAsReaded(User user, int messageId) throws InvalidParameterException, MessageNotFoundException;

    int getMessageCount(User user, MessageStatus messageStatus) throws InvalidParameterException;

    void sendResponseToMessage(Message message) throws InvalidParameterException, InvalidUserStatusException, MessageNotFoundException;
}
