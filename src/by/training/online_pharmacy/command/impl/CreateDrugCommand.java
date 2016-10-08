package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.DrugClass;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;
import by.training.online_pharmacy.domain.drug.DrugType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.DrugService;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.*;
import by.training.online_pharmacy.service.util.ImageConstant;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vladislav on 31.08.16.
 */
public class CreateDrugCommand implements Command {

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

        Drug drug = new Drug();
        String drugName = request.getParameter(Parameter.DRUG_NAME);

        drug.setName(drugName);
        drug.setPrescriptionEnable(Boolean.parseBoolean(request.getParameter(Parameter.PRESCRIPTION_STATUS)));
        drug.setActiveSubstance(request.getParameter(Parameter.ACTIVE_SUBSTANCE));
        drug.setDescription(request.getParameter(Parameter.DRUG_DESCRIPTION));
        drug.setType(DrugType.valueOf(request.getParameter(Parameter.DRUG_TYPE).toUpperCase()));
        drug.setDoctorSpecialization(request.getParameter(Parameter.SPECIALIZATION));
        drug.setPrice(Float.parseFloat(request.getParameter(Parameter.DRUG_PRICE)));
        drug.setDrugsInStock(Integer.parseInt(request.getParameter(Parameter.DRUGS_IN_STOCK)));

        DrugClass drugClass = new DrugClass();
        drug.setDrugClass(drugClass);
        drugClass.setName(request.getParameter(Parameter.CLASS));

        String[] dosages = request.getParameterValues(Parameter.DRUG_DOSAGE);

        if(dosages!=null) {
            List<String> stringDosages = Arrays.asList(dosages);
            List<Integer> dosagesList = new ArrayList<>(stringDosages.size());

            dosagesList.addAll(stringDosages.stream().map(Integer::parseInt).collect(Collectors.toList()));

            drug.setDosages(dosagesList);
        }

        String manufacture = request.getParameter(Parameter.MANUFACTURER);
        String[] manufactureParams;

        if(manufacture!=null && (manufactureParams=manufacture.split(Parameter.COMMA)).length==2) {
            DrugManufacturer drugManufacturer = new DrugManufacturer();
            drugManufacturer.setName(manufactureParams[0]);
            drugManufacturer.setCountry(manufactureParams[1]);
            drug.setDrugManufacturer(drugManufacturer);
        }

        JSONObject jsonObject = new JSONObject();
        ServletOutputStream servletOutputStream = response.getOutputStream();
        response.setContentType(Content.JSON);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        DrugService drugService = serviceFactory.getDrugService();

        try {
            drugService.createDrug(user, drug, request.getPart(Parameter.DRUG_IMAGE), request.getServletContext().getRealPath(ImageConstant.DRUG_IMAGES));
            jsonObject.put(Parameter.RESULT, true);

        } catch (InvalidParameterException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed parameters is invalid");

        } catch (InvalidUserStatusException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "User status is invalid. Only pharmacist can create drugs");

        } catch (SpecializationNotFoundException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "Doctor specialization="+drug.getDoctorSpecialization()+" was not found");

        }  catch (InvalidContentException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "File, that you trying to upload is not an image, or file size greater than 10 MB");

        } finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();

        }

        servletOutputStream.write(jsonObject.toString().getBytes());

    }
}
