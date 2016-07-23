package by.training.online_pharmacy.controller;

import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.CanceledAuthorizationException;
import by.training.online_pharmacy.service.exception.InternalServerException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vladislav on 23.07.16.
 */
public class UserLoginFBCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        User user = null;
        try {
            user = userService.userLoginFb(request.getParameter("code"));
        } catch (CanceledAuthorizationException e) {
            e.printStackTrace();
            //TODO:дописать
        } catch (InternalServerException e) {
            e.printStackTrace();
        }
        request.getSession(true).setAttribute("user", user);
        request.getRequestDispatcher("/main.jsp").forward(request, response);
    }
}
