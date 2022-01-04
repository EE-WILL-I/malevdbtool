package com.malevdb.Application.Configurations;

import com.malevdb.Application.TerminateBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShutdownConfig {
    @Bean
    public TerminateBean getTerminateBean() {
        return new TerminateBean();
    }
}