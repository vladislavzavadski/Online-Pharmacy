package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InvalidContentException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;

/**
 * Created by vladislav on 31.07.16.
 */
public class UploadProfileImageCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;
        if(httpSession==null||(user=(User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }
        ServletOutputStream servletOutputStream = response.getOutputStream();
        response.setContentType(Content.JSON);
        JSONObject jsonObject = new JSONObject();
        Part part = request.getPart(Parameter.PROFILE_IMAGE);
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        try {
            userService.uploadProfileImage(user, part);
            jsonObject.put(Parameter.RESULT, true);
        } catch (InvalidContentException|InvalidParameterException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, e.getMessage());
            jsonObject.put(Parameter.IS_CRITICAL, false);
        }catch (NotFoundException e) {
            httpSession.invalidate();
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.IS_CRITICAL, true);
            jsonObject.put(Parameter.MESSAGE, e.getMessage());
        }
        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
