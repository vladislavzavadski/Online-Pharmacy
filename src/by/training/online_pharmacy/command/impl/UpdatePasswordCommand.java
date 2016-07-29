package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
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
public class UpdatePasswordCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;
        if(httpSession==null||(user = (User)httpSession.getAttribute("user"))==null){
            response.sendRedirect(request.getRequestURI());
            return;
        }
        user.setPassword(request.getParameter("old_password"));
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        response.setContentType("application/json");
        ServletOutputStream servletOutputStream = response.getOutputStream();
        JSONObject jsonObject = new JSONObject();
        boolean result = userService.updatePassword(user, request.getParameter("new_password"));
        jsonObject.put("result", result);
        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
