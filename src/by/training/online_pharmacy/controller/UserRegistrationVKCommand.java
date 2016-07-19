package by.training.online_pharmacy.controller;

import by.training.online_pharmacy.domain.VkResponse;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vladislav on 18.07.16.
 */
public class UserRegistrationVKCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        VkResponse vkResponse = new VkResponse();
        vkResponse.setEmail(request.getParameter("email"));
        vkResponse.setId(request.getParameter("user_id"));
        vkResponse.setToken(request.getParameter("access_token"));
        User user = userService.userRegistrationVk(vkResponse);
        request.setAttribute("user", user);
        request.getRequestDispatcher("").forward(request, response);
    }
}
