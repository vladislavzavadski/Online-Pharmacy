package by.training.online_pharmacy.dao.connection_pool;

import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import org.gjt.mm.mysql.*;
import org.gjt.mm.mysql.Driver;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladislav on 09.06.16.
 */
public class ConnectionPool {
    private final List<Connection> freeConnections = new LinkedList<>();
    private Map<Long, Connection> reservedConnections = new Hashtable<>();
    private String databaseURL;
    private String username;
    private String password;
    private String driver;
    private int poolSize;
    private String connectionMode;

    private static final ConnectionPool instance = new ConnectionPool();

    public static ConnectionPool getInstance(){
        return instance;
    }

    private ConnectionPool() {

        DBResourceManager dbResourceManager = DBResourceManager.getInstance();
        databaseURL = dbResourceManager.getProperty(DBParameter.DB_URL);
        username = dbResourceManager.getProperty(DBParameter.DB_USERNAME);
        password = dbResourceManager.getProperty(DBParameter.DB_PASSWORD);
        driver = dbResourceManager.getProperty(DBParameter.DB_DRIVER);
        connectionMode = dbResourceManager.getProperty(DBParameter.DB_MODE);

        try {
            poolSize = Integer.parseInt(dbResourceManager.getProperty(DBParameter.DB_POOL_SIZE));
        }

        catch (NumberFormatException ex){
            poolSize = 5;
        }

    }

    public void initConnectionPool() throws ConnectionPoolException{

        try {
            Class.forName(driver);

            for(int i=0; i<poolSize; i++) {
                Connection connection = DriverManager.getConnection(databaseURL, username, password);
                PooledConnection pooledConnection = new PooledConnection(connection);
                freeConnections.add(pooledConnection);
            }


        } catch (ClassNotFoundException e) {
            throw new ConnectionPoolException("Database driver was not found.", e);

        } catch (SQLException e) {
            throw new ConnectionPoolException("Can't create database connection", e);

        }

    }

    public Connection reserveConnection() throws ConnectionPoolException {
        Connection connection;

        synchronized (freeConnections){

            try {

                while (freeConnections.isEmpty()) {
                    freeConnections.wait();
                }

            } catch (InterruptedException e) {
                throw new ConnectionPoolException("Exception while trying to take new Connection");
            }

            connection = freeConnections.remove(0);
        }

        long threadId = Thread.currentThread().getId();
        reservedConnections.put(threadId, connection);

        return connection;
    }

    public void freeConnection() throws ConnectionPoolException {

        long threadId = Thread.currentThread().getId();
        Connection connection = reservedConnections.get(threadId);

        try {

            if(connection!=null) {
                connection.close();
            }

        } catch (SQLException e) {
            throw new ConnectionPoolException("Exception while trying to free connection", e);
        }
    }

    public Connection takeReservedConnection(){
        long threadId = Thread.currentThread().getId();
        return reservedConnections.get(threadId);
    }

    public void dispose() throws ConnectionPoolException {

        try {
            clearConnectionList();
        } catch (SQLException e) {
            throw new ConnectionPoolException("Can not destroy connection pool", e);

        }
    }

    private void clearConnectionList() throws SQLException {

        synchronized (freeConnections) {
            closeConnectionList(freeConnections);
        }

        closeConnectionsMap(reservedConnections);

    }

    private void closeConnectionList(List<Connection> connectionList) throws SQLException {

        while(!connectionList.isEmpty()){
            Connection connection = connectionList.remove(0);

            if(!connection.getAutoCommit()){
                connection.commit();
            }

            ((PooledConnection)connection).reallyClose();
        }
    }

    private void closeConnectionsMap(Map<Long, Connection> map) throws SQLException {

        for(Map.Entry<Long, Connection> entry:map.entrySet()){

            Connection connection = entry.getValue();

            if(!connection.getAutoCommit()){
                connection.commit();
            }

            ((PooledConnection)connection).reallyClose();
        }

        map.clear();
    }

    private class PooledConnection implements Connection{
        private Connection connection;

        public PooledConnection(Connection connection) {
            this.connection = connection;
        }

        public void reallyClose() throws SQLException {
            connection.close();
        }

        @Override
        public void close() throws SQLException {

            if(connection.isClosed()) {
                throw new SQLException("Given connection already closed "+reservedConnections.size()+" "+freeConnections.size());
            }

            if(connection.isReadOnly()) {
                connection.setReadOnly(false);
            }

            if(!connection.getAutoCommit()){
                connection.rollback();
                connection.setAutoCommit(true);
            }


            long threadId = Thread.currentThread().getId();

            if (!reservedConnections.remove(threadId, this)) {
                throw new SQLException("Error deleting connection from used connections pool");
            }

            synchronized (freeConnections) {

                if (!freeConnections.add(this)) {
                    throw new SQLException("Error adding connection to free connections pool");
                }

                freeConnections.notify();
            }
        }

