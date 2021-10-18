package com.malevdb.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.mail.AuthenticationFailedException;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;

public class SecureServlet extends HttpServlet {
    protected boolean checkAuthorized(HttpServletRequest request) {
        if(request.getSession().getAttribute("auth") == null)
            request.getSession().setAttribute("auth", "false");
        return request.getSession().getAttribute("auth").equals("true");
    }

    protected void checkUser(HttpServletRequest request, HttpServletResponse response) throws AuthenticationFailedException, IOException, ServletException {
        if(!checkAuthorized(request)) {
            this.getServletContext().getRequestDispatcher("/login").forward(request, response);
            response.setStatus(401);
            throw new AuthenticationFailedException("User not authorized");
        }
    }
}
