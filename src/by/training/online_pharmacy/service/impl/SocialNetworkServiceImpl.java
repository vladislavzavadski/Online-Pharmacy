package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.UserDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserRole;
import by.training.online_pharmacy.service.SocialNetworkService;
import by.training.online_pharmacy.service.exception.CanceledAuthorizationException;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.util.*;

import java.io.IOException;

/**
 * Created by vladislav on 25.07.16.
 */
public class SocialNetworkServiceImpl implements SocialNetworkService {
    @Override
    public User userLoginVk(String code) throws CanceledAuthorizationException, InternalServerException {
        if(code==null){
            throw new CanceledAuthorizationException("User cancel authorization");
        }
        VkApi vkApi = new VkApi();
        try {
            User user = getUserFromApi(vkApi, code);
            user.setRegistrationType(RegistrationType.VK);
            user = requestToDao(vkApi, user);
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
            user.setRegistrationType(RegistrationType.FACEBOOK);
            user = requestToDao(facebookApi, user);
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
            user.setRegistrationType(RegistrationType.LINKEDIN);
            user = requestToDao(linkedInApi, user);
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
        user.setGender(api.getGender());
        return user;
    }

    private User requestToDao(Api api, User user) throws IOException, DaoException {
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        user = userDAO.userAuthentication(user);
        if(user.getUserImage()==null){
            String pathToAlternativeImage = api.getImage();
            if(pathToAlternativeImage==null||
                    pathToAlternativeImage.contains(ImageConstants.VK_DEFAULT_IMAGE)||
                    pathToAlternativeImage.contains(ImageConstants.FACEBOOK_DEFAULT_IMAGE)) {
                user.setPathToAlternativeImage(ImageConstants.PHARMACY_DEFAULT_IMAGE);
            }else {
                user.setPathToAlternativeImage(api.getImage());
            }

        }
        return user;
    }
}
