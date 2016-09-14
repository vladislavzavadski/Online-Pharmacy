package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.*;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.InvalidUserStatusException;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 04.09.16.
 */
public class DoctorRegistrationCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;

        if(httpSession==null||(user=(User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        String email = request.getParameter(Parameter.MAIL);
        User newUser = new User();

        newUser.setFirstName(request.getParameter(Parameter.FIRST_NAME));
        newUser.setSecondName(request.getParameter(Parameter.SECOND_NAME));
        newUser.setLogin(request.getParameter(Parameter.LOGIN));
        newUser.setMail(email);
        newUser.setGender(Gender.UNKNOWN);
        newUser.setRegistrationType(RegistrationType.NATIVE);
        newUser.setUserRole(UserRole.DOCTOR);

        UserDescription userDescription = new UserDescription();
        userDescription.setSpecialization(request.getParameter(Parameter.SPECIALIZATION));
        userDescription.setDescription(request.getParameter(Parameter.DESCRIPTION));

        newUser.setUserDescription(userDescription);

        JSONObject jsonObject = new JSONObject();
        response.setContentType(Content.JSON);
        ServletOutputStream servletOutputStream = response.getOutputStream();

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();

        try {
            userService.staffRegistration(user, newUser);
            jsonObject.put(Parameter.RESULT, true);

        } catch (InvalidParameterException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed parameters is invalid");

        } catch (InvalidUserStatusException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "Only pharmacist can create new doctor");

        } finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();
        }

        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
