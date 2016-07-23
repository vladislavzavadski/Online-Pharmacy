package by.training.online_pharmacy.controller;

import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.UserNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vladislav on 18.07.16.
 */
public class UserLoginCommand implements Command{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        User user = null;
        try {
            user = userService.userLogin(request.getParameter("login"), request.getParameter("password"), RegistrationType.NATIVE);
        } catch (InternalServerException e) {
            e.printStackTrace();
        } catch (UserNotFoundException e) {
            request.setAttribute("login", request.getParameter("login"));
            request.getRequestDispatcher("/authorization.jsp").forward(request, response);
        }
        request.getSession(true).setAttribute("user", user);
        request.getRequestDispatcher("/main.jsp").forward(request, response);
        //TODO:перенаправление на страницу
    }
}
