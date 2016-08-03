package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.UserDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.*;
import by.training.online_pharmacy.service.util.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vladislav on 18.07.16.
 */
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getRootLogger();
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_PATTERN = "^\\+(?:[0-9]●?){6,14}[0-9]$";
    @Override
    public User userLogin(String login, String password) throws UserNotFoundException, InvalidParameterException {
        if(login==null||login.equals("")){
            throw new InvalidParameterException("Parameter login is invalid");
        }
        if(password==null||password.equals("")){
            throw new InvalidParameterException("Parameter password is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        User user;
        try {
            user = userDAO.userAuthentication(login, password, RegistrationType.NATIVE);
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
    public void userRegistration(User user) throws InvalidParameterException {
        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user.getLogin()==null||user.getLogin().equals("")){
            throw new InvalidParameterException("User's login is invalid");
        }
        if(user.getPassword()==null||user.getPassword().equals("")){
            throw new InvalidParameterException("User's password is invalid");
        }
        if(user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter RegistrationType is invalid");
        }
        if(user.getGender()==null){
            throw new InvalidParameterException("User's gender is invalid");
        }
        if(user.getMail()==null||user.getMail().equals("")||!user.getMail().matches(EMAIL_PATTERN)){
            throw new InvalidParameterException("User's email is invalid.");
        }
        if(user.getFirstName()==null||user.getFirstName().equals("")){
            throw new InvalidParameterException("Parameter First Name is invalid");
        }
        if(user.getSecondName()==null||user.getSecondName().equals("")){
            throw new InvalidParameterException("Parameter Second name is invalid");
        }
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
    public boolean isUserExist(String login) throws InvalidParameterException {
        if(login==null||login.equals("")){
            throw new InvalidParameterException("Parameter login is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        try {
            return userDAO.isLoginUsed(login);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to check user login", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void updatePersonalInformation(User user) throws InvalidParameterException {
        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user.getLogin()==null||user.getLogin().equals("")){
            throw new InvalidParameterException("User's login is invalid");
        }
        if(user.getFirstName()==null||user.getFirstName().equals("")){
            throw new InvalidParameterException("Parameter First Name is invalid");
        }
        if(user.getSecondName()==null||user.getSecondName().equals("")){
            throw new InvalidParameterException("Parameter Second name is invalid");
        }
        if(user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter RegistrationType is invalid");
        }
        if(user.getGender()==null){
            throw new InvalidParameterException("User's gender is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        try {
            userDAO.updatePersonalInformation(user);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to update users personal info", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void updatePassword(User user, String newPassword) throws InvalidPasswordException, InvalidParameterException {
        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter RegistrationType is invalid");
        }
        if(user.getLogin()==null||user.getLogin().equals("")){
            throw new InvalidParameterException("User's login is invalid");
        }
        if(user.getPassword()==null||user.getPassword().equals("")){
            throw new InvalidParameterException("Old password is invalid");
        }
        if(newPassword==null||newPassword.equals("")){
            throw new InvalidParameterException("New password is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        try {
            int usersCount = userDAO.updateUsersPassword(user, newPassword);
            if(usersCount==0){
                throw new InvalidPasswordException("Entered old password is incorrect");
            }
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to update users password", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void updateContacts(User user) throws InvalidParameterException {
        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter RegistrationType is invalid");
        }
        if(user.getLogin()==null||user.getLogin().equals("")){
            throw new InvalidParameterException("User's login is invalid");
        }
        if(user.getPhone()==null||!user.getPhone().matches(PHONE_PATTERN)){
            throw new InvalidParameterException("Invalid phone number");
        }
        if(user.getMail()==null||!user.getMail().matches(EMAIL_PATTERN)){
            throw new InvalidParameterException("Invalid e-mail");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        try {
            userDAO.updateUsersContacts(user);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to update users contacts", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void uploadProfileImage(User user, Part part) throws InvalidContentException, IOException, InvalidParameterException {
        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter RegistrationType is invalid");
        }
        if(user.getLogin()==null||user.getLogin().equals("")){
            throw new InvalidParameterException("User's login is invalid");
        }
        if(part==null){
            throw new InvalidParameterException("Invalid new image parameter");
        }
        InputStream newImageStream = part.getInputStream();
        byte[] buffer = new byte[10];
        List<Byte> image = new ArrayList<>(1024);
        try {
            String str = URLConnection.guessContentTypeFromStream(newImageStream);
            if(str==null||!str.startsWith("image/")){
                throw new InvalidContentException("The uploaded file is not an image");
            }
            if (newImageStream.available() > 4194304) {
                throw new InvalidContentException("The uploaded file is too big");
            }
            while (newImageStream.available() > 0) {
                newImageStream.read(buffer);
                image.addAll(Arrays.asList(ArrayUtils.toObject(buffer)));
            }
        } catch (IOException ex){
            logger.error("Something went wrong when trying to upload user profile image.", ex);
            throw new InternalServerException(ex);
        }

        byte[] newImage = ArrayUtils.toPrimitive(image.toArray(new Byte[image.size()]));
        try {
            DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
            UserDAO userDAO = daoFactory.getUserDAO();
            user.setUserImage(newImage);
            userDAO.uploadProfileImage(user);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to upload user profile image.", e);
            throw new InternalServerException(e);
        }
    }

}
