package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.UserDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserRole;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.CanceledAuthorizationException;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.UserNotFoundException;
import by.training.online_pharmacy.service.util.*;

import java.io.IOException;

/**
 * Created by vladislav on 18.07.16.
 */
public class UserServiceImpl implements UserService {
    @Override
    public User userLogin(String login, String password, RegistrationType registrationType) throws InternalServerException, UserNotFoundException {
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        User user = null;
        try {
            user = userDAO.userAuthentication(login, password, registrationType);
        } catch (DaoException e) {
            throw new InternalServerException(e);
        }
        if(user==null){
            throw new UserNotFoundException("Incorrect login or password");
        }
        return user;
    }

    @Override
    public User userLoginVk(String code) throws CanceledAuthorizationException, InternalServerException {
        if(code==null){
            throw new CanceledAuthorizationException("User cancel authorization");
        }
        VkApi vkApi = new VkApi();
        try {
            User user = getUserFromApi(vkApi, code);
            user = requestToDao(vkApi, user, RegistrationType.VK);
            return user;
        } catch (IOException | DaoException e) {
            throw new InternalServerException(e);
        }
    }

    @Override
    public User userLoginFb(String code) throws CanceledAuthorizationException, InternalServerException {
        if(code==null){
            throw new CanceledAuthorizationException("User cancel exception");
        }
        FacebookApi facebookApi = new FacebookApi();
        try {
            User user = getUserFromApi(facebookApi, code);
            user = requestToDao(facebookApi, user, RegistrationType.FACEBOOK);
            return user;
        } catch (IOException|DaoException e) {
            throw new InternalServerException(e);
        }
    }

    @Override
    public User userLoginLi(String code) throws CanceledAuthorizationException, InternalServerException {
        if(code==null){
            throw new CanceledAuthorizationException("User cancel exception");
        }
        LinkedInApi linkedInApi = new LinkedInApi();
        try {
            User user = getUserFromApi(linkedInApi, code);
            user = requestToDao(linkedInApi, user, RegistrationType.LINKEDIN);
            return user;
        } catch (IOException|DaoException e) {
            throw new InternalServerException(e);
        }
    }

    private User getUserFromApi(Api api, String code) throws IOException {
        User user = new User();
        api.authorization(code);
        user.setLogin(api.getId());
        user.setMail(api.getEmail());
        user.setFirstName(api.getFirstName());
        user.setSecondName(api.getSecondName());
        user.setPhone(api.getPhone());
        user.setUserRole(UserRole.CLIENT);
        return user;
    }

    private User requestToDao(Api api, User user, RegistrationType registrationType) throws IOException, DaoException {
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        user = userDAO.userAuthentication(user, registrationType);
        if(user.getUserImage()==null){
            ImageDownloader imageDownloader = new ImageDownloader(api.getImage());
            user.setUserImage(imageDownloader.download());
        }
        return user;
    }

    @Override
    public void userRegistration(User user) {

    }

}
