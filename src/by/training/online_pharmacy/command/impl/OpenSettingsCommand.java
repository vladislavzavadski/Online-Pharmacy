package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.RegistrationType;
import by.training.online_pharmacy.domain.user.SecretQuestion;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.InitConnectionService;
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

        if(httpSession==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        user = (User)httpSession.getAttribute(Parameter.USER);

        if(user==null){
            response.sendRedirect(Page.INDEX);
            return;
        }

        if(user.getRegistrationType()== RegistrationType.NATIVE){
            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            SecretQuestionService secretQuestionService = serviceFactory.getSecretQuestionService();

            try {
                List<SecretQuestion> secretQuestions = secretQuestionService.getAllSecretQuestions();
                request.setAttribute(Parameter.SECRET_QUESTIONS, secretQuestions);

            }
            finally {
                InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
                initConnectionService.freeConnection();

            }
        }

        request.getRequestDispatcher(Page.SETTINGS).forward(request, response);
    }
}
