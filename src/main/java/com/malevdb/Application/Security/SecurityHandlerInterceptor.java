package com.malevdb.Application.Security;

import Utils.Logging.Logger;
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
        String auth = request.getHeader("Authorization");
        if(auth != null && !auth.isEmpty()) {
            String credentials = auth.substring(6);
            if (AuthorizationManager.authorize(credentials))
                return true;
        }
        Logger.log(this, "Access denied for unauthorized user. IP:" + request.getRemoteAddr(), 2);
        response.sendRedirect("/login");
        return false;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }
}
