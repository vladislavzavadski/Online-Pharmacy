package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.command.util.UrlBuilder;
import by.training.online_pharmacy.domain.message.MessageStatus;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserRole;
import by.training.online_pharmacy.service.*;
import by.training.online_pharmacy.service.exception.CanceledAuthorizationException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.InvalidUserStatusException;
import by.training.online_pharmacy.service.util.ImageConstant;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 18.07.16.
 */
public class UserLoginVKCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user;

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        SocialNetworkService socialNetworkService = serviceFactory.getSocialNetworkService();

        try {
            user = socialNetworkService.userLoginVk(request.getParameter(Parameter.CODE), request.getServletContext().getRealPath(ImageConstant.USER_IMAGES));
            HttpSession httpSession = request.getSession(true);

            if(user.getUserRole()== UserRole.CLIENT || user.getUserRole()==UserRole.DOCTOR){
                MessageService messageService = serviceFactory.getMessageService();

                int messageCount = messageService.getMessageCount(user, MessageStatus.NEW);
                httpSession.setAttribute(Parameter.MESSAGES_COUNT, messageCount);
            }

            if(user.getUserRole()==UserRole.DOCTOR){
                RequestService requestService = serviceFactory.getRequestService();

                int requestCount = requestService.getRequestsCount(user);
                httpSession.setAttribute(Parameter.REQUEST_COUNT, requestCount);
            }

            user.setPassword(request.getParameter(Parameter.PASSWORD));

            httpSession.setAttribute(Parameter.USER, user);
            httpSession.setAttribute(Parameter.PREV_REQUEST, UrlBuilder.build(request));

            response.sendRedirect(Page.ROOT);

        } catch (CanceledAuthorizationException e) {
            response.sendRedirect(Page.INDEX);

        } catch (InvalidParameterException e) {
            JSONObject jsonObject = new JSONObject();
            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType(Content.JSON);

            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed parameters is invalid");

            servletOutputStream.write(jsonObject.toString().getBytes());

        } catch (InvalidUserStatusException e) {
            JSONObject jsonObject = new JSONObject();
            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType(Content.JSON);

            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "Only doctors can get requests count");

            servletOutputStream.write(jsonObject.toString().getBytes());

        } finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();

        }
    }
}
