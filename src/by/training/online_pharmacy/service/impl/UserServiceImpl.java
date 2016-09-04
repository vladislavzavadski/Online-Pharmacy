package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.UserDAO;
import by.training.online_pharmacy.dao.UserDescriptionDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserDescription;
import by.training.online_pharmacy.domain.user.UserRole;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.*;
import by.training.online_pharmacy.service.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Part;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
            if(user.getPathToImage()==null){
                user.setPathToImage(ImageConstant.PHARMACY_DEFAULT_IMAGE);
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
    public void updatePersonalInformation(User user) throws InvalidParameterException, NotFoundException {
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
        }catch (EntityDeletedException e) {
            throw new NotFoundException("User with login=" + user.getLogin() + " was not found", e);
        }  catch (DaoException e) {
            logger.error("Something went wrong when trying to update users personal info", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void updatePassword(User user, String newPassword, String oldPassword) throws InvalidPasswordException, InvalidParameterException, NotFoundException {
        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(!oldPassword.equals(user.getPassword())){
            throw new InvalidPasswordException("Old password is incorrect");
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
            userDAO.updateUsersPassword(user, newPassword);
        }catch (EntityDeletedException e) {
            throw new NotFoundException("User with login=" + user.getLogin() + " was not found", e);
        }
        catch (DaoException e) {
            logger.error("Something went wrong when trying to update users password", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void updateContacts(User user) throws InvalidParameterException, NotFoundException {
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
        }catch (EntityDeletedException e) {
            throw new NotFoundException("User with login=" + user.getLogin() + " was not found", e);
        }  catch (DaoException e) {
            logger.error("Something went wrong when trying to update users contacts", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void uploadProfileImage(User user, Part part) throws InvalidContentException, InvalidParameterException, NotFoundException {
        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter RegistrationType is invalid");
        }
        if(user.getLogin()==null||user.getLogin().isEmpty()){
            throw new InvalidParameterException("User's login is invalid");
        }
        if(part==null){
            throw new InvalidParameterException("Invalid new image parameter");
        }
        try {
        InputStream newImageStream = part.getInputStream();
        String content = URLConnection.guessContentTypeFromStream(newImageStream);
        if(content==null||!content.startsWith("image/")){
            throw new InvalidContentException("This file is not an image");
        }
        File uploads = new File(ImageConstant.USER_IMAGES);
        File file = new File(uploads, user.getLogin()+user.getRegistrationType()+ImageConstant.IMAGE_JPG);
        Files.copy(newImageStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        user.setPathToImage(file.getAbsolutePath());
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();

            userDAO.uploadProfileImage(user);
        }catch (EntityDeletedException e) {
            throw new NotFoundException("User with login=" + user.getLogin() + " was not found", e);
        }  catch (DaoException e) {
            logger.error("Something went wrong, when trying to update profile image", e);
            throw new InternalServerException(e);
        } catch (IOException e) {
            logger.error("Something went wrong, when trying to update profile image", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void deleteUser(User user) throws InvalidPasswordException {
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        try {
            int result  = userDAO.deleteUser(user);
            if(result==0){
                throw new InvalidPasswordException("Entered password is invalid");
            }
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to delete user", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public List<User> getAllDoctors(int limit, int startFrom, boolean pageOverload) throws InvalidParameterException {
        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }
        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        try {
            List<User> doctors =  userDAO.getAllDoctors(limit, startFrom);
            return doctors;
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to load doctors", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public List<UserDescription> getAllSpecializations(){
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDescriptionDAO userDescriptionDAO = daoFactory.getUserDescriptionDao();
        try {
            return userDescriptionDAO.getAllSpecializations();
        } catch (DaoException e) {
            logger.error("Something when trying to load doctors specializations", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public List<User> getDoctorsBySpecialization(UserDescription userDescription, int limit, int startFrom) throws InvalidParameterException {
        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }
        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }
        if(userDescription==null||userDescription.getSpecialization()==null||userDescription.getSpecialization().isEmpty()){
            throw new InvalidParameterException("Parameter user specialization is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        try {
            List<User> doctors = userDAO.getDoctorsBySpecialization(userDescription, limit, startFrom);
            doctors.stream().filter(user -> user.getPathToImage()==null).forEach(user -> user.setPathToImage(ImageConstant.PHARMACY_DEFAULT_IMAGE));
            return doctors;
        } catch (DaoException e) {
            throw new InternalServerException(e);
        }
    }

    @Override
    public List<User> searchDoctors(String query, int limit, int startFrom) throws InvalidParameterException {
        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }
        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }
        if(query==null||query.isEmpty()){
            throw new InvalidParameterException("Invalid parameter criteria");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        try {
            List<User> doctors = userDAO.searchUsers(query.split(" "), limit, startFrom);
            doctors.stream().filter(user -> user.getPathToImage()==null).forEach(user -> user.setPathToImage(ImageConstant.PHARMACY_DEFAULT_IMAGE));
            return doctors;
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to search doctors", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public User getUserDetails(String userLogin, RegistrationType registrationType) throws InvalidParameterException {
        if(userLogin==null||userLogin.isEmpty()){
            throw new InvalidParameterException("Parameter login is invalid");
        }
        if(registrationType==null){
            throw new InvalidParameterException("Parameter registration type is invalid");
        }
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();
        try {
            User user = userDAO.getUserDetails(userLogin, registrationType);
            if(user!=null&&user.getPathToImage()==null){
                user.setPathToImage(ImageConstant.PHARMACY_DEFAULT_IMAGE);
            }
            return user;
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get user details", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public InputStream getUserImage(User user) throws InvalidParameterException {
        if(user==null||user.getLogin()==null||
                user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user.getPathToImage()!=null){
            if(user.getPathToImage().startsWith(ImageConstant.IMAGES)){
                try {
                    return new FileInputStream(user.getPathToImage());
                } catch (FileNotFoundException e) {
                    logger.error("Something went wrong when trying to load profile image", e);
                    throw new InternalServerException(e);
                }
            }
            else {
                try {
                    URL url = new URL(user.getPathToImage());
                    return url.openStream();
                } catch (IOException e) {
                    logger.error("Something went wrong when trying to load profile image", e);
                    throw new InternalServerException(e);
                }
            }
        }
        else {
            DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
            UserDAO userDAO = daoFactory.getUserDAO();
            try {
                InputStream inputStream =  userDAO.getProfileImage(user);
                if(inputStream==null){
                    return new FileInputStream(ImageConstant.PHARMACY_DEFAULT_IMAGE);
                }
                return inputStream;
            } catch (DaoException|FileNotFoundException e) {
                logger.error("Something went wrong when trying to load image", e);
                throw new InternalServerException(e);
            }
        }
    }

    @Override
    public void staffRegistration(User user1, User newUser) throws InvalidParameterException, InvalidUserStatusException {
        if(user1==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
        if(user1.getUserRole()!= UserRole.PHARMACIST){
            throw new InvalidUserStatusException("Only pharmacist can register new doctor");
        }
        UserDescription userDescription = newUser.getUserDescription();
        if(userDescription==null||userDescription.getSpecialization()==null||userDescription.getSpecialization().isEmpty()
                ||userDescription.getDescription()==null||userDescription.getDescription().isEmpty()){
            throw new InvalidParameterException("Parameter user description is invalid");
        }
        userDescription.setUserLogin(newUser.getLogin());
        userDescription.setRegistrationType(newUser.getRegistrationType());
        userRegistration(newUser);
        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDescriptionDAO userDescriptionDAO = daoFactory.getUserDescriptionDao();
        try {
            userDescriptionDAO.insertUserDescription(userDescription);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to insert new user description");
            throw new InternalServerException(e);
        }
    }

}
