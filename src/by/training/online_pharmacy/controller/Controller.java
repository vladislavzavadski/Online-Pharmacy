package by.training.online_pharmacy.controller;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.command.impl.Parameter;
import by.training.online_pharmacy.controller.listener.exception.InternalServerException;
import by.training.online_pharmacy.dao.UserDAO;
import by.training.online_pharmacy.dao.exception.DaoException;
import by.training.online_pharmacy.dao.impl.database.DatabaseUserDAO;
import by.training.online_pharmacy.service.util.ImageConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by vladislav on 18.07.16.
 */
public class Controller extends javax.servlet.http.HttpServlet {


    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        CommandName commandName = CommandName.valueOf(request.getParameter(Parameter.COMMAND));
        Command command = CommandHelper.getCommand(commandName);
        command.execute(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
