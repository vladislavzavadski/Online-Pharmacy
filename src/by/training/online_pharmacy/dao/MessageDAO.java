package by.training.online_pharmacy.dao;

import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.message.Message;

/**
 * Created by vladislav on 11.08.16.
 */
public interface MessageDAO {
    void insertMessage(Message message) throws DaoException;
}
