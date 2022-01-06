package com.malevdb.Application.Servlets;

import com.malevdb.Application.Main;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ShutdownServlet implements ApplicationContextAware {
    private ApplicationContext context;

    @PostMapping("app/shutdown")
    public void shutdownApplication() {
        ((ConfigurableApplicationContext)context).close();
    }

    @PostMapping("app/restart/{args}")
    public void restartApplicationWithArgs(@PathVariable(required = false) String [] args) throws Exception {
        Main.restart(args);
    }

    @PostMapping("/app/restart")
    public void restartApplication() throws Exception {
        restartApplicationWithArgs(new String[0]);
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.context = ctx;

    }
}
