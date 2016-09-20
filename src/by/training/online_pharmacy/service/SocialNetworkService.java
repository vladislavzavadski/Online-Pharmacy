package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.exception.CanceledAuthorizationException;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;

/**
 * Created by vladislav on 25.07.16.
 */
public interface SocialNetworkService {
    User userLoginVk(String code, String pathToImages) throws CanceledAuthorizationException, InternalServerException, InvalidParameterException;

    User userLoginFb(String code, String pathToImages) throws CanceledAuthorizationException, InternalServerException, InvalidParameterException;

    User userLoginLi(String code, String pathToImages) throws CanceledAuthorizationException, InternalServerException, InvalidParameterException;
}
