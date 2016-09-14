package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.SecretQuestion;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.Encoding;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.SecretQuestionService;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by vladislav on 05.09.16.
 */
public class GetUsersSecretQuestionCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        user.setLogin(request.getParameter(Parameter.LOGIN));

        JSONObject jsonObject = new JSONObject();
        ServletOutputStream servletOutputStream = response.getOutputStream();

        response.setContentType(Content.JSON);
        response.setCharacterEncoding(Encoding.UTF8);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        SecretQuestionService secretQuestionService = serviceFactory.getSecretQuestionService();

        try {
            SecretQuestion secretQuestion = secretQuestionService.getUsersSecretQuestion(user);
            jsonObject.put(Parameter.RESULT, true);

            if(secretQuestion!=null) {
                jsonObject.put(Parameter.QUESTION, secretQuestion.getQuestion());
            }else {
                jsonObject.put(Parameter.QUESTION, Parameter.EMPTY_STRING);
            }
        } catch (InvalidParameterException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.RESULT, "One of passed parameters is inlaid");

        }
        finally {
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();

        }
        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
