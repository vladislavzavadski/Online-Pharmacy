package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.command.util.UrlBuilder;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.UserNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 18.07.16.
 */
public class UserLoginCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        User user = null;
        HttpSession httpSession = request.getSession(false);
        if(httpSession!=null&&httpSession.getAttribute("user")!=null){
            request.getRequestDispatcher("/main.jsp").forward(request, response);
            return;
        }
        try {
            user = userService.userLogin(request.getParameter("login"), request.getParameter("password"), RegistrationType.NATIVE);
        }  catch (UserNotFoundException e) {
            request.getRequestDispatcher("/authorization.jsp").forward(request, response);
            return;
        }
        httpSession = request.getSession(true);
        httpSession.setAttribute("user", user);
        httpSession.setAttribute("prevRequest", UrlBuilder.build(request));
        request.getRequestDispatcher("/main.jsp").forward(request, response);
    }
}
