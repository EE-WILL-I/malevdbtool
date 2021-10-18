package com.malevdb.Application;

public class Logger {
    public static void Log(Object sender, Object data) {
        if(data == null || sender == null)
            return;
        String senderName;
        if(sender.getClass().getSimpleName().equals("Class"))
            senderName = ((Class)sender).getName();
        else
            senderName = sender.getClass().getName();
        System.out.println(senderName + "  :  " + data.toString());
    }
}
