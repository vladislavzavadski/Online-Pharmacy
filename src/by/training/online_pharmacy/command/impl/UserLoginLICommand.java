package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.command.util.UrlBuilder;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.SocialNetworkService;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.CanceledAuthorizationException;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;

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
        if(httpSession!=null&&httpSession.getAttribute(Parameter.USER)!=null){
            request.getRequestDispatcher(Page.MAIN).forward(request, response);
            return;
        }
        try {
            user = socialNetworkService.userLoginLi(request.getParameter(Parameter.CODE));
        } catch (CanceledAuthorizationException e) {
            response.sendRedirect(Page.INDEX);
            return;
        } catch (InvalidParameterException e) {
            request.setAttribute(Parameter.MESSAGE, e.getMessage());
            request.getRequestDispatcher(Page.AUTHORIZATION).forward(request, response);
            return;
        }
        request.getSession(true).setAttribute(Parameter.USER, user);
        request.getSession(true).setAttribute(Parameter.PREV_REQUEST, UrlBuilder.build(request));
        request.getRequestDispatcher(Page.MAIN).forward(request, response);
    }
}
