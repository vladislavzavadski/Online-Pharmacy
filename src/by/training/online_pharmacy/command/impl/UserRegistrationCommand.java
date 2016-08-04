package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.Gender;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserRole;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vladislav on 18.07.16.
 */
public class UserRegistrationCommand implements Command {
    private static final int PASSWORD_LENGTH = 7;
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        User user = new User();
        String password = RandomStringUtils.random(PASSWORD_LENGTH, true, true);
        String email = request.getParameter(Parameter.MAIL);
        user.setFirstName(request.getParameter(Parameter.FIRST_NAME));
        user.setSecondName(request.getParameter(Parameter.SECOND_NAME));
        user.setLogin(request.getParameter(Parameter.LOGIN));
        user.setPassword(password);
        user.setMail(email);
        user.setGender(Gender.UNKNOWN);
        user.setRegistrationType(RegistrationType.NATIVE);
        user.setUserRole(UserRole.CLIENT);
        try {
            userService.userRegistration(user);
        } catch (InvalidParameterException e) {
            request.setAttribute(Parameter.MESSAGE, e.getMessage());
            request.getRequestDispatcher(Page.REGISTRATION).forward(request, response);
            return;
        }
        request.setAttribute(Parameter.REGISTRATION_SUCCESS, true);
        request.getRequestDispatcher(Page.INDEX).forward(request, response);
    }
}
