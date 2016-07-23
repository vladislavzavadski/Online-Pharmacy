package by.training.online_pharmacy.controller;

import by.training.online_pharmacy.domain.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by vladislav on 23.07.16.
 */
public class GetProfileImageCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession;
        if((httpSession=request.getSession(false))!=null){
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            User user = (User)httpSession.getAttribute("user");
            response.setContentLength(user.getUserImage().length);
            outputStream.write(user.getUserImage());
            outputStream.close();
        }
    }
}
