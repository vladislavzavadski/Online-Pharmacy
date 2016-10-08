package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.controller.listener.exception.InternalServerException;
import by.training.online_pharmacy.domain.prescription.Prescription;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.prescription.RequestStatus;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.RequestService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.InvalidUserStatusException;
import by.training.online_pharmacy.service.exception.RequestForPrescriptionNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by vladislav on 08.09.16.
 */
public class AnswerToRequestForPrescriptionCommand implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;

        if(httpSession==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        user = (User)httpSession.getAttribute(Parameter.USER);

        if(user==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        RequestForPrescription requestForPrescription = new RequestForPrescription();

        int requestForPrescriptionId = Integer.parseInt(request.getParameter(Parameter.REQUEST_ID));
        requestForPrescription.setId(requestForPrescriptionId);

        RequestStatus requestForPrescriptionStatus = RequestStatus.valueOf(request.getParameter(Parameter.REQUEST_STATUS));
        requestForPrescription.setRequestStatus(requestForPrescriptionStatus);
        requestForPrescription.setDoctorComment(request.getParameter(Parameter.DOCTOR_COMMENT));

        Prescription prescription = new Prescription();
        String expirationDateString = request.getParameter(Parameter.EXPIRATION_DATE);

        if(expirationDateString!=null && !expirationDateString.isEmpty()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Parameter.DATE_PATTERN);

            try {
                prescription.setExpirationDate(simpleDateFormat.parse(expirationDateString));
            } catch (ParseException e) {
                logger.error(e);
                throw new InternalServerException(e);
            }

        }
        String strDrugDosage = request.getParameter(Parameter.DRUG_DOSAGE);

        if(strDrugDosage!=null && !strDrugDosage.isEmpty()) {
            short drugDosage = Short.parseShort(request.getParameter(Parameter.DRUG_DOSAGE));
            prescription.setDrugDosage(drugDosage);
        }

        String strDrugCount = request.getParameter(Parameter.DRUG_COUNT);

        if(strDrugCount!=null && !strDrugCount.isEmpty()) {
            short drugCount = Short.parseShort(request.getParameter(Parameter.DRUG_COUNT));
            prescription.setDrugCount(drugCount);
        }

        JSONObject jsonObject = new JSONObject();
        ServletOutputStream servletOutputStream = response.getOutputStream();
        response.setContentType(Content.JSON);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        RequestService requestService = serviceFactory.getRequestService();

        try {
            requestService.sendResponseForRequestForPrescription(user, prescription, requestForPrescription);
            jsonObject.put(Parameter.RESULT, true);

        } catch (InvalidParameterException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of parameters is invalid");

        } catch (InvalidUserStatusException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "This user can not create prescriptions or answer to request for prescription");

        } catch (RequestForPrescriptionNotFoundException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "This request for prescription was not found");

        }finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();
        }

        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
