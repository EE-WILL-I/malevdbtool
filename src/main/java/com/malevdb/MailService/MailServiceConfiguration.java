package com.malevdb.MailService;

import com.malevdb.Utils.PropertyReader;
import com.malevdb.Utils.PropertyType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MailServiceConfiguration {
    @Bean
    @Scope("singleton")
    public MailSender mailSender() {
        return new MailSender(PropertyType.MAILSERVICE.toString().toLowerCase());
    }
}
