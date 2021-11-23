package com.malevdb.Application.Logging;

public class Logger {
    public static int loggingLevel = 4;

    public static void log(Object sender, Object data, int level) {
        if(data == null || sender == null)
            return;
        if(level == 0 || level > loggingLevel)
            return;
        String senderName;
        if(sender.getClass().getSimpleName().equals("Class"))
            senderName = ((Class)sender).getName();
        else
            senderName = sender.getClass().getName();
        System.out.println(senderName + "  :  " + data.toString());
    }

    public static void log(Object sender, Object data) { log(sender, data, 1); }
}
