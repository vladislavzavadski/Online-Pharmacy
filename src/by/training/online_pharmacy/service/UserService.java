package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.UserNotFoundException;

/**
 * Created by vladislav on 18.07.16.
 */
public interface UserService {
    User userLogin(String login, String password, RegistrationType registrationType) throws InternalServerException, UserNotFoundException;
    void userRegistration(User user);
    boolean isUserExist(String login);
    void updatePersonalInformation(User user);
    boolean updatePassword(User user, String newPassword);
    void updateContacts(User user);
}
