package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserDescription;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.util.ImageConstant;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by vladislav on 13.08.16.
 */
public class GetDoctorsCommand implements Command{

    private static final int LIMIT = 6;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);

        if(httpSession==null||httpSession.getAttribute(Parameter.USER)==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        boolean pageOverload = Boolean.parseBoolean(request.getParameter(Parameter.OVERLOAD));

        int page = Integer.parseInt(request.getParameter(Parameter.PAGE));

        UserDescription userDescription = new UserDescription();
        userDescription.setSpecialization(request.getParameter(Parameter.SPECIALIZATION));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();

        try {

            List<User> doctors = userService.getDoctors(userDescription, LIMIT, (page-1)*LIMIT, request.getServletContext().getRealPath(ImageConstant.USER_IMAGES));

            System.out.println(doctors);

            request.setAttribute(Parameter.DOCTOR_LIST, doctors);

            if(pageOverload){
                List<UserDescription> userDescriptions = userService.getAllSpecializations();

                request.setAttribute(Parameter.SPECIALIZATIONS, userDescriptions);
                request.getRequestDispatcher(Page.DOCTORS).forward(request, response);
            }
            else {
                request.getRequestDispatcher(Page.DOCTOR).forward(request, response);
            }

        } catch (InvalidParameterException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed  parameters is invalid");

            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType(Content.JSON);
            servletOutputStream.write(jsonObject.toString().getBytes());

        }
        finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();
        }
    }
}