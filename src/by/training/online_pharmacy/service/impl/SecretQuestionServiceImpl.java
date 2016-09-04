package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.SecretQuestionDao;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.impl.database.util.DatabaseOperation;
import by.training.online_pharmacy.domain.user.SecretQuestion;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.SecretQuestionService;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by vladislav on 04.09.16.
 */
public class SecretQuestionServiceImpl implements SecretQuestionService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public List<SecretQuestion> getAllSecretQuestions(){
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        SecretQuestionDao secretQuestionDao = daoFactory.getSecretQuestionDao();
        try {
            return secretQuestionDao.getAllQuestions();
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get all secret questions", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public SecretQuestion getUsersSecretQuestion(User user) throws InvalidParameterException {
        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user.getLogin()==null||user.getLogin().isEmpty()){
            throw new InvalidParameterException("Parameter user login is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        SecretQuestionDao secretQuestionDao = daoFactory.getSecretQuestionDao();
        try {
            return secretQuestionDao.getUsersSecretQuestion(user);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get users secret question");
            throw new InternalServerException(e);
        }
    }
}
