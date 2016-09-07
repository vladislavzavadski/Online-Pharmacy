package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.domain.user.SecretQuestion;
import by.training.online_pharmacy.domain.user.SecretWord;
import by.training.online_pharmacy.domain.user.User;
import by.training.online_pharmacy.service.ServiceFactory;
import by.training.online_pharmacy.service.UserService;
import by.training.online_pharmacy.service.exception.InvalidParameterException;
import by.training.online_pharmacy.service.exception.InvalidUserStatusException;
import by.training.online_pharmacy.service.exception.NotFoundException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 04.09.16.
 */
public class CreateSecretWordCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        User user;
        if(httpSession==null||(user=(User)httpSession.getAttribute(Parameter.USER))==null){
            response.sendRedirect(Page.INDEX);
            return;
        }
        int questionId = Integer.parseInt(request.getParameter(Parameter.QUESTION_ID));

        String secretResponse = request.getParameter(Parameter.SECRET_WORD);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();

        SecretWord secretWord = new SecretWord();
        SecretQuestion secretQuestion = new SecretQuestion();
        secretQuestion.setId(questionId);
        secretWord.setSecretQuestion(secretQuestion);
        secretWord.setResponse(secretResponse);
        secretWord.setUser(user);
        JSONObject jsonObject = new JSONObject();
        ServletOutputStream servletOutputStream = response.getOutputStream();
        response.setContentType(Content.JSON);
        try {
            userService.createSecretWord(secretWord);
            jsonObject.put(Parameter.RESULT, true);
        } catch (InvalidParameterException | InvalidUserStatusException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.MESSAGE, e.getMessage());
        } catch (NotFoundException e) {
            jsonObject.put(Parameter.RESULT, false);
            jsonObject.put(Parameter.IS_CRITICAL, e.isCritical());
            jsonObject.put(Parameter.MESSAGE, e.getMessage());
            if(e.isCritical()){
                httpSession.invalidate();
            }
        }
        servletOutputStream.write(jsonObject.toString().getBytes());
    }
}
