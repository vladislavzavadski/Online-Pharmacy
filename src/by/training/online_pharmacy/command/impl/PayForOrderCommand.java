package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.OrderService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.AmbiguousValueException;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.OrderNotFoundException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 28.08.16.
 */
public class PayForOrderCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;
        if(httpSession==null||(user=(User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }
        JSONObject jsonObject = new JSONObject();
        response.setContentType(Content.JSON);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        int orderId = Integer.parseInt(request.getParameter(Parameter.ORDER_ID));
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        OrderService orderService = serviceFactory.getOrderService();
        try {
            orderService.payForOrder(user, orderId);
            jsonObject.put(Parameter.RESULT, true);
        } catch (InvalidParameterException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, e.getMessage());
        } catch (OrderNotFoundException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, Message.ORDER_NOT_FOUND);
        } catch (AmbiguousValueException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, Message.INSUF_FUNDS);
        }
        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
