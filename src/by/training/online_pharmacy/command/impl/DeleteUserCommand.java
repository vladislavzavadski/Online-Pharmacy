package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InvalidPasswordException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 06.08.16.
 */
public class DeleteUserCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;
        if(httpSession==null||(user = (User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }
        response.setContentType(Content.JSON);
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        user.setPassword(request.getParameter(Parameter.PASSWORD));
        JSONObject jsonObject = new JSONObject();
        try {
            userService.deleteUser(user);
            jsonObject.put(Parameter.RESULT, true);
            httpSession.invalidate();
        } catch (InvalidPasswordException e) {
            jsonObject.put(Parameter.RESULT, false);
        }
        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
