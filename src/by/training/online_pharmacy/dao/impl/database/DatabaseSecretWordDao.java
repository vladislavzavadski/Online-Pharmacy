package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.SecretWordDao;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.domain.user.SecretWord;
import by.training.online_pharmacy.domain.user.User;

import java.sql.SQLException;

/**
 * Created by vladislav on 04.09.16.
 */
public class DatabaseSecretWordDao implements SecretWordDao {

    private static final String CREATE_SECRET_QUERY = "insert into secret_word (sw_user_login, sw_login_via, sw_question, sw_response) values (?,?,?,md5(?)) on duplicate key update sw_question=values(sw_question), sw_response=values(sw_response);";

    @Override
    public void createSecretWord(SecretWord secretWord) throws DaoException {

        try (DatabaseOperation databaseOperation = new DatabaseOperation(CREATE_SECRET_QUERY);){
            databaseOperation.setParameter(1, secretWord.getUser().getLogin());
            databaseOperation.setParameter(2, secretWord.getUser().getRegistrationType().toString().toLowerCase());
            databaseOperation.setParameter(3, secretWord.getSecretQuestion().getId());
            databaseOperation.setParameter(4, secretWord.getResponse());

            databaseOperation.invokeWriteOperation();
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Can not create new secret word", e);
        }

    }

}
