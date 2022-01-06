package com.malevdb.Application.Security;

import Utils.Properties.PropertyReader;
import Utils.Properties.PropertyType;
import com.malevdb.Application.SessionManagement.SessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class SecurityHandlerInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(PropertyReader.getPropertyValue(PropertyType.SERVER, "app.disableSecurity")
                .toLowerCase(Locale.ROOT).equals("true"))
            return true;
        if (request.getServletPath().contains("/login"))
            return true;
        if (SessionManager.checkSession(request))
            return true;
        response.sendRedirect("/login");
        return false;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }
}
