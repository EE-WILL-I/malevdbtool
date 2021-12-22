package com.malevdb.Application;

import com.malevdb.Application.Logging.Logger;
import com.malevdb.Application.Servlets.SecurityHandlerInterceptor;
import com.malevdb.Database.DatabaseConnector;
import com.malevdb.Utils.FileResourcesUtils;
import com.malevdb.Utils.PropertyReader;
import com.malevdb.Utils.PropertyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.util.Arrays;

@SpringBootApplication
public class Main {
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
        System.out.println("Loading server properties");
        String resPath = "";
        try {
            resPath = ResourceUtils.getFile("classpath:application.properties").getParent() + "/";
        } catch (FileNotFoundException e) {
            System.out.println("Loading properties from local directories");
            resPath = new File(System.getProperty("java.class.path")).getAbsoluteFile().getAbsolutePath();
            resPath = resPath.replaceAll("[^/|^\\\\]*$", "");
            System.out.println("Executable path: " + resPath);
        }
        FileResourcesUtils.RESOURCE_PATH = resPath;
        PropertyReader.loadServerProps();
        Logger.log(Main.class, "Starting the server...");
        SpringApplication.run(Main.class, args);
        System.out.println("Прием, tomcat на связи слышно нормально, работаем по плану..");
        try {
            DatabaseConnector.setConnection(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Стартуем сервер с командно строки.. прием, прием, как слышно?");
        };
    }
}
