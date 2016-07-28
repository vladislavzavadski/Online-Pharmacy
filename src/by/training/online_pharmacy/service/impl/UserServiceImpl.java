package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.UserDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.UserNotFoundException;
import by.training.online_pharmacy.service.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by vladislav on 18.07.16.
 */
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getRootLogger();
    @Override
    public User userLogin(String login, String password, RegistrationType registrationType) throws InternalServerException, UserNotFoundException {
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        User user;
        try {
            user = userDAO.userAuthentication(login, password, registrationType);
            if(user==null){
                throw new UserNotFoundException("Incorrect login or password");
            }
            if(user.getUserImage()==null){
                user.setPathToAlternativeImage(ImageConstants.PHARMACY_DEFAULT_IMAGE);
            }
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to login as native user", e);
            throw new InternalServerException(e);
        }
        return user;
    }
    @Override
    public void userRegistration(User user) {
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        try {
            userDAO.insertUser(user);
            EmailSender emailSender = new EmailSender(EmailProperties.EMAIL, EmailProperties.PASSWORD);
            emailSender.send(EmailProperties.TITLE, String.format(EmailProperties.MESSAGE_BODY, user.getFirstName(), user.getLogin(), user.getPassword()), user.getMail());
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to register as native user", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public boolean isUserExist(String login) {
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        try {
            return userDAO.isLoginUsed(login);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to check user login", e);
            throw new InternalServerException(e);
        }
    }

}
