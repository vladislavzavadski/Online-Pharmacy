package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InternalServerException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vladislav on 28.07.16.
 */
public class CheckLoginCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService service = serviceFactory.getUserService();
        String login = request.getParameter("login");
        JSONObject  jsonObject = new JSONObject();
        boolean isUserExist;
        try {
            isUserExist = service.isUserExist(login);
            jsonObject.put("isExist", isUserExist);
        } catch (InvalidParameterException e) {
            jsonObject.put("isExist", false);
            jsonObject.put("message", e.getMessage());
        }
        response.setContentType("application/json");
        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
