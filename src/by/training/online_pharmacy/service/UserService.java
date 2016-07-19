package by.training.online_pharmacy.service;

import by.training.online_pharmacy.domain.VkResponse;
import by.training.online_pharmacy.domain.user.User;

/**
 * Created by vladislav on 18.07.16.
 */
public interface UserService {
    User userLogin(String login, String password);
    User userLoginVk(String vkId);
    void userRegistration(User user);
    User userRegistrationVk(VkResponse vkResponse);
}
