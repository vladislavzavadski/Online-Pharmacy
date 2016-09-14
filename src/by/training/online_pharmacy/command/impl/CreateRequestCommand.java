package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.dao.impl.database.Param;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.RequestService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vladislav on 10.08.16.
 */
public class CreateRequestCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;

        if(httpSession==null||(user = (User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        ServletOutputStream servletOutputStream = response.getOutputStream();
        response.setContentType(Content.JSON);

        RequestForPrescription requestForPrescription = new RequestForPrescription();
        requestForPrescription.setClient(user);
        requestForPrescription.setClientComment(request.getParameter(Parameter.CLIENT_COMMENT));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Parameter.DATE_PATTERN);

        String date = request.getParameter(Parameter.PROLONG_DATE);

        try {
            requestForPrescription.setProlongDate(simpleDateFormat.parse(date));

        } catch (ParseException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "You must pass date in format "+Parameter.DATE_PATTERN);
            return;

        }

        Drug drug = new Drug();
        requestForPrescription.setDrug(drug);
        drug.setId(Integer.parseInt(request.getParameter(Parameter.DRUG_ID)));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        RequestService requestService = serviceFactory.getRequestService();

        try {
            requestService.createRequest(requestForPrescription);
            jsonObject.put(Parameter.RESULT, true);

        } catch (InvalidParameterException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.IS_CRITICAL, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed parameters is invalid");

        } catch (NotFoundException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.IS_CRITICAL, e.isCritical());
            jsonObject.put(Parameter.MESSAGE, "The requested drug was not");

        }

        finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();

        }

        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
