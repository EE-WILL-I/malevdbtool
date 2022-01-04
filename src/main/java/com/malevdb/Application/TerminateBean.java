package com.malevdb.Application;

import com.malevdb.Database.DatabaseConnector;

import javax.annotation.PreDestroy;

public class TerminateBean {
    @PreDestroy
    public void onDestroy() throws Exception {
        System.out.println("Spring Container is destroyed");
        DatabaseConnector.closeConnection();
    }
}