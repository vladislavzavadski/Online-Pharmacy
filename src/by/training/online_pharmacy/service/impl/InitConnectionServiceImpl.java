package by.training.online_pharmacy.service.impl;

import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.service.InitConnectionService;
import by.training.online_pharmacy.service.exception.InternalServerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by vladislav on 28.07.16.
 */
public class InitConnectionServiceImpl implements InitConnectionService {
    private static final Logger logger = LogManager.getRootLogger();
    @Override
    public void initConnection() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        try {
            connectionPool.initConnectionPool();
        } catch (ConnectionPoolException e) {
            logger.error("Something went wrong when trying to init connection", e);
            throw new InternalServerException(e);
        }
    }

    @Override
    public void destroyConnection() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        try {
            connectionPool.dispose();
        } catch (ConnectionPoolException e) {
            logger.error("Something went wrong when trying to destroy connection", e);
            throw new InternalServerException(e);
        }
    }
}
