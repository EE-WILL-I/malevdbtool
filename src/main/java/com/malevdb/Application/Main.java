package com.malevdb.Application;

import Utils.FileResourcesUtils;
import Utils.Logging.Logger;
import Utils.Properties.PropertyReader;
import com.malevdb.Application.Security.AuthorizationManager;
import com.malevdb.Application.Security.SecurityHandlerInterceptor;
import com.malevdb.MailService.MNSAuthenticator;
import com.malevtool.Connection.DatabaseConnector;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.io.FileNotFoundException;

@SpringBootApplication
public class Main {
    private static ConfigurableApplicationContext ctx;
    @Bean
    public SecurityHandlerInterceptor securityHandlerInterceptor() {
        return new SecurityHandlerInterceptor();
    }

    @Bean
    public WebMvcConfigurerAdapter adapter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //add interceptor on page pre-load state to check if user is authorized
                registry.addInterceptor(securityHandlerInterceptor());
            }
        };
    }

    public static void main(String[] args) throws Exception {
        init();
        start(args);
    }

    private static void init() {
        System.out.println("Loading server properties");
        String resPath = "";
        try {
            //try load resources from JAR
            resPath = ResourceUtils.getFile("classpath:application.properties").getParent() + "/";
        } catch (FileNotFoundException e) {
            //load resources from .WAR directory
            System.out.println("Loading properties from local directories");
            resPath = new File(System.getProperty("java.class.path")).getAbsoluteFile().getAbsolutePath();
            //remove file name from path
            resPath = resPath.replaceAll("[^/|^\\\\]*$", "");
            System.out.println("Executable path: " + resPath);
        }
        FileResourcesUtils.RESOURCE_PATH = resPath;
        PropertyReader.loadServerProps();
        MNSAuthenticator.loadProvidedUserCredentials();
        if(!AuthorizationManager.loadConfiguredServiceUserCredentials())
            AuthorizationManager.loadDefaultServiceUserCredentials();
    }

    private static void start(String[] args) throws Exception {
        StringBuilder argsStr = new StringBuilder();
        for(String arg : args)
            argsStr.append(arg);
        Logger.log(Main.class, "Starting the server with args: " + argsStr, 1);
        ctx = new SpringApplicationBuilder(Main.class).web(WebApplicationType.SERVLET).run(args);
        ctx.getBean(TerminateBean.class);
        Logger.log(Main.class,"Spring application started", 1);
        //connect to DB
        try {
            DatabaseConnector.setConnection(args);
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    public static void stop() throws Exception {
        Logger.log(Main.class, "Stopping application..", 1);
        try {
            DatabaseConnector.closeConnection();
        } catch (Exception e) { Logger.log(Main.class,"Can't close DB connection", 2); }
        ctx.close();
        SpringApplication.exit(ctx, () -> 0);
    }

    public static void restart(String [] args) throws Exception {
        try {
            DatabaseConnector.closeConnection();
        } catch (Exception e) { Logger.log(Main.class,"Can't close DB connection", 2); }

        Thread thread = new Thread(() -> {
            ctx.close();
            init();
            try {
                start(args);
            } catch (Exception e) {
                System.out.println("Restarting server ended up with an error. " + e.getMessage());
            }
        });

        thread.setDaemon(false);
        thread.start();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            Logger.log(Main.class, "Starting from command line", 1);
        };
    }
}
