package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.DrugClass;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;
import by.training.online_pharmacy.domain.prescription.Prescription;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserDescription;
import by.training.online_pharmacy.domain.user.UserRole;
import by.training.online_pharmacy.service.DrugService;
import by.training.online_pharmacy.service.InitConnectionService;
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
 * Created by vladislav on 08.08.16.
 */
public class GetDrugDetailsCommand implements Command {

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

        int drugId = Integer.parseInt(request.getParameter(Parameter.DRUG_ID));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        DrugService drugService = serviceFactory.getDrugService();

        try {
            Drug drug = drugService.getDrugDetails(drugId);
            boolean isPrescriptionExist = false;

            if(drug.isPrescriptionEnable()&&user.getUserRole()==UserRole.CLIENT){
                PrescriptionService prescriptionService = serviceFactory.getPrescriptionService();
                Prescription prescription = prescriptionService.getActivePrescription(user, drugId);

                isPrescriptionExist = prescription!=null;

                request.setAttribute(Parameter.PRESCRIPTION, prescription);
            }

            request.setAttribute(Parameter.PRESCRIPTION_EXIST, isPrescriptionExist);

            if(user.getUserRole() == UserRole.PHARMACIST){
                List<DrugManufacturer> drugManufacturers = drugService.getDrugManufactures();
                request.setAttribute(Parameter.DRUG_MANUFACTURES, drugManufacturers);

                List<DrugClass> drugClasses = drugService.getAllDrugClasses();
                request.setAttribute(Parameter.DRUG_CLASSES, drugClasses);

                List<UserDescription> userDescriptions = ServiceFactory.getInstance().getUserService().getAllSpecializations();
                request.setAttribute(Parameter.SPECIALIZATIONS, userDescriptions);

            }

            request.setAttribute(Parameter.DRUG, drug);
            request.getRequestDispatcher(Page.DRUG_DETAILS).forward(request, response);

        } catch (InvalidParameterException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed parameters is invalid");

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
