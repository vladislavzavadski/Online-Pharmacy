package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.message.Message;
import by.training.online_pharmacy.domain.message.SearchMessageCriteria;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.MessageService;
import by.training.online_pharmacy.service.ServiceFactory;
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
 * Created by vladislav on 18.08.16.
 */
public class GetAllMessagesCommand implements Command {
    private static final int LIMIT = 6;
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;

        if(httpSession==null||(user = (User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        String dateTo = request.getParameter(Parameter.DATE_TO);
        String dateFrom = request.getParameter(Parameter.DATE_FROM);
        String messageStatus = request.getParameter(Parameter.MESSAGE_STATUS);
        boolean pageOverload = Boolean.parseBoolean(request.getParameter(Parameter.OVERLOAD));
        int page = Integer.parseInt(request.getParameter(Parameter.PAGE));

        SearchMessageCriteria searchMessageCriteria = new SearchMessageCriteria();
        searchMessageCriteria.setMessageStatus(messageStatus);
        searchMessageCriteria.setDateTo(dateTo);
        searchMessageCriteria.setDateFrom(dateFrom);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MessageService messageService = serviceFactory.getMessageService();

        try {
            List<Message> messages = messageService.getMessages(user, searchMessageCriteria, (page-1)*LIMIT, LIMIT);
            request.setAttribute(Parameter.MESSAGE_LIST, messages);

            if(page==1&&pageOverload){
                request.getRequestDispatcher(Page.MESSAGES).forward(request, response);
            }
            else {
                request.getRequestDispatcher(Page.MESSAGE).forward(request, response);
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
