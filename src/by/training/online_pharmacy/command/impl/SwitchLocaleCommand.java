package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by vladislav on 25.07.16.
 */
public class SwitchLocaleCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();

        Locale locale = new Locale(request.getParameter(Parameter.LANGUAGE));

        httpSession.setAttribute(Parameter.LANGUAGE, locale);

        String redirectUrl = request.getParameter(Parameter.REDIRECT_URL);

        response.sendRedirect(redirectUrl);

    }
}
