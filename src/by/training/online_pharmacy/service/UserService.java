package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.*;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vladislav on 18.07.16.
 */
public interface UserService {
    User userLogin(String login, String password) throws InternalServerException, UserNotFoundException, InvalidParameterException;
    void userRegistration(User user) throws InvalidParameterException;
    boolean isUserExist(String login) throws InvalidParameterException;
    void updatePersonalInformation(User user) throws InvalidParameterException;
    void updatePassword(User user, String newPassword) throws InvalidPasswordException, InvalidParameterException;
    void updateContacts(User user) throws InvalidParameterException;
    void uploadProfileImage(User user, Part part) throws InvalidContentException, IOException, InvalidParameterException;
}
