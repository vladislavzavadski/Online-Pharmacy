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
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.util.*;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by vladislav on 25.07.16.
 */
public class SocialNetworkServiceImpl implements SocialNetworkService {
    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public User userLoginVk(String code, String pathToImages)
            throws InternalServerException, CanceledAuthorizationException, InvalidParameterException {

        if(code==null){
            throw new CanceledAuthorizationException("User cancel authorization");
        }

        if(code.isEmpty()){
            logger.error("Empty code when trying to log in with VK");
            throw new InvalidParameterException("Empty code when trying to log in with VK");
        }

        VkApi vkApi = new VkApi();

        try {
            User user = getUserFromApi(vkApi, code);
            user.setRegistrationType(RegistrationType.VK);
            user = requestToDao(vkApi, user, pathToImages);
            return user;

        } catch (IOException | DaoException e) {
            logger.error("Something went wrong when trying to log in via VK", e);
            throw new InternalServerException(e);

        }
    }

    @Override
    public User userLoginFb(String code, String pathToImages)
            throws InternalServerException, CanceledAuthorizationException, InvalidParameterException {

        if(code==null){
            throw new CanceledAuthorizationException("User cancel exception");
        }

        if(code.equals("")){
            logger.error("Empty code when trying to log in with Facebook");
            throw new InvalidParameterException("Empty code when trying to log in with Facebook");
        }

        FacebookApi facebookApi = new FacebookApi();

        try {
            User user = getUserFromApi(facebookApi, code);
            user.setRegistrationType(RegistrationType.FACEBOOK);
            user = requestToDao(facebookApi, user, pathToImages);
            return user;

        } catch (IOException|DaoException e) {
            logger.error("Something went wrong when trying to log in via Facebook", e);
            throw new InternalServerException(e);

        }
    }

    @Override
    public User userLoginLi(String code, String pathToImages)
            throws InternalServerException, CanceledAuthorizationException, InvalidParameterException {

        if(code==null){
            throw new CanceledAuthorizationException("User cancel exception");
        }

        if(code.isEmpty()){
            logger.error("Empty code when trying to log in with Linked In");
            throw new InvalidParameterException("Empty code when trying to log in with Linked In");
        }

        LinkedInApi linkedInApi = new LinkedInApi();

        try {
            User user = getUserFromApi(linkedInApi, code);
            user.setRegistrationType(RegistrationType.LINKEDIN);
            user = requestToDao(linkedInApi, user, pathToImages);
            return user;

        } catch (IOException|DaoException e) {
            logger.error("Something went wrong when trying to log in via Linked in", e);
            throw new InternalServerException(e);

        }
    }

    private User getUserFromApi(Api api, String code) throws IOException {
        api.authorization(code);

        User user = new User();
        user.setLogin(api.getId());
        user.setMail(api.getEmail());
        user.setFirstName(api.getFirstName());
        user.setSecondName(api.getSecondName());
        user.setPhone(api.getPhone());
        user.setUserRole(UserRole.CLIENT);
        user.setGender(api.getGender());

        return user;
    }

    private User requestToDao(Api api, User user, String pathToImages) throws IOException, DaoException {

        String login = user.getLogin();
        RegistrationType registrationType = user.getRegistrationType();

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();

        User result = userDAO.userAuthentication(login, registrationType);

        if(result!=null){
            result.setLogin(login);
            result.setRegistrationType(registrationType);

            if(result.getPathToImage()==null) {
                result.setPathToImage(pathToImages+ImageConstant.PHARMACY_DEFAULT_IMAGE);
            }

            return result;
        }

        String pathToImage = api.getImage();

        if(!(pathToImage==null||pathToImage.contains(ImageConstant.VK_DEFAULT_IMAGE)
                ||pathToImage.contains(ImageConstant.FACEBOOK_DEFAULT_IMAGE))){

            ImageDownloader.download(pathToImage, pathToImages, login);
            user.setPathToImage(pathToImages+"/"+login+ImageConstant.IMAGE_JPG);

        }

        userDAO.insertUser(user);

        return user;
    }
}
