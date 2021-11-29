package com.malevdb.Application.Configurations;

import com.malevdb.Utils.FileResourcesUtils;
import com.malevdb.Utils.PropertyReader;
import com.malevdb.Utils.PropertyType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Properties;

@Configuration

@ComponentScan("com.malevdb.Application.*")
public class ApplicationContextConfig {

    @Bean(name = "viewResolver")
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //PropertyReader.loadProperty(FileResourcesUtils.RESOURCE_PATH + PropertyType.APPLICATION + PropertyReader.FILE_POSTFIX);
        viewResolver.setPrefix(PropertyReader.getPropertyValue(PropertyType.APPLICATION, "spring.mvc.view.prefix"));
        viewResolver.setSuffix(PropertyReader.getPropertyValue(PropertyType.APPLICATION, "spring.mvc.view.suffix"));
        return viewResolver;
    }

}