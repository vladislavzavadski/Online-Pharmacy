package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.dao.impl.database.Param;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.OrderService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.AmbiguousValueException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;
import by.training.online_pharmacy.service.exception.PrescriptionNotFoundException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 10.08.16.
 */
public class CreateOrderCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;
        if(httpSession==null||(user = (User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }
        JSONObject jsonObject = new JSONObject();
        response.setContentType(Content.JSON);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        Order order = new Order();
        order.setClient(user);
        order.setDrugCount(Integer.parseInt(request.getParameter(Parameter.DRUG_COUNT)));
        order.setDrugDosage(Short.parseShort(request.getParameter(Parameter.DRUG_DOSAGE)));
        Drug drug = new Drug();
        try {
            drug.setId(Integer.parseInt(request.getParameter(Parameter.DRUG_ID)));
        }catch (NumberFormatException ex){
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.IS_CRITICAL, false);
            jsonObject.put(Parameter.MESSAGE, ex.getMessage());
            servletOutputStream.write(jsonObject.toString().getBytes());
            return;
        }

        order.setDrug(drug);
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        OrderService orderService = serviceFactory.getOrderService();

        try {
            orderService.createOrder(order);
            jsonObject.put(Parameter.RESULT, true);
        } catch (InvalidParameterException | AmbiguousValueException | PrescriptionNotFoundException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.IS_CRITICAL, false);
            jsonObject.put(Parameter.MESSAGE, e.getMessage());
        } catch (NotFoundException e) {
            if(e.isCritical()){
                httpSession.invalidate();
            }
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.IS_CRITICAL, e.isCritical());
            jsonObject.put(Parameter.MESSAGE, e.getMessage());
        }

        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
