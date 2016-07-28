package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 25.07.16.
 */
public class SwitchLocaleCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("language", request.getParameter("language"));
        String prevRequest = (String)httpSession.getAttribute("prevRequest");
        if(prevRequest==null){
            response.sendRedirect("/index.jsp");
        }else {
            response.sendRedirect(prevRequest);
        }

    }
}
