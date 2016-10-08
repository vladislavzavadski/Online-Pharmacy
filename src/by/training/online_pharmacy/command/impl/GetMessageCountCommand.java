package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.message.MessageStatus;
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

/**
 * Created by vladislav on 22.08.16.
 */
public class GetMessageCountCommand implements Command {

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

        MessageStatus messageStatus = MessageStatus.valueOf(request.getParameter(Parameter.MESSAGE_STATUS));

        JSONObject jsonObject = new JSONObject();
        ServletOutputStream servletOutputStream = response.getOutputStream();
        response.setContentType(Content.JSON);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MessageService messageService = serviceFactory.getMessageService();

        try {
            int number = messageService.getMessageCount(user, messageStatus);
            jsonObject.put(Parameter.RESULT, true);
            jsonObject.put(Parameter.MESSAGES_COUNT, number);

            httpSession.setAttribute(Parameter.MESSAGES_COUNT, number);

        } catch (InvalidParameterException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of passed parameters is invalid");

        }
        finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();

        }

        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
