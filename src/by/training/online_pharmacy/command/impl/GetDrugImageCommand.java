package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.service.DrugService;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.util.ImageConstant;
import org.apache.commons.compress.utils.IOUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vladislav on 16.08.16.
 */
public class GetDrugImageCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);

        if(httpSession==null||httpSession.getAttribute(Parameter.USER)==null){
            response.sendRedirect(Page.INDEX);
            return;
        }


        int drugId = Integer.parseInt(request.getParameter(Parameter.DRUG_ID));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        DrugService drugService = serviceFactory.getDrugService();

        try {
            InputStream inputStream = drugService.getDrugImage(drugId, request.getServletContext().getRealPath(ImageConstant.DRUG_IMAGES));

            ServletOutputStream servletOutputStream = response.getOutputStream();

            IOUtils.copy(inputStream, servletOutputStream);
            inputStream.close();

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
