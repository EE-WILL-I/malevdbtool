package com.malevdb.Database;

import com.malevdb.Application.Logging.Logger;
import com.malevdb.SpringConfigurations.SQLExecutorConfiguration;
import com.malevdb.Utils.FileResourcesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SQLExecutor {
    public final String SQL_RESOURCE_PATH;
    private final Connection connection;
    private static AnnotationConfigApplicationContext sqlExecutorContext;
    private String lastLoadedResource;
    private ArrayList<String> argumentConstants = new ArrayList<>(Arrays.asList("null","default"));

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
        if(query == null || query.isEmpty())
            throw new IllegalArgumentException("Empty SQL query");
        query = insertArgs(query, args);
        return connection.prepareStatement(query);
    }

    public String insertArgs(String query, String[] args) { return  insertArgs(query, args, 0); }

    public String insertArgs(String query, String[] args, int argsBias) {
        if(args != null) {
            for (int i = argsBias; i < args.length; i++) {
                boolean skip = false;
                if (query.contains("@a" + i)) {
                    for(String constant : argumentConstants) {
                        if (query.contains("@a" + i + constant)) {
                            query = query.replace("@a" + i + constant, constant);
                            skip = true;
                            break;
                        }
                    }
                    if(!skip)
                        query = query.replace("@a" + i, args[i - argsBias]);
                } else {
                    throw new IllegalArgumentException(lastLoadedResource + " don't receives " + i + " parameter(s)");
                }
            }
        }
        query = query.replaceAll("'@a(\\d*)'", "null");
        query = query.replaceAll("@a(\\d*)", "null");
        return query;
    }

    public ResultSet executeSelect(String query, String... args) {
        if(!validateQuery(query))
            return null;
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
            logBeforeExecution(statement.toString());
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
        if(!validateQuery(query))
            return false;
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
            logBeforeExecution(statement.toString());
            statement.executeUpdate();
            logAfterExecution(true);
        } catch (SQLException e) {
            logAfterExecution(false);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean executeInsert(String query, String table, String... args) {
        InsertQueryBuilder queryBuilder = new InsertQueryBuilder(table, query);
        queryBuilder.addRow(args);
        PreparedStatement statement = queryBuilder.getStatement();
        return executeUpdate(statement);
    }

    public boolean executeCall(String query, String... args) {
        try {
            PreparedStatement statement = prepareStatement(query, args);
            logBeforeExecution();
            logBeforeExecution(statement.toString());
            statement.executeQuery();
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
            Logger.log(SQLExecutor.class, "Can't load resource: " + e.getLocalizedMessage(), 2);
            return "";
        }
    }

    private boolean validateQuery(String query) {
        if(query == null || query.isEmpty()) {
            Logger.log(SQLExecutor.class, "Invalid query found", 3);
            return false;
        }
        return true;
    }

    private void logBeforeExecution() {
        Logger.log(this, "Executing query: " + lastLoadedResource, 3);
    }

    private void logBeforeExecution(String statement) {
        Logger.log(this, "Executing statement: " + statement, 4);
    }

    private void logAfterExecution(boolean successful) {
        if(successful)
            Logger.log(this, "Query executed", 4);
        else
            Logger.log(this,"Query execution failed: " + lastLoadedResource, 2);
    }
}
