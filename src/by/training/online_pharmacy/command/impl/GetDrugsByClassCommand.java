package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.service.DrugService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by vladislav on 07.08.16.
 */
public class GetDrugsByClassCommand implements Command {
    private static final int DRUGS_ON_PAGE = 6;
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        if(httpSession==null||httpSession.getAttribute(Parameter.USER)==null){
            response.sendRedirect(Page.INDEX);
            return;
        }
        String className = request.getParameter(Parameter.CLASS);
        int page = Integer.parseInt(request.getParameter(Parameter.PAGE));
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        DrugService drugService = serviceFactory.getDrugService();
        try {
            List<Drug> drugs = drugService.getDrugsByClass(className, DRUGS_ON_PAGE, (page-1)*DRUGS_ON_PAGE);
            request.setAttribute(Parameter.DRUGS, drugs);
            request.getRequestDispatcher(Page.DRUG).forward(request, response);
        } catch (InvalidParameterException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, e.getMessage());
            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType(Content.JSON);
            servletOutputStream.write(jsonObject.toString().getBytes());
        }
    }
}
