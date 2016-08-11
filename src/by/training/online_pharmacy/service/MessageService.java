package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.message.Message;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;

/**
 * Created by vladislav on 11.08.16.
 */
public interface MessageService {
    void sendMessage(Message message) throws InvalidParameterException, NotFoundException;
}
