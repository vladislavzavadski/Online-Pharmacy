package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.command.util.UrlBuilder;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.SocialNetworkService;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.CanceledAuthorizationException;
import by.training.online_pharmacy.service.exception.InternalServerException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 23.07.16.
 */
public class UserLoginLICommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        SocialNetworkService socialNetworkService = serviceFactory.getSocialNetworkService();
        User user = null;
        HttpSession httpSession = request.getSession(false);
        if(httpSession!=null&&httpSession.getAttribute("user")!=null){
            request.getRequestDispatcher("/main.jsp").forward(request, response);
            return;
        }
        try {
            user = socialNetworkService.userLoginLi(request.getParameter("code"));
        } catch (CanceledAuthorizationException e) {
            response.sendRedirect("/index.jsp");
            return;
        } catch (InternalServerException e) {
            e.printStackTrace();
        }
        request.getSession(true).setAttribute("user", user);
        request.getSession(true).setAttribute("prevRequest", UrlBuilder.build(request));
        request.getRequestDispatcher("/main.jsp").forward(request, response);
    }
}
