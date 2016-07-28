package by.training.online_pharmacy.listener;

import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.ServiceFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by vladislav on 24.07.16.
 */
public class ContextCreateListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
        initConnectionService.initConnection();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        InitConnectionService initConnectionService = serviceFactory.getInitConnectionService();
        initConnectionService.destroyConnection();
    }
}
