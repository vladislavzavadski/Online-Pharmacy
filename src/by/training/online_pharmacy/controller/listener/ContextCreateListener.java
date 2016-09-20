package by.training.online_pharmacy.controller.listener;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.controller.CommandHelper;
import by.training.online_pharmacy.controller.CommandName;
import by.training.online_pharmacy.controller.listener.exception.InternalServerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by vladislav on 24.07.16.
 */
public class ContextCreateListener implements ServletContextListener {
    private final static Logger logger = LogManager.getLogger();
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        Command command = CommandHelper.getCommand(CommandName.INIT_CONNECTION);

        try {
            command.execute(null, null);
        } catch (ServletException|IOException e) {
            logger.error("Something went wrong when trying to init connection pool", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        Command command = CommandHelper.getCommand(CommandName.DESTROY_CONNECTION);
        final Logger logger = LogManager.getLogger();
        logger.error("Closed context!!!!!!!!!!!!!!!!!!!!!");
        try {
            command.execute(null, null);
        } catch (ServletException|IOException e) {
            logger.error("Something went wrong when trying to destroy connection pool", e);
            throw new InternalServerException(e);
        }
    }
}
