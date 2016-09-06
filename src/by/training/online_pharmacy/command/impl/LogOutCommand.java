package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by vladislav on 01.08.16.
 */
public class LogOutCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if(session!=null){
            session.invalidate();
        }
        response.sendRedirect(Page.INDEX);
    }
}
