package by.training.online_pharmacy.dao.impl.database;

import by.training.online_pharmacy.dao.SecretQuestionDao;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.domain.user.SecretQuestion;
import by.training.online_pharmacy.domain.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladislav on 04.09.16.
 */
public class SecretQuestionDaoImpl implements SecretQuestionDao {
    private static final String GET_ALL_QUESTIONS_QUERY = "select sq_id, sq_question from secret_questions order by sq_question";
    private static final String GET_USERS_QUESTION_QUERY = "select sq_question from secret_questions inner join secret_word on sq_id=sw_question where sw_user_login=? and sw_login_via='native';";
    @Override
    public List<SecretQuestion> getAllQuestions() throws DaoException {
        List<SecretQuestion> secretQuestions = new ArrayList<>();
        try {
            DatabaseOperation databaseOperation = new DatabaseOperation(GET_ALL_QUESTIONS_QUERY);
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            while (resultSet.next()){
                SecretQuestion secretQuestion = new SecretQuestion();
                secretQuestion.setId(resultSet.getInt(TableColumn.QUESTION_ID));
                secretQuestion.setQuestion(resultSet.getString(TableColumn.QUESTION));
                secretQuestions.add(secretQuestion);
            }
            return secretQuestions;
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Can not load secret questions from database", e);
        }

    }

    @Override
    public SecretQuestion getUsersSecretQuestion(User user) throws DaoException {
        try {
            DatabaseOperation databaseOperation = new DatabaseOperation(GET_USERS_QUESTION_QUERY);
            databaseOperation.setParameter(1, user.getLogin());
            ResultSet resultSet = databaseOperation.invokeReadOperation();
            if(resultSet.next()){
                SecretQuestion secretQuestion = new SecretQuestion();
                secretQuestion.setQuestion(resultSet.getString(TableColumn.QUESTION));
                return secretQuestion;
            }
            return null;
        } catch (ConnectionPoolException | SQLException e) {
            throw new DaoException("Can not load user's secret question from database");
        }
    }
}
