package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.Gender;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 29.07.16.
 */
public class UpdatePersonalInformationCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;

        if(httpSession==null||(user=(User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        user.setFirstName(request.getParameter(Parameter.FIRST_NAME));
        user.setSecondName(request.getParameter(Parameter.SECOND_NAME));
        user.setGender(Gender.valueOf(request.getParameter(Parameter.GENDER)));

        JSONObject jsonObject = new JSONObject();
        response.setContentType(Content.JSON);
        ServletOutputStream servletOutputStream = response.getOutputStream();

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();

        try {
            userService.updatePersonalInformation(user);
            jsonObject.put(Parameter.RESULT, true);

        } catch (InvalidParameterException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed parameters is invalid");
            jsonObject.put(Parameter.IS_CRITICAL, false);

        }catch (NotFoundException e) {
            httpSession.invalidate();
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.IS_CRITICAL, true);
            jsonObject.put(Parameter.MESSAGE, "User was not found. May de he was deleted");

        }
        finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();

        }

        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
