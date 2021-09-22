package com.malevdb.Application;

public class Logger {
    public static void Log(Object sender, Object data) {
        if(data == null || sender == null)
            return;

        System.out.println(sender.getClass().getName() + "  :  " + data.toString());
    }
}
