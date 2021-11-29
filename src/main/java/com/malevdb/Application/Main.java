package com.malevdb.Application;

import com.malevdb.Application.Logging.Logger;
import com.malevdb.Application.Servlets.SecurityHandlerInterceptor;
import com.malevdb.Database.DatabaseConnector;
import com.malevdb.Utils.FileResourcesUtils;
import com.malevdb.Utils.PropertyReader;
import com.malevdb.Utils.PropertyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

@SpringBootApplication
public class Main extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Main.class);
    }

    @Bean
    public SecurityHandlerInterceptor securityHandlerInterceptor() {
        return new SecurityHandlerInterceptor();
    }

    @Bean
    public WebMvcConfigurerAdapter adapter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(securityHandlerInterceptor());
            }
        };
    }

    public static void main(String[] args) throws Exception {
        //Horse horse = new Horse();
        //horse.findSolution();
        //Ferzine ferzine = new Ferzine();
        //ferzine.findSolution();

        System.out.println("Loading server properties");
        FileResourcesUtils.RESOURCE_PATH = ResourceUtils.getFile("classpath:application.properties").getParent() + "\\";
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
