package com.malevdb.Servlets;

import com.malevdb.Database.SQLExecutor;
import com.malevdb.Utils.Excel.ExcelParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/load")
public class ExcelLoaderServlet extends SecureServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            checkUser(request, response);
        } catch (Exception e)
        {
            return;
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/excelLoader.jsp").forward(request, response);
        response.setStatus(200);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            checkUser(request, response);
        } catch (Exception e)
        {
            return;
        }
        ExcelParser excelParser = new ExcelParser();
        try {
            SQLExecutor.getInstance().executeUpdate(excelParser.prepareStatement("people", "(null, '@a0','@a1','@a2')", excelParser.read(request.getParameter("path")), 0));
        } catch (FileNotFoundException e) {
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/excelLoader.jsp?notfound").forward(request, response);
            response.setStatus(404);
            return;
        }
        this.getServletContext().getRequestDispatcher("/dataView").forward(request, response);
        response.setStatus(200);
    }
}
