package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.DrugClass;
import by.training.online_pharmacy.domain.drug.DrugManufacturer;
import by.training.online_pharmacy.domain.drug.SearchDrugsCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.user.UserDescription;
import by.training.online_pharmacy.domain.user.UserRole;
import by.training.online_pharmacy.service.DrugService;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static by.training.online_pharmacy.command.impl.Parameter.COMMA;

/**
 * Created by vladislav on 09.08.16.
 */
public class ExtendedSearchCommand implements Command {
    private static final int LIMIT = 6;

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

        int pageNumber = Integer.parseInt(request.getParameter(Parameter.PAGE));
        boolean pageOverload = Boolean.parseBoolean(request.getParameter(Parameter.OVERLOAD));

        SearchDrugsCriteria searchDrugsCriteria = new SearchDrugsCriteria();

        searchDrugsCriteria.setName(request.getParameter(Parameter.NAME));
        searchDrugsCriteria.setActiveSubstance(request.getParameter(Parameter.ACTIVE_SUBSTANCE));
        searchDrugsCriteria.setDrugMaxPrice(request.getParameter(Parameter.MAX_PRICE));
        searchDrugsCriteria.setDrugClass(request.getParameter(Parameter.CLASS));

        String drugMan = request.getParameter(Parameter.MANUFACTURER);

        if(drugMan!=null && !drugMan.isEmpty()){
            String[] nameCountry = drugMan.split(COMMA);

            if(nameCountry.length==2){
                DrugManufacturer drugManufacturer = new DrugManufacturer();
                drugManufacturer.setName(nameCountry[0]);
                drugManufacturer.setCountry(nameCountry[1]);

                searchDrugsCriteria.setDrugManufacture(drugManufacturer);
            }
        }

        searchDrugsCriteria.setOnlyInStock(request.getParameter(Parameter.ONLY_IN_STOCK));
        searchDrugsCriteria.setPrescriptionEnable(request.getParameter(Parameter.ONLY_FREE));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        DrugService drugService = serviceFactory.getDrugService();

        try {
            List<Drug> drugs = drugService.searchDrugs(searchDrugsCriteria,  LIMIT, (pageNumber-1)*LIMIT);
            request.setAttribute(Parameter.DRUG_LIST, drugs);

            if(pageOverload){
                List<DrugClass> drugClasses = drugService.getAllDrugClasses();

                request.setAttribute(Parameter.DRUG_CLASSES, drugClasses);

                if(user.getUserRole() == UserRole.PHARMACIST){
                    UserService userService = serviceFactory.getUserService();

                    List<UserDescription> userDescriptions = userService.getAllSpecializations();

                    request.setAttribute(Parameter.SPECIALIZATIONS, userDescriptions);

                }

                List<DrugManufacturer> drugManufacturers = drugService.getDrugManufactures();

                request.setAttribute(Parameter.DRUG_MANUFACTURES, drugManufacturers);

                request.getRequestDispatcher(Page.DRUGS).forward(request, response);
            }
            else {
                request.getRequestDispatcher(Page.DRUG).forward(request, response);
            }

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
