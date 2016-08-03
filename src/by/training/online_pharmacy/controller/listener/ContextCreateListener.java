package by.training.online_pharmacy.controller.listener;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.controller.CommandHelper;
import by.training.online_pharmacy.controller.CommandName;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.ServiceFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by vladislav on 24.07.16.
 */
public class ContextCreateListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Command command = CommandHelper.getCommand(CommandName.INIT_CONNECTION);
        try {
            command.execute(null, null);
        } catch (ServletException|IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Command command = CommandHelper.getCommand(CommandName.DESTROY_CONNECTION);
        try {
            command.execute(null, null);
        } catch (ServletException|IOException e) {
            e.printStackTrace();
        }
    }
}
