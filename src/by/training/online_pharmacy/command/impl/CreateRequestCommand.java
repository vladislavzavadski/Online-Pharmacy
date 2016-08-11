package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.prescription.RequestForPrescription;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.RequestService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
        RequestForPrescription requestForPrescription = new RequestForPrescription();
        requestForPrescription.setClient(user);
        requestForPrescription.setClientComment(request.getParameter(Parameter.CLIENT_COMMENT));
        requestForPrescription.setProlongDate(new Date(request.getParameter(Parameter.PROLONG_DATE)));
        Drug drug = new Drug();
        requestForPrescription.setDrug(drug);
        drug.setId(Integer.parseInt(request.getParameter(Parameter.DRUG_ID)));
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        RequestService requestService = serviceFactory.getRequestService();
        try {
            requestService.createRequest(requestForPrescription);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            if(e.isCritical()){
                httpSession.invalidate();
            }
        }
    }
}
