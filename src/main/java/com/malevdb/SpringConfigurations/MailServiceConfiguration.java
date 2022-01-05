package com.malevdb.SpringConfigurations;

import Utils.Properties.PropertyReader;
import Utils.Properties.PropertyType;
import com.malevdb.MailService.MailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MailServiceConfiguration {
    @Bean
    @Scope("singleton")
    public MailSender mailSender() {
        return new MailSender(PropertyReader.getProperties(PropertyType.MAILSERVICE));
    }
}
