package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.util.ImageConstant;
import org.apache.commons.compress.utils.IOUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vladislav on 15.08.16.
 */
public class GetUserImageCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);

        if(httpSession==null||httpSession.getAttribute(Parameter.USER)==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        User user = new User();
        user.setLogin(request.getParameter(Parameter.LOGIN));
        RegistrationType registrationType = RegistrationType.valueOf(request.getParameter(Parameter.REGISTRATION_TYPE));
        user.setRegistrationType(registrationType);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();

        try {
            InputStream inputStream = userService.getUserImage(user, request.getServletContext().getRealPath(ImageConstant.USER_IMAGES));
            ServletOutputStream servletOutputStream = response.getOutputStream();
            IOUtils.copy(inputStream, servletOutputStream);
            inputStream.close();

        } catch (InvalidParameterException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed parameters is invalid");

            response.setContentType(Content.JSON);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            servletOutputStream.write(jsonObject.toString().getBytes());

        }
        finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();

        }

    }
}
