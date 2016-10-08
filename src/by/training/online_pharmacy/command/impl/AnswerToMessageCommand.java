package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.domain.message.Message;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.MessageService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.InvalidUserStatusException;
import by.training.online_pharmacy.service.exception.MessageNotFoundException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 09.09.16.
 */
public class AnswerToMessageCommand implements Command {

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

        Message message = new Message();
        message.setReceiverMessage(request.getParameter(Parameter.RECEIVER_MESSAGE));
        message.setId(Integer.parseInt(request.getParameter(Parameter.MESSAGE_ID)));
        message.setReceiver(user);

        JSONObject jsonObject = new JSONObject();
        ServletOutputStream servletOutputStream = response.getOutputStream();
        response.setContentType(Content.JSON);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MessageService messageService = serviceFactory.getMessageService();

        try {
            messageService.sendResponseToMessage(message);
            jsonObject.put(Parameter.RESULT, true);
        } catch (InvalidParameterException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "One of the parameter is invalid");
        } catch (InvalidUserStatusException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "Only doctor can answer to message");
        } catch (MessageNotFoundException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, "Message was not found or you are not message receiver");
        } finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();
        }

        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
