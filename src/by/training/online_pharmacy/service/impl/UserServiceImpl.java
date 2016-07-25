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

/**
 * Created by vladislav on 18.07.16.
 */
public class UserServiceImpl implements UserService {
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
            throw new InternalServerException(e);
        }
        return user;
    }
    @Override
    public void userRegistration(User user) {

    }

}
