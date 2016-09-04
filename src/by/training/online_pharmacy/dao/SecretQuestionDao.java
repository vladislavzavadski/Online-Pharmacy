package by.training.online_pharmacy.dao;

import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.SecretQuestion;
import by.training.online_pharmacy.domain.user.User;

import java.util.List;

/**
 * Created by vladislav on 04.09.16.
 */
public interface SecretQuestionDao {
    List<SecretQuestion> getAllQuestions() throws DaoException;

    SecretQuestion getUsersSecretQuestion(User user) throws DaoException;
}
