package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.prescription.Prescription;
import by.training.online_pharmacy.domain.prescription.PrescriptionCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.PrescriptionService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.InvalidUserStatusException;
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
        HttpSession httpSession = request.getSession(false);
        User user;

        if(httpSession==null||(user=(User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        int page = Integer.parseInt(request.getParameter(Parameter.PAGE));
        String drugName = request.getParameter(Parameter.DRUG_NAME);
        String prescriptionStatus = request.getParameter(Parameter.PRESCRIPTION_STATUS);
        boolean pageOverload = Boolean.parseBoolean(request.getParameter(Parameter.OVERLOAD));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        PrescriptionService prescriptionService = serviceFactory.getPrescriptionService();

        try {
            List<Prescription> prescriptions = prescriptionService.searchPrescriptions(user, drugName, prescriptionStatus, LIMIT, (page-1)*LIMIT);
            request.setAttribute(Parameter.PRESCRIPTIONS, prescriptions);

            if(pageOverload) {
                request.getRequestDispatcher(Page.PRESCRIPTIONS).forward(request, response);
            }
            else {
                request.getRequestDispatcher(Page.PRESCRIPTION).forward(request, response);
            }

        } catch (InvalidParameterException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed parameters is invalid");

            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType(Content.JSON);
            servletOutputStream.write(jsonObject.toString().getBytes());

        } catch (InvalidUserStatusException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "Pharmacist can not get prescriptions");

            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType(Content.JSON);
            servletOutputStream.write(jsonObject.toString().getBytes());

        } finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();

        }
    }
}
