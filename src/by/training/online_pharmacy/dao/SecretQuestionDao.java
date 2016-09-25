package by.training.online_pharmacy.dao;

import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.SecretQuestion;
import by.training.online_pharmacy.domain.user.User;

import java.util.List;

/**
 * Created by vladislav on 04.09.16.
 */
public interface SecretQuestionDao {

    /**
     * Retrieve all questions from storage
     * @return List that contains secret questions
     * @throws DaoException if fail occurs while invoke read operation*/
    List<SecretQuestion> getAllQuestions() throws DaoException;

    /**
     * Retrieve secret questions for user
     * @return SecretQuestion that represent secret question
     * @param user object that represent secret question owner
     * @throws DaoException if fail occurs while invoke read operation*/
    SecretQuestion getUsersSecretQuestion(User user) throws DaoException;
}
