package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.DrugClass;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;
import by.training.online_pharmacy.service.DrugService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.InvalidParameterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 09.08.16.
 */
public class ExtendedSearchCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       /* HttpSession httpSession = request.getSession(false);
        if(httpSession==null||httpSession.getAttribute(Parameter.USER)==null){
            response.sendRedirect(Page.INDEX);
            return;
        }*/
        int pageNumber = Integer.parseInt(request.getParameter(Parameter.PAGE));
        Drug drug = new Drug();
        drug.setName(request.getParameter(Parameter.NAME));
        drug.setActiveSubstance(request.getParameter(Parameter.ACTIVE_SUBSTANCE));
        try {
            float maxPrice = Float.parseFloat(request.getParameter(Parameter.MAX_PRICE));
            drug.setPrice(maxPrice);
        }catch (NumberFormatException ex){
            drug.setPrice((float) 0.0);
        }
        if(!Parameter.EMPTY_STRING.equals(request.getParameter(Parameter.CLASS))){
            DrugClass drugClass = new DrugClass();
            drugClass.setName(request.getParameter(Parameter.CLASS));
            drug.setDrugClass(drugClass);
        }
        try {
            int manufactureId = Integer.parseInt(request.getParameter(Parameter.MANUFACTURER));
            DrugManufacturer drugManufacturer = new DrugManufacturer();
            drugManufacturer.setId(manufactureId);
            drug.setDrugManufacturer(drugManufacturer);
        }catch (NumberFormatException ignored){

        }
        try {
            boolean onlyInStock = Boolean.parseBoolean(request.getParameter(Parameter.ONLY_IN_STOCK));
            drug.setInStock(onlyInStock);
        }catch (NumberFormatException ignored){}
        try {
            boolean prescriptionEnable = Boolean.parseBoolean(request.getParameter(Parameter.ONLY_FREE));
            drug.setPrescriptionEnable(prescriptionEnable);
        }catch (NumberFormatException ex){
            drug.setPrescriptionEnable(true);
        }
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        DrugService drugService = serviceFactory.getDrugService();
        try {
            drugService.extendedDrugSearch(drug, 6, (pageNumber-1)*6);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
    }
}
