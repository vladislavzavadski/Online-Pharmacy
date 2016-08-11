package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.drug.Drug;
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
 * Created by vladislav on 08.08.16.
 */
public class SearchDrugsCommand implements Command {
    private static final int DRUGS_ON_PAGE = 6;
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        if(httpSession==null||httpSession.getAttribute(Parameter.USER)==null){
            response.sendRedirect(Page.INDEX);
            return;
        }
        String query = request.getParameter(Parameter.QUERY);
        int pageNumber = Integer.parseInt(request.getParameter(Parameter.PAGE));
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        DrugService drugService = serviceFactory.getDrugService();
        try {
            List<Drug> drugList = drugService.searchDrugs(query, DRUGS_ON_PAGE, (pageNumber-1)*DRUGS_ON_PAGE);
            request.setAttribute("drugList", drugList);
            request.getRequestDispatcher("/drug").forward(request, response);

        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
    }
}
