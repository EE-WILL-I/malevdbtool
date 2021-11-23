package com.malevdb.Application.Servlets;
import com.malevdb.Application.Logging.Logger;
import com.malevdb.Database.SQLExecutor;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pwriter;
        try {
            pwriter = response.getWriter();
        } catch (IOException e) {
            this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
            response.setStatus(500);
            return;
        }

        if(!request.getParameterMap().containsKey("user"))
            this.getServletContext().getRequestDispatcher("/authorization.jsp").forward(request, response);

        String user = request.getParameter("user");
        String passwd = request.getParameter("passwd");

        SQLExecutor executor = SQLExecutor.getInstance();
        try {
            ResultSet resultSet = executor.executeSelect(executor.loadSQLResource("get_user.sql"), user, passwd);
            resultSet.next();
            int result = resultSet.getInt(1);
            if(result == 1) {
                Logger.log(this,"Login successful", 3);
                pwriter.println("Login successful");
                request.getSession().setAttribute("username", user);
                request.getSession().setAttribute("auth", "true");
                this.getServletContext().getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                response.setStatus(200);
            } else {
                Logger.log(this,"Login failed", 3);
                pwriter.println("Login failed");
                request.getSession().setAttribute("auth", "false");
                this.getServletContext().getRequestDispatcher("/authorization.jsp?failed=true&").forward(request, response);
                response.setStatus(401);
            }
        }  catch (SQLException e) {
            Logger.log(this, "Login failed", 3);
            e.printStackTrace();
            pwriter.println("Login failed");
            this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
            response.setStatus(401);
        } finally {
            pwriter.close();
        }
    }
}
