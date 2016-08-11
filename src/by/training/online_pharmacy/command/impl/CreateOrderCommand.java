package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.drug.Drug;
import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.OrderService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.AmbiguousValueException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;

import javax.servlet.ServletException;
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
        Order order = new Order();
        order.setClient(user);
        order.setDrugCount(Integer.parseInt(request.getParameter(Parameter.DRUG_COUNT)));
        order.setDrugDosage(Short.parseShort(request.getParameter(Parameter.DRUG_DOSAGE)));
        Drug drug = new Drug();
        drug.setId(Integer.parseInt(request.getParameter(Parameter.DRUG_ID)));
        order.setDrug(drug);
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        OrderService orderService = serviceFactory.getOrderService();
        try {
            orderService.createOrder(order);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (AmbiguousValueException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            if(e.isCritical()){
                httpSession.invalidate();
            }
        }
    }
}
