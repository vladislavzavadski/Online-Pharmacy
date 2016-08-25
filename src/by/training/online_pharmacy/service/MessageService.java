package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.message.Message;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.MessageNotFoundException;
import by.training.online_pharmacy.service.exception.NotFoundException;

import java.util.List;

/**
 * Created by vladislav on 11.08.16.
 */
public interface MessageService {
    void sendMessage(Message message) throws InvalidParameterException, NotFoundException;

    List<Message> getMessages(User user, String messageStatus, String dateTo, String dateFrom, int limit, int startFrom) throws InvalidParameterException;

    void markMessageAsReaded(User user, int messageId) throws InvalidParameterException, MessageNotFoundException;

    int getNewMessageCount(User user) throws InvalidParameterException;
}
