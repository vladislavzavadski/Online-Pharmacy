package by.training.online_pharmacy.dao;

import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.SecretWord;
import by.training.online_pharmacy.domain.user.User;

/**
 * Created by vladislav on 04.09.16.
 */
public interface SecretWordDao {

    /**
     * Insert secret word into storage
     * @param secretWord object that represent new secret word
     * @throws DaoException if fail occurs while invoke write operation*/
    void createSecretWord(SecretWord secretWord) throws DaoException;

}
