package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserDescription;
import by.training.online_pharmacy.service.exception.*;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by vladislav on 18.07.16.
 */
public interface UserService {
    User userLogin(String login, String password) throws InternalServerException, UserNotFoundException, InvalidParameterException;
    void userRegistration(User user) throws InvalidParameterException;
    boolean isUserExist(String login) throws InvalidParameterException;
    void updatePersonalInformation(User user) throws InvalidParameterException, NotFoundException;
    void updatePassword(User user, String newPassword, String oldPassword) throws InvalidPasswordException, InvalidParameterException, NotFoundException;
    void updateContacts(User user) throws InvalidParameterException, NotFoundException;
    void uploadProfileImage(User user, Part part, String realPath) throws InvalidContentException, IOException, InvalidParameterException, NotFoundException;
    void deleteUser(User user) throws InvalidPasswordException;

    List<User> getAllDoctors(int limit, int startFrom, boolean pageOverload) throws InvalidParameterException;

    List<UserDescription> getAllSpecializations();

    List<User> getDoctorsBySpecialization(UserDescription userDescription, int limit, int startFrom) throws InvalidParameterException;

    List<User> searchDoctors(String query, int limit, int startFrom) throws InvalidParameterException;

    User getUserDetails(String userLogin, RegistrationType registrationType) throws InvalidParameterException;

    InputStream getUserImage(User user) throws InvalidParameterException;
}
