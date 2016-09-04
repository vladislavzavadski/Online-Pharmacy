package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.user.SecretQuestion;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.InvalidParameterException;

import java.util.List;

/**
 * Created by vladislav on 04.09.16.
 */
public interface SecretQuestionService {
    List<SecretQuestion> getAllSecretQuestions();

    SecretQuestion getUsersSecretQuestion(User user) throws InvalidParameterException;
}
