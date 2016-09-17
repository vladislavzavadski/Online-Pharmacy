package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.drug.DrugClass;
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

/**
 * Created by vladislav on 08.08.16.
 */
public class SearchDrugsCommand implements Command {
    private static final int DRUGS_ON_PAGE = 6;
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;

        if(httpSession==null||(user = (User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        String query = request.getParameter(Parameter.QUERY);
        int pageNumber = Integer.parseInt(request.getParameter(Parameter.PAGE));
        boolean pageOverload = Boolean.parseBoolean(request.getParameter(Parameter.OVERLOAD));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        DrugService drugService = serviceFactory.getDrugService();

        try {
            List<Drug> drugList = drugService.searchDrugs(query, DRUGS_ON_PAGE, (pageNumber-1)*DRUGS_ON_PAGE);
            request.setAttribute(Parameter.DRUG_LIST, drugList);

            if (pageOverload){
                List<DrugClass> drugClasses = drugService.getAllDrugClasses();

                request.setAttribute(Parameter.DRUG_CLASSES, drugClasses);

                if(user.getUserRole() == UserRole.PHARMACIST){
                    UserService userService = serviceFactory.getUserService();

                    List<UserDescription> userDescriptions = userService.getAllSpecializations();

                    request.setAttribute(Parameter.SPECIALIZATIONS, userDescriptions);
                }

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
