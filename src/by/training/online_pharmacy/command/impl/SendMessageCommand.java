package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.message.Message;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.MessageService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.NotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 11.08.16.
 */
public class SendMessageCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;
        if(httpSession==null||(user = (User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }
        Message message = new Message();
        message.setSender(user);
        User receiver = new User();
        message.setReceiver(receiver);
        receiver.setLogin(request.getParameter(Parameter.RECEIVER_LOGIN));
        receiver.setRegistrationType(RegistrationType.valueOf(Parameter.RECEIVER_LOGIN_VIA));
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MessageService messageService = serviceFactory.getMessageService();
        try {
            messageService.sendMessage(message);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            if(e.isCritical()){
                httpSession.invalidate();
            }
        }
    }
}
