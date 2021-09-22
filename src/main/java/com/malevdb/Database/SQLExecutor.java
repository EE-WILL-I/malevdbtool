package com.malevdb.Database;

import com.malevdb.Application.Logger;
import com.malevdb.SpringConfigurations.SQLExecutorConfiguration;
import com.malevdb.Utils.FileResourcesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.sql.*;

public class SQLExecutor {
    public final String SQL_RESOURCE_PATH;
    private final Connection connection;
    private static AnnotationConfigApplicationContext sqlExecutorContext;
    private String lastLoadedResource;

    @Autowired
    public SQLExecutor(Connection connection, String sqlPath) {
        this.connection = connection;
        SQL_RESOURCE_PATH = sqlPath;
    }

    public static SQLExecutor getInstance() {
        if(sqlExecutorContext == null)
            sqlExecutorContext  = new AnnotationConfigApplicationContext(SQLExecutorConfiguration.class);
        return sqlExecutorContext.getBean(SQLExecutor.class);
    }

    public PreparedStatement prepareStatement(String query, String... args) throws SQLException, IllegalArgumentException {
        if(query.isEmpty())
            throw new IllegalArgumentException("Empty SQL query");
        for(int i = 0; i < args.length; i++) {
            if(query.contains("@a" + i))
             query = query.replace("@a" + i, args[i]);
            else
                throw new IllegalArgumentException(lastLoadedResource + " don't receives " + i + " parameter(s)");
        }
        query = query.replaceAll("'@a(\\w*)'", "null");
        return connection.prepareStatement(query);
    }

    public ResultSet executeSelect(String query, String... args) {
        ResultSet resultSet;
        try {
            PreparedStatement statement = prepareStatement(query, args);
            resultSet = executeSelect(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    public ResultSet executeSelect(PreparedStatement statement, String... args) {
        ResultSet resultSet;
        try {
            if(args.length > 0)
                statement = prepareStatement(statement.toString(), args);
            logBeforeExecution();
            resultSet = statement.executeQuery();
            logAfterExecution(true);
        } catch (SQLException e) {
            e.printStackTrace();
            logAfterExecution(false);
            return null;
        }
        return resultSet;
    }

    public boolean executeUpdate(String query, String... args) {
        try {
            PreparedStatement statement = prepareStatement(query, args);
            return executeUpdate(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean executeUpdate(PreparedStatement statement, String... args) {
        try {
            if(args.length > 0)
                statement = prepareStatement(statement.toString(), args);
            logBeforeExecution();
            statement.executeUpdate();
            logAfterExecution(true);
        } catch (SQLException e) {
            logAfterExecution(false);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String loadSQLResource(String resourceName) {
        try {
            lastLoadedResource = resourceName;
            return FileResourcesUtils.getFileDataAsString(SQL_RESOURCE_PATH + resourceName);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void logBeforeExecution() {
        Logger.Log(this, "Executing query: " + lastLoadedResource);
    }

    private void logAfterExecution(boolean successful) {
        if(successful)
            Logger.Log(this, "Query executed");
        else
            Logger.Log(this,"Query execution failed");
    }
}
