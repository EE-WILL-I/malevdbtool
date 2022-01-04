package com.malevdb.Application.Servlets;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ShutdownServlet implements ApplicationContextAware {
    private ApplicationContext context;

    @PostMapping("/shutdownContext")
    public void shutdownContext() {
        ((ConfigurableApplicationContext)context).close();
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.context = ctx;

    }
}
