package by.training.online_pharmacy.dao.impl.database.util;


import by.training.online_pharmacy.dao.connection_pool.ConnectionPool;
import by.training.online_pharmacy.dao.connection_pool.exception.ConnectionPoolException;
import by.training.online_pharmacy.dao.impl.database.util.exception.ParameterNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by vladislav on 23.06.16.
 */
public class DatabaseOperation implements AutoCloseable {
    private static final String SQL_PARAMETER_REG_EXP = "[_a-z0-9]*(?=([\\s]*(=|like|<|>|<=|>=)[\\s]*\\?)|[\\s]*=[\\s]*md5[\\s]*\\([\\s]*\\?[\\s]*\\))|[_a-z0-9]+(?=\\s*(,[^\\(]*\\)|\\)))|limit(?=([\\s]*\\?[\\s]*,[\\s]*\\?))";
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private static ConnectionPool connectionPool;
    private List<String> params;

    public DatabaseOperation(String sqlQuery) throws ConnectionPoolException, SQLException {
        this();
        if((connection = connectionPool.takeReservedConnection())==null) {
            connection = connectionPool.reserveConnection();
        }
        init(sqlQuery);
    }

    private DatabaseOperation() throws ConnectionPoolException {
        if(connectionPool==null) {
            connectionPool = ConnectionPool.getInstance();
        }
    }

    public void init(String sqlQuery) throws ConnectionPoolException, SQLException {
        sqlQuery = sqlQuery.toLowerCase();
        params = getQueryParameters(sqlQuery);
        preparedStatement = connection.prepareStatement(sqlQuery);
    }

    public int invokeWriteOperation() throws SQLException {
        return preparedStatement.executeUpdate();
    }

    public ResultSet invokeReadOperation() throws SQLException {
        resultSet = preparedStatement.executeQuery();
        return resultSet;

    }

    private List<String> getQueryParameters(String query){
        List<String> params = new ArrayList<>();
        Pattern pattern = Pattern.compile(SQL_PARAMETER_REG_EXP);
        Matcher matcher = pattern.matcher(query);
        while (matcher.find()){
            int i=0;
            String subResult = matcher.group();
            if(subResult.isEmpty()){
                continue;
            }
            if(params.contains(subResult)){
                do{
                   i++;
                }while (params.contains(subResult+i));
                subResult+=i;
            }

            params.add(subResult);
        }
        return params;
    }

    public void setParameter(String name, Object value) throws ParameterNotFoundException, SQLException {
        name = name.toLowerCase();
        if(!params.contains(name)){
            throw new ParameterNotFoundException("Parameter \'"+name+"\' was not found in query");
        }
        int paramIndex = params.indexOf(name);
        preparedStatement.setObject(paramIndex+1, value);
    }

    public void setParameter(int paramNum, Object value) throws SQLException {
        preparedStatement.setObject(paramNum, value);
    }

    public void setParameter(String name, int number, Object value) throws ParameterNotFoundException, SQLException {
        name = name.toLowerCase();
        if(!params.contains(name)){
            throw new ParameterNotFoundException("Parameter \'"+name+"\' was not found in query");
        }
        int paramIndex = params.indexOf(name);
        preparedStatement.setObject(paramIndex+number, value);
    }

    public void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    public void endTransaction() throws SQLException {
        connection.commit();
    }

    public void rollBack() throws SQLException {
        connection.rollback();
    }

    @Override
    public void close() throws SQLException {

    }
}
