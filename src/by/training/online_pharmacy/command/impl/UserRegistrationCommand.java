package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.Gender;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserRole;
import by.training.online_pharmacy.service.Encoding;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import com.sun.xml.internal.ws.wsdl.writer.document.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
        request.setCharacterEncoding(Encoding.UTF8);
        response.setCharacterEncoding(Encoding.UTF8);
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
        JSONObject jsonObject = new JSONObject();
        ServletOutputStream servletOutputStream = response.getOutputStream();
        response.setContentType(Content.JSON);
        try {
            userService.userRegistration(user);
            jsonObject.put(Parameter.RESULT, true);
        } catch (InvalidParameterException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, e.getMessage());
        }
        servletOutputStream.write(jsonObject.toString().getBytes());

    }
}
