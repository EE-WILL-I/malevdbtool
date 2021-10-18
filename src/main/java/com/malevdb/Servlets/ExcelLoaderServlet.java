package com.malevdb.Servlets;

import com.malevdb.Database.SQLExecutor;
import com.malevdb.Utils.Excel.ExcelParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/load")
public class ExcelLoaderServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/excelLoader.jsp").forward(request, response);
        response.setStatus(200);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ExcelParser excelParser = new ExcelParser();
        SQLExecutor.getInstance().executeUpdate(excelParser.prepareStatement("people", "(null, '@a0','@a1','@a2')", excelParser.read(request.getParameter("path")), 0));

        this.getServletContext().getRequestDispatcher("/dataView").forward(request, response);
        response.setStatus(200);
    }
}
