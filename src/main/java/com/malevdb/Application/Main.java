package com.malevdb.Application;

import com.malevdb.Database.DatabaseConnector;
import com.malevdb.MailService.MailSender;

public class Main {
    public static void main(String[] args)  {
        Logger.Log(Main.class, "Starting the server...");
        try {
            DatabaseConnector.setConnection(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //"makeev.andrey.2001@mail.ru", "alexey.zenin@list.ru", "eleonora.tschepkowa2001@yandex.ru"
        String[] recipients = new String[] {"the-first-templar@mail.ru"};
        MailSender.getInstance().sendMessage("Если вы видите это сообщение, значит Богдан запилил отправку смс по почте", "Проект малева", recipients);
        Logger.Log(Main.class, "Stopping the server...");
    }
}
