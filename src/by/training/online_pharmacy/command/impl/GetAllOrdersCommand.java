package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.order.Order;
import by.training.online_pharmacy.domain.order.SearchOrderCriteria;
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
import java.util.List;

/**
 * Created by vladislav on 15.08.16.
 */
public class GetAllOrdersCommand implements Command {

    private static final int LIMIT = 6;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;

        if(httpSession==null||(user=(User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        boolean pageOverload = Boolean.parseBoolean(request.getParameter(Parameter.OVERLOAD));
        int page = Integer.parseInt(request.getParameter(Parameter.PAGE));

        SearchOrderCriteria searchOrderCriteria = new SearchOrderCriteria();
        searchOrderCriteria.setDateFrom(request.getParameter(Parameter.DATE_FROM));
        searchOrderCriteria.setDateTo(request.getParameter(Parameter.DATE_TO));
        searchOrderCriteria.setDrugName(request.getParameter(Parameter.DRUG_NAME));
        searchOrderCriteria.setOrderStatus(request.getParameter(Parameter.ORDER_STATUS));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        OrderService orderService = serviceFactory.getOrderService();

        try {
            List<Order> orderList = orderService.getAllUsersOrders(user, searchOrderCriteria, LIMIT, (page - 1) * LIMIT);
            request.setAttribute(Parameter.ORDER_LIST, orderList);

            if(page==1&&pageOverload) {
                request.getRequestDispatcher(Page.ORDERS).forward(request, response);
            }
            else {
                request.getRequestDispatcher(Page.ORDER).forward(request, response);
            }

        } catch (InvalidParameterException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed parameters is invalid");

            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType(Content.JSON);
            servletOutputStream.write(jsonObject.toString().getBytes());
        } catch (InvalidUserStatusException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "Doctors can not view orders");

            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType(Content.JSON);
            servletOutputStream.write(jsonObject.toString().getBytes());

        } finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();
        }

    }
}
