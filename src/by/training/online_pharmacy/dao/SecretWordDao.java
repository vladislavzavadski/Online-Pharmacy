package by.training.online_pharmacy.dao;

import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.SecretWord;
import by.training.online_pharmacy.domain.user.User;

/**
 * Created by vladislav on 04.09.16.
 */
public interface SecretWordDao {
    void createSecretWord(SecretWord secretWord) throws DaoException;
    SecretWord getUsersSecret(User user);
}
