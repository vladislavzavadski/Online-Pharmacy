package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.drug.DrugClass;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.DrugService;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.InvalidUserStatusException;
import by.training.online_pharmacy.service.exception.ServiceException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 02.09.16.
 */
public class CreateDrugClassCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;

        if(httpSession==null||(user=(User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        ServletOutputStream servletOutputStream = response.getOutputStream();
        response.setContentType(Content.JSON);

        DrugClass drugClass = new DrugClass();
        drugClass.setName(request.getParameter(Parameter.CLASS));
        drugClass.setDescription(request.getParameter(Parameter.CLASS_DESCRIPTION));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        DrugService drugService = serviceFactory.getDrugService();

        try {
            drugService.createDrugClass(user, drugClass);
            jsonObject.put(Parameter.RESULT, true);

        } catch (InvalidParameterException | InvalidUserStatusException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed  parameters is invalid");

        }
        finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();
        }

        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
