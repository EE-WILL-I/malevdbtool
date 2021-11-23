package com.malevdb.Application;

import com.malevdb.Application.Logging.Logger;
import com.malevdb.Database.DatabaseConnector;
import com.malevdb.Utils.PropertyReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.File;

@SpringBootApplication
public class Main  extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Main.class);
    }

    public static void main(String[] args) {
        //Horse horse = new Horse();
        //horse.findSolution();
        //Ferzine ferzine = new Ferzine();
        //ferzine.findSolution();

        System.out.println("Loading server properties");
        PropertyReader.loadServerProps();
        Logger.log(Main.class, "Starting the server...");
        SpringApplication.run(Main.class, args);
        try {
            DatabaseConnector.setConnection(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //"makeev.andrey.2001@mail.ru", "alexey.zenin@list.ru", "eleonora.tschepkowa2001@yandex.ru"
        //String[] recipients = new String[] {"the-first-templar@mail.ru"};
        //MailSender.getInstance().sendMessage("Если вы видите это сообщение, значит Богдан запилил отправку смс по почте", "Проект малева", recipients);
        //Logger.log(Main.class, "Stopping the server...");
    }
}
