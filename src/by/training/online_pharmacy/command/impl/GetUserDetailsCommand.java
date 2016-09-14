package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 14.08.16.
 */
public class GetUserDetailsCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;

        if(httpSession==null||(user = (User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        String userLogin = request.getParameter(Parameter.LOGIN);
        RegistrationType registrationType = RegistrationType.valueOf(request.getParameter(Parameter.REGISTRATION_TYPE));

        if(user.getLogin().equals(userLogin) && user.getRegistrationType()==registrationType){
            request.getRequestDispatcher(Page.MAIN).forward(request, response);
            return;
        }

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();

        try {
            User userDetails = userService.getUserDetails(userLogin, registrationType);
            request.setAttribute(Parameter.DOCTOR, userDetails);
            request.getRequestDispatcher(Page.DOCTOR_DESCRIPTION).forward(request, response);

        } catch (InvalidParameterException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed parameters is invalid");

            response.setContentType(Content.JSON);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            servletOutputStream.write(jsonObject.toString().getBytes());
        }
        finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();

        }
    }
}
