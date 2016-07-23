package by.training.online_pharmacy.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.MarshalledObject;
import java.util.Map;

/**
 * Created by vladislav on 18.07.16.
 */
public class Controller extends javax.servlet.http.HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response){
        CommandName commandName = CommandName.valueOf(request.getParameter("command"));
        Command command = CommandHelper.getCommand(commandName);
        try {
            command.execute(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }//TODO:Исправить обязательно
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
