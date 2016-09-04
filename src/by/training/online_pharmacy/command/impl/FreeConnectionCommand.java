package by.training.online_pharmacy.command.impl;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.controller.CommandName;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.training.online_pharmacy.controller.CommandName.GET_PROFILE_IMAGE;

/**
 * Created by vladislav on 03.09.16.
 */
public class FreeConnectionCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isConnectionEnable = CommandName.valueOf(request.getParameter(Parameter.COMMAND))!=GET_PROFILE_IMAGE;
        if(isConnectionEnable) {
            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
            initConnectionService.freeConnection();
        }
    }
}
