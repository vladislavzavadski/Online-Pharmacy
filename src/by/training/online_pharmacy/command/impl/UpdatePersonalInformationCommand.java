package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.Gender;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
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
        if(httpSession==null||(user=(User)httpSession.getAttribute("user"))==null){
            response.sendRedirect(request.getRequestURI());
            return;
        }
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        user.setFirstName(request.getParameter("first_name"));
        user.setSecondName(request.getParameter("second_name"));
        user.setGender(Gender.valueOf(request.getParameter("gender")));
        userService.updatePersonalInformation(user);
        response.setContentType("application/json");
        ServletOutputStream servletOutputStream = response.getOutputStream();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "success");
        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
