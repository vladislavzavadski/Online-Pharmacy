package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.CanceledAuthorizationException;
import by.training.online_pharmacy.service.exception.InternalServerException;

/**
 * Created by vladislav on 25.07.16.
 */
public interface SocialNetworkService {
    User userLoginVk(String code) throws CanceledAuthorizationException, InternalServerException;
    User userLoginFb(String code) throws CanceledAuthorizationException, InternalServerException;
    User userLoginLi(String code) throws CanceledAuthorizationException, InternalServerException;
}
