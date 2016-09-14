package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.prescription.RequestForPrescriptionCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.RequestService;
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
 * Created by vladislav on 26.08.16.
 */
public class GetRequestsCommand implements Command {
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
        boolean pageOverload = Boolean.parseBoolean(request.getParameter(Parameter.OVERLOAD));

        RequestForPrescriptionCriteria criteria = new RequestForPrescriptionCriteria();
        criteria.setDrugName(request.getParameter(Parameter.DRUG_NAME));
        criteria.setRequestStatus(request.getParameter(Parameter.REQUEST_STATUS));
        criteria.setRequestDateFrom(request.getParameter(Parameter.DATE_FROM));
        criteria.setRequestDateTo(request.getParameter(Parameter.DATE_TO));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        RequestService requestService = serviceFactory.getRequestService();

        try {
            List<RequestForPrescription> requestForPrescriptions = requestService.searchRequests(user,
                    criteria, LIMIT, (page-1)*LIMIT);

            request.setAttribute(Parameter.REQUESTS, requestForPrescriptions);

            if(pageOverload) {
                request.getRequestDispatcher(Page.REQUESTS).forward(request, response);
            }
            else {
                request.getRequestDispatcher(Page.REQUEST).forward(request, response);
            }

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
