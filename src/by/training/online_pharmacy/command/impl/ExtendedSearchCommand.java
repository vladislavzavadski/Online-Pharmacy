package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.DrugClass;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;
import by.training.online_pharmacy.domain.drug.SearchDrugsCriteria;
import by.training.online_pharmacy.service.DrugService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.InvalidParameterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by vladislav on 09.08.16.
 */
public class ExtendedSearchCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        if(httpSession==null||httpSession.getAttribute(Parameter.USER)==null){
            response.sendRedirect(Page.INDEX);
            return;
        }
        int pageNumber = Integer.parseInt(request.getParameter(Parameter.PAGE));
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        DrugService drugService = serviceFactory.getDrugService();
        SearchDrugsCriteria searchDrugsCriteria = new SearchDrugsCriteria();
        searchDrugsCriteria.setName(request.getParameter(Parameter.NAME));
        searchDrugsCriteria.setActiveSubstance(request.getParameter(Parameter.ACTIVE_SUBSTANCE));
        searchDrugsCriteria.setDrugMaxPrice(request.getParameter(Parameter.MAX_PRICE));
        searchDrugsCriteria.setDrugClass(request.getParameter(Parameter.CLASS));
        searchDrugsCriteria.setDrugManufacture(request.getParameter(Parameter.MANUFACTURER));
        searchDrugsCriteria.setOnlyInStock(request.getParameter(Parameter.ONLY_IN_STOCK));
        searchDrugsCriteria.setPrescriptionEnable(request.getParameter(Parameter.ONLY_FREE));

        try {
            List<Drug> drugs = drugService.extendedDrugSearch(searchDrugsCriteria,  6, (pageNumber-1)*6);
            request.setAttribute(Parameter.DRUG_LIST, drugs);
            request.getRequestDispatcher(Page.DRUG).forward(request, response);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
    }
}