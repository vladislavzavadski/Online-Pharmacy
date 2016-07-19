package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.domain.VkResponse;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.UserService;

/**
 * Created by vladislav on 18.07.16.
 */
public class UserServiceImpl implements UserService {
    @Override
    public User userLogin(String login, String password) {
        return null;
    }

    @Override
    public User userLoginVk(String vkId) {
        return null;
    }

    @Override
    public void userRegistration(User user) {

    }

    @Override
    public User userRegistrationVk(VkResponse vkResponse) {
        return null;
    }
}
