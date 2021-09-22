package com.malevdb.Application;

import com.malevdb.Database.DatabaseConnector;

public class Main {
    public static void main(String[] args)  {
        Logger.Log(Main.class, "Starting the server...");
        try {
            DatabaseConnector.setConnection(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.Log(Main.class, "Stopping the server...");
    }
}
