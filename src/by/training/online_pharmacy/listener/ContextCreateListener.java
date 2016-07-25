package by.training.online_pharmacy.listener;

import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by vladislav on 24.07.16.
 */
public class ContextCreateListener implements ServletContextListener {
    private ConnectionPool connectionPool;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            connectionPool = ConnectionPool.getInstance();
            connectionPool.initConnectionPool();
        } catch (ConnectionPoolException e) {
            throw new RuntimeException("Something went wrong, while trying to init connection pool.", e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        connectionPool.dispose();
    }
}
