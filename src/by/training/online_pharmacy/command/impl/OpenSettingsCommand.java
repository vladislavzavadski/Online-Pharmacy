package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.SecretQuestion;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.SecretQuestionService;
import by.training.online_pharmacy.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by vladislav on 04.09.16.
 */
public class OpenSettingsCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;
        if(httpSession==null||(user=(User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }
        if(user.getRegistrationType()== RegistrationType.NATIVE){
            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            SecretQuestionService secretQuestionService = serviceFactory.getSecretQuestionService();
            List<SecretQuestion> secretQuestions = secretQuestionService.getAllSecretQuestions();
            request.setAttribute("secretQuestions", secretQuestions);
        }
        request.getRequestDispatcher("/settings").forward(request, response);
    }
}
