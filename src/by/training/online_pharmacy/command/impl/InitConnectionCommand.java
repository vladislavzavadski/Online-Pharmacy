package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vladislav on 01.08.16.
 */
public class InitConnectionCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)  {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
        initConnectionService.initConnection();
    }
}
