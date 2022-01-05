package com.malevdb.Application;

import Utils.FileResourcesUtils;
import Utils.Logging.Logger;
import Utils.Properties.PropertyReader;
import com.malevdb.Application.Servlets.SecurityHandlerInterceptor;
import com.malevtool.Connection.DatabaseConnector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
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
        Logger.log(Main.class, "Starting the server...");
        //entry point
        //SpringApplication.run(Main.class, args);
        ctx = new SpringApplicationBuilder(Main.class).web(WebApplicationType.SERVLET).run(args);
        ctx.getBean(TerminateBean.class);
        System.out.println("Spring application started");
        //connect to DB
        try {
            DatabaseConnector.setConnection(args);
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    public static void stop() throws Exception {
        System.out.println("Stopping application..");
        DatabaseConnector.closeConnection();
        ctx.close();
        SpringApplication.exit(ctx, () -> 0);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Starting from command line");
        };
    }
}
