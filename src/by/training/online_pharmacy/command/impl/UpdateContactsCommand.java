package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.User;
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
 * Created by vladislav on 29.07.16.
 */
public class UpdateContactsCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;
        if(httpSession==null||(user = (User)httpSession.getAttribute("user"))==null){
            response.sendRedirect(request.getRequestURI());
            return;
        }
        JSONObject jsonObject = new JSONObject();
        user.setMail(request.getParameter("mail"));
        user.setPhone(request.getParameter("phone"));
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        try {
            userService.updateContacts(user);
            jsonObject.put("result", true);
        } catch (InvalidParameterException e) {
            jsonObject.put("result", false);
            jsonObject.put("message", e.getMessage());
        }
        response.setContentType("application/json");
        ServletOutputStream servletOutputStream = response.getOutputStream();

        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
