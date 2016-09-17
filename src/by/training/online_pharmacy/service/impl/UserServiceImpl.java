package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.DaoFactory;
import by.training.online_pharmacy.dao.SecretWordDao;
import by.training.online_pharmacy.dao.UserDAO;
import by.training.online_pharmacy.dao.UserDescriptionDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.exception.EntityDeletedException;
import by.training.online_pharmacy.domain.user.*;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.*;
import by.training.online_pharmacy.service.util.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Part;
import java.io.*;
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
    private static final int PASSWORD_LENGTH = 7;

    @Override
    public User userLogin(String login, String password)
            throws InternalServerException, UserNotFoundException, InvalidParameterException {

        if(login==null||login.isEmpty()){
            throw new InvalidParameterException("Parameter login is invalid");
        }

        if(password==null||password.equals("")){
            throw new InvalidParameterException("Parameter password is invalid");
        }

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();

        try {
            User user = userDAO.userAuthentication(login, password, RegistrationType.NATIVE);
            if(user==null){
                throw new UserNotFoundException("Incorrect login or password");
            }

            if(user.getPathToImage()==null){
                user.setPathToImage(ImageConstant.PHARMACY_DEFAULT_IMAGE);
            }

            return user;

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to login as native user", e);
            throw new InternalServerException(e);

        }

    }

    @Override
    public void userRegistration(User user) throws InternalServerException, InvalidParameterException {

        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(user.getLogin()==null||user.getLogin().isEmpty()||user.getLogin().length()>30){
            throw new InvalidParameterException("User's login is invalid");
        }

        if(user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter RegistrationType is invalid");
        }

        if(user.getGender()==null){
            throw new InvalidParameterException("User's gender is invalid");
        }

        if(user.getMail()==null||user.getMail().isEmpty()||!user.getMail().matches(EMAIL_PATTERN)||user.getMail().length()>45){
            throw new InvalidParameterException("User's email is invalid.");
        }

        if(user.getFirstName()==null||user.getFirstName().isEmpty()||user.getFirstName().length()>30){
            throw new InvalidParameterException("Parameter First Name is invalid");
        }

        if(user.getSecondName()==null||user.getSecondName().isEmpty()||user.getSecondName().length()>30){
            throw new InvalidParameterException("Parameter Second name is invalid");
        }

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();

        try {
            user.setPassword(RandomStringUtils.random(PASSWORD_LENGTH, true, true));
            userDAO.insertUser(user);

            EmailSender emailSender = new EmailSender(EmailProperties.EMAIL, EmailProperties.PASSWORD);
            emailSender.send(EmailProperties.TITLE, String.format(EmailProperties.MESSAGE_BODY, user.getFirstName(), user.getLogin(), user.getPassword()), user.getMail());

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to register as native user", e);
            throw new InternalServerException(e);

        }
    }

    @Override
    public boolean isUserExist(String login) throws InternalServerException, InvalidParameterException {

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
    public void updatePersonalInformation(User user) throws InternalServerException, InvalidParameterException, NotFoundException {

        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(user.getLogin()==null||user.getLogin().isEmpty()||user.getLogin().length()>30){
            throw new InvalidParameterException("User's login is invalid");
        }

        if(user.getFirstName()==null||user.getFirstName().isEmpty()||user.getFirstName().length()>30){
            throw new InvalidParameterException("Parameter First Name is invalid");
        }

        if(user.getSecondName()==null||user.getSecondName().isEmpty()||user.getSecondName().length()>30){
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
    public void updatePassword(User user, String newPassword, String oldPassword)
            throws InternalServerException, InvalidPasswordException, InvalidParameterException, NotFoundException {

        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(user.getPassword()==null||user.getPassword().isEmpty()||user.getPassword().length()>45){
            throw new InvalidParameterException("Old password is invalid");
        }

        if(!oldPassword.equals(user.getPassword())){
            throw new InvalidPasswordException("Old password is incorrect");
        }

        if(user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter RegistrationType is invalid");
        }

        if(user.getLogin()==null||user.getLogin().isEmpty()||user.getLogin().length()>45){
            throw new InvalidParameterException("User's login is invalid");
        }

        if(newPassword==null||newPassword.isEmpty()||newPassword.length()>45){
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
    public void updateContacts(User user) throws InternalServerException, InvalidParameterException, NotFoundException {

        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter RegistrationType is invalid");
        }

        if(user.getLogin()==null||user.getLogin().isEmpty()){
            throw new InvalidParameterException("User's login is invalid");
        }

        if(user.getPhone()==null||!user.getPhone().matches(PHONE_PATTERN)||user.getPhone().length()>15){
            throw new InvalidParameterException("Invalid phone number");
        }

        if(user.getMail()==null||!user.getMail().matches(EMAIL_PATTERN)||user.getMail().length()>45){
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
    public void uploadProfileImage(User user, Part part) throws InternalServerException, InvalidContentException, InvalidParameterException, NotFoundException {

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

            if(content==null||!content.startsWith(ImageConstant.IMAGE)){
                throw new InvalidContentException("This file is not an image");
            }

            if(part.getSize()==0||part.getSize()>1.342e+9){
                throw new InvalidContentException("Invalid image size");
            }

            File uploads = new File(ImageConstant.USER_IMAGES);
            String fileName = user.getLogin()+user.getRegistrationType()+ImageConstant.IMAGE_JPG;

            File file = new File(uploads,fileName);
            Files.copy(newImageStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

            user.setPathToImage(file.getAbsolutePath());

            if(user.getPathToImage()==null||!user.getPathToImage().equals(fileName)) {
                DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
                UserDAO userDAO = daoFactory.getUserDAO();
                userDAO.uploadProfileImage(user);
            }

        }catch (EntityDeletedException e) {
            throw new NotFoundException("User with login=" + user.getLogin() + " was not found", e);

        }  catch (DaoException | IOException e) {
            logger.error("Something went wrong, when trying to update profile image", e);
            throw new InternalServerException(e);

        }
    }

    @Override
    public void deleteUser(User user, String password)
            throws InternalServerException, InvalidPasswordException, InvalidParameterException, InvalidUserStatusException {

        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(user.getLogin()==null || user.getLogin().isEmpty()){
            throw new InvalidParameterException("Parameter user login is invalid");
        }

        if(user.getRegistrationType() != RegistrationType.NATIVE){
            throw new InvalidUserStatusException("Only native user can delete account");
        }

        if(!user.getPassword().equals(password)){
            throw new InvalidPasswordException("Entered password is invalid");
        }

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();

        try {
            userDAO.deleteUser(user);//TODO:глянь

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to delete user", e);
            throw new InternalServerException(e);

        }
    }

    @Override
    public List<User> getAllDoctors(int limit, int startFrom, boolean pageOverload)
            throws InternalServerException, InvalidParameterException {

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
    public List<UserDescription> getAllSpecializations() throws InternalServerException{

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
    public List<User> getDoctors(UserDescription userDescription, int limit, int startFrom)
            throws InternalServerException, InvalidParameterException {

        if(limit<=0){
            throw new InvalidParameterException("Invalid parameter limit. Limit can be >0");
        }

        if(startFrom<0){
            throw new InvalidParameterException("Invalid parameter startFrom startFrom can be >0");
        }

        if(userDescription==null){
            throw new InvalidParameterException("Parameter user specialization is invalid");
        }

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();

        try {
            List<User> doctors = userDAO.searchDoctors(userDescription, limit, startFrom);
            doctors.stream().filter(user -> user.getPathToImage()==null).forEach(user -> user.setPathToImage(ImageConstant.PHARMACY_DEFAULT_IMAGE));
            return doctors;

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get doctors by specialization");
            throw new InternalServerException(e);

        }
    }

    @Override
    public List<User> searchDoctors(String query, int limit, int startFrom)
            throws InternalServerException, InvalidParameterException {

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
    public User getUserDetails(String userLogin, RegistrationType registrationType)
            throws InternalServerException, InvalidParameterException {

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

            return user;

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to get user details", e);
            throw new InternalServerException(e);

        }
    }

    @Override
    public InputStream getUserImage(User user) throws InternalServerException, InvalidParameterException {

        if(user==null||user.getLogin()==null||
                user.getLogin().isEmpty()||user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }
       try {

           if (user.getPathToImage() != null) {
                return new FileInputStream(user.getPathToImage());

           } else {
               DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
               UserDAO userDAO = daoFactory.getUserDAO();

               InputStream inputStream = userDAO.getProfileImage(user);

               if (inputStream == null) {
                    return new FileInputStream(ImageConstant.PHARMACY_DEFAULT_IMAGE);
               }

               return inputStream;
           }

       } catch (DaoException | FileNotFoundException e) {
           logger.error("Something went wrong when trying to load image", e);
           throw new InternalServerException(e);

       }
    }

    @Override
    public void staffRegistration(User pharmacist, User newUser)
            throws InternalServerException, InvalidParameterException, InvalidUserStatusException {

        if(pharmacist==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(pharmacist.getUserRole()!= UserRole.PHARMACIST){
            throw new InvalidUserStatusException("Only pharmacist can register new doctor");
        }

        UserDescription userDescription = newUser.getUserDescription();

        if(userDescription==null||userDescription.getSpecialization()==null||userDescription.getSpecialization().length()>40||
                userDescription.getDescription().length()>300||userDescription.getSpecialization().isEmpty()||
                userDescription.getDescription()==null||userDescription.getDescription().isEmpty()){
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

    @Override
    public void addFunds(User user, String cardNumber, float summ)
            throws InternalServerException, InvalidParameterException, InvalidUserStatusException {

        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(user.getLogin()==null||user.getLogin().isEmpty()){
            throw new InvalidParameterException("Parameter user login is invalid");
        }

        if(user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter registration is invalid");
        }

        if(user.getBalance()+summ>999.99){
            throw new InvalidParameterException("Parameter summ is invalid");
        }

        if(cardNumber==null||cardNumber.isEmpty()){
            throw new InvalidParameterException("Parameter card number is invalid");
        }

        if(summ<=0.0){
            throw new InvalidParameterException("Parameter summ is invalid");
        }

        if(user.getUserRole()!=UserRole.CLIENT){
            throw new InvalidUserStatusException("Only client can add funds");
        }

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();

        try {
            userDAO.addMoneyToBalance(user, summ);
            user.setBalance(user.getBalance()+summ);

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to add founds to user balance", e);
            throw new InternalServerException(e);

        }
    }

    @Override
    public void createSecretWord(SecretWord secretWord)
            throws InternalServerException, InvalidParameterException, InvalidUserStatusException, NotFoundException {

        if(secretWord==null){
            throw new InvalidParameterException("Parameter secret word is invalid");
        }

        if(secretWord.getUser()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(secretWord.getUser().getLogin()==null||secretWord.getUser().getLogin().isEmpty()||secretWord.getUser().getLogin().length()>30){
            throw new InvalidParameterException("Parameter user login is invalid");
        }

        if(secretWord.getUser().getRegistrationType()==null){
            throw new InvalidParameterException("Parameter registration type is invalid");
        }

        if(secretWord.getUser().getRegistrationType()!=RegistrationType.NATIVE){
            throw new InvalidUserStatusException("Only native users can set secret word");
        }

        if(secretWord.getSecretQuestion()==null){
            throw new InvalidParameterException("Parameter secret question is invalid");
        }

        if(secretWord.getSecretQuestion().getId()<0){
            throw new InvalidParameterException("Parameter secret question is invalid");
        }

        if(secretWord.getResponse()==null||secretWord.getResponse().isEmpty()||secretWord.getResponse().length()>50){
            throw new InvalidParameterException("Parameter secret response is invalid");
        }

        DaoFactory daoFactory  = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        SecretWordDao secretWordDao = daoFactory.getSecretWordDao();

        try {
            secretWordDao.createSecretWord(secretWord);

        } catch (EntityDeletedException e){
            throw new NotFoundException(e);

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to create secret word");
            throw new InternalServerException(e);

        }

    }

    @Override
    public void reestablishAccount(SecretWord secretWord)
            throws InternalServerException, InvalidParameterException, NotFoundException {

        if(secretWord==null){
            throw new InvalidParameterException("Parameter secret word is invalid");
        }

        if(secretWord.getUser()==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(secretWord.getUser().getLogin()==null||secretWord.getUser().getLogin().isEmpty()){
            throw new InvalidParameterException("Parameter user login is invalid");
        }

        if(secretWord.getResponse()==null||secretWord.getResponse().isEmpty()){
            throw new InvalidParameterException("Parameter secret response is invalid");
        }

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDAO userDAO = daoFactory.getUserDAO();

        try {

            String email = userDAO.getUserMailBySecretWord(secretWord);

            if(email==null){
                throw new NotFoundException("User was not found, or secret word is invalid");
            }

            secretWord.getUser().setRegistrationType(RegistrationType.NATIVE);

            String newPassword = RandomStringUtils.random(PASSWORD_LENGTH, true, true);

            userDAO.updateUsersPassword(secretWord.getUser(), newPassword);

            EmailSender emailSender = new EmailSender(EmailProperties.EMAIL, EmailProperties.PASSWORD);
            emailSender.send(EmailProperties.REESTABLISH_ACCOUNT, String.format(EmailProperties.REESTABLISH_BODY, secretWord.getUser().getLogin(), newPassword), email);
        } catch (DaoException e) {
            logger.error("Something went wrong when trying to reestablish account", e);
            throw new InternalServerException(e);

        }

    }

    @Override
    public void updateUserDescription(User user, UserDescription userDescription)
            throws InvalidParameterException, InvalidUserStatusException {

        if(user==null){
            throw new InvalidParameterException("Parameter user is invalid");
        }

        if(user.getLogin()==null || user.getLogin().isEmpty()){
            throw new InvalidParameterException("Parameter user login is invalid");
        }

        if(user.getRegistrationType()==null){
            throw new InvalidParameterException("Parameter registration type is invalid");
        }

        if(userDescription==null){
            throw new InvalidParameterException("Parameter user description is invalid");
        }

        if(userDescription.getSpecialization()==null || userDescription.getSpecialization().isEmpty()){
            throw new InvalidParameterException("Parameter user specialization is invalid");
        }

        if(userDescription.getDescription()==null || userDescription.getDescription().isEmpty()){
            throw new InvalidParameterException("Parameter user description is invalid");
        }

        if(user.getUserRole()!=UserRole.DOCTOR){
            throw new InvalidUserStatusException("Only doctors can update description");
        }

        userDescription.setUserLogin(user.getLogin());
        userDescription.setRegistrationType(user.getRegistrationType());

        DaoFactory daoFactory = DaoFactory.takeFactory(DaoFactory.DATABASE_DAO_IMPL);
        UserDescriptionDAO userDescriptionDAO = daoFactory.getUserDescriptionDao();

        try {
            userDescriptionDAO.updateUserDescription(userDescription);
            user.setUserDescription(userDescription);

        } catch (DaoException e) {
            logger.error("Something went wrong when trying to update user description");
            throw new InternalServerException(e);

        }
    }

}