        @Override
        public Statement createStatement() throws SQLException {
            return connection.createStatement();
        }

        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return connection.prepareStatement(sql);
        }

        @Override
        public CallableStatement prepareCall(String sql) throws SQLException {
            return connection.prepareCall(sql);
        }

        @Override
        public String nativeSQL(String sql) throws SQLException {
            return connection.nativeSQL(sql);
        }

        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            connection.setAutoCommit(autoCommit);
        }

        @Override
        public boolean getAutoCommit() throws SQLException {
            return connection.getAutoCommit();
        }

        @Override
        public void commit() throws SQLException {

            if(!connectionMode.equals(DBParameter.DB_TEST)) {
                connection.commit();
            }
        }

        @Override
        public void rollback() throws SQLException {
            connection.rollback();
        }



        @Override
        public boolean isClosed() throws SQLException {
            return connection.isClosed();
        }

        @Override
        public DatabaseMetaData getMetaData() throws SQLException {
            return connection.getMetaData();
        }

        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {
            connection.setReadOnly(readOnly);
        }

        @Override
        public boolean isReadOnly() throws SQLException {
            return connection.isReadOnly();
        }

        @Override
        public void setCatalog(String catalog) throws SQLException {
            connection.setCatalog(catalog);
        }

        @Override
        public String getCatalog() throws SQLException {
            return connection.getCatalog();
        }

        @Override
        public void setTransactionIsolation(int level) throws SQLException {
            connection.setTransactionIsolation(level);
        }

        @Override
        public int getTransactionIsolation() throws SQLException {
            return connection.getTransactionIsolation();
        }

        @Override
        public SQLWarning getWarnings() throws SQLException {
            return connection.getWarnings();
        }

        @Override
        public void clearWarnings() throws SQLException {
            connection.clearWarnings();
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.createStatement(resultSetType, resultSetConcurrency);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return connection.getTypeMap();
        }

        @Override
        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            connection.setTypeMap(map);
        }

        @Override
        public void setHoldability(int holdability) throws SQLException {
            connection.setHoldability(holdability);
        }

        @Override
        public int getHoldability() throws SQLException {
            return connection.getHoldability();
        }

        @Override
        public Savepoint setSavepoint() throws SQLException {
            return connection.setSavepoint();
        }

        @Override
        public Savepoint setSavepoint(String name) throws SQLException {
            return connection.setSavepoint(name);
        }

        @Override
        public void rollback(Savepoint savepoint) throws SQLException {
            connection.rollback(savepoint);
        }

        @Override
        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            connection.releaseSavepoint(savepoint);
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return connection.prepareStatement(sql, autoGeneratedKeys);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return connection.prepareStatement(sql, columnIndexes);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return connection.prepareStatement(sql, columnNames);
        }

        @Override
        public Clob createClob() throws SQLException {
            return connection.createClob();
        }

        @Override
        public Blob createBlob() throws SQLException {
            return connection.createBlob();
        }

        @Override
        public NClob createNClob() throws SQLException {
            return connection.createNClob();
        }

        @Override
        public SQLXML createSQLXML() throws SQLException {
            return connection.createSQLXML();
        }

        @Override
        public boolean isValid(int timeout) throws SQLException {
            return connection.isValid(timeout);
        }

        @Override
        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            connection.setClientInfo(name, value);
        }

        @Override
        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            connection.setClientInfo(properties);
        }

        @Override
        public String getClientInfo(String name) throws SQLException {
            return connection.getClientInfo(name);
        }

        @Override
        public Properties getClientInfo() throws SQLException {
            return connection.getClientInfo();
        }

        @Override
        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return connection.createArrayOf(typeName, elements);
        }

        @Override
        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return connection.createStruct(typeName, attributes);
        }

        @Override
        public void setSchema(String schema) throws SQLException {
            connection.setSchema(schema);
        }

        @Override
        public String getSchema() throws SQLException {
            return connection.getSchema();
        }

        @Override
        public void abort(Executor executor) throws SQLException {
            connection.abort(executor);
        }

        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            connection.setNetworkTimeout(executor, milliseconds);
        }

        @Override
        public int getNetworkTimeout() throws SQLException {
            return connection.getNetworkTimeout();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return connection.unwrap(iface);
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return connection.isWrapperFor(iface);
        }
    }


}
