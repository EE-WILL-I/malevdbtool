package com.malevdb.Database;
import com.malevdb.Application.Logger;
import com.malevdb.Utils.*;

import java.sql.*;

public class DatabaseConnector {
    /**
     * JDBC Driver and database url
     */
    public static Connection connection;
    public static Statement statement;
    private static final String JDBC_DRIVER = PropertyReader.getPropertyKey(PropertyType.DATABASE, "datasource.driver-class-name");
    private static final String DATABASE_URL = PropertyReader.getPropertyKey(PropertyType.DATABASE, "datasource.url");
    private static final String DATABASE_SCHEMA = PropertyReader.getPropertyKey(PropertyType.DATABASE, "datasource.schema");
    private static final String CONNECTION_ARGS = PropertyReader.getPropertyKey(PropertyType.DATABASE, "connection.args");

    /**
     * User and Password
     */
    private static final String USER = PropertyReader.getPropertyKey(PropertyType.DATABASE, "datasource.username");
    private static final String PASSWORD = PropertyReader.getPropertyKey(PropertyType.DATABASE, "datasource.password");

    public static boolean setConnection(String... args) throws ClassNotFoundException, SQLException {
        Logger.Log(DatabaseConnector.class,"Registering JDBC driver...");

        Class.forName(JDBC_DRIVER);

        Logger.Log(DatabaseConnector.class,"Creating database connection...");

        try {
            if (args.length == 3)
                connection = DriverManager.getConnection(args[0], args[1], args[2]);
            else
                connection = DriverManager.getConnection(DATABASE_URL + DATABASE_SCHEMA + CONNECTION_ARGS, USER, PASSWORD);
        } catch (SQLException e) {
            Logger.Log(DatabaseConnector.class,"Unable connect to database.");
            e.printStackTrace();
            return false;
        }

        statement = connection.createStatement();
        return true;
    }

    public static Connection getConnection() {
        if(connection == null) {
            try {
                setConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        Logger.Log(DatabaseConnector.class,"Closing connection and releasing resources...");
        statement.close();
        connection.close();
    }

    @Override
    public void finalize() throws SQLException {
        closeConnection();
    }
}
