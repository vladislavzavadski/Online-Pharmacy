package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.prescription.Prescription;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.PrescriptionService;
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
 * Created by vladislav on 27.08.16.
 */
public class GetAllPrescriptionsCommand implements Command {
    private static final int LIMIT = 6;
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user;
        HttpSession httpSession = request.getSession(false);
        if(httpSession==null||(user=(User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        PrescriptionService prescriptionService = serviceFactory.getPrescriptionService();
        int page = Integer.parseInt(request.getParameter(Parameter.PAGE));
        String drugName = request.getParameter(Parameter.DRUG_NAME);
        String prescriptionStatus = request.getParameter(Parameter.PRESCRIPTION_STATUS);
        try {
            List<Prescription> prescriptions = prescriptionService.searchPrescriptions(user, drugName, prescriptionStatus, LIMIT, (page-1)*LIMIT);
            //TODO:страницы
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
