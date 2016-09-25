package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.OrderService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.InvalidUserStatusException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by vladislav on 17.09.16.
 */
public class GetOrderByIdCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;

        if(httpSession==null||(user=(User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        int orderId = Integer.parseInt(request.getParameter(Parameter.ORDER_ID));
        boolean pageOverload = Boolean.parseBoolean(request.getParameter(Parameter.OVERLOAD));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        OrderService orderService = serviceFactory.getOrderService();

        try {
            Order order = orderService.getOrderById(user, orderId);
            request.setAttribute(Parameter.ORDER_LIST, Collections.singletonList(order));

            if(pageOverload){
                request.getRequestDispatcher(Page.ORDERS).forward(request, response);
            }else {
                request.getRequestDispatcher(Page.ORDER).forward(request, response);
            }

        } catch (InvalidUserStatusException e) {
            JSONObject jsonObject = new JSONObject();
            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType(Content.JSON);

            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed parameters is invalid");

            servletOutputStream.write(jsonObject.toString().getBytes());

        } catch (InvalidParameterException e) {
            JSONObject jsonObject = new JSONObject();
            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType(Content.JSON);

            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "Only pharmacist can get orders by id");

            servletOutputStream.write(jsonObject.toString().getBytes());

        }
        finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();

        }
    }
}
