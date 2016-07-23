package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.CanceledAuthorizationException;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.UserNotFoundException;

/**
 * Created by vladislav on 18.07.16.
 */
public interface UserService {
    User userLogin(String login, String password, RegistrationType registrationType) throws InternalServerException, UserNotFoundException;
    User userLoginVk(String code) throws CanceledAuthorizationException, InternalServerException;
    User userLoginFb(String code) throws CanceledAuthorizationException, InternalServerException;
    User userLoginLi(String code) throws CanceledAuthorizationException, InternalServerException;
    void userRegistration(User user);
}
