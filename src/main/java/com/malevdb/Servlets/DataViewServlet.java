package com.malevdb.Servlets;

import com.malevdb.Application.Logger;
import com.malevdb.Database.SQLExecutor;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/dataView")
public class DataViewServlet extends SecureServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            checkUser(request, response);
        } catch (Exception e)
        {
            return;
        }
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        List<String> headers = new ArrayList<>();
        SQLExecutor executor = SQLExecutor.getInstance();
        String query = request.getParameter("query");
        if (query == null || query.isEmpty()) {
            query = "select_people.sql";
        }

        ResultSet resultSet = executor.executeSelect(executor.loadSQLResource(query), "*");
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int column = 1; column <= metaData.getColumnCount(); column++) {
                headers.add(metaData.getColumnName(column));
            }
            while (resultSet.next()) {
                Map<String, String> map = new HashMap<String, String>();
                for (int column = 1; column <= metaData.getColumnCount(); column++) {
                    map.put(metaData.getColumnName(column), resultSet.getString(column));
                }
                data.add(map);
            }
            resultSet.close();
        } catch (SQLException e) {
            Logger.Log(this, "Error during parsing data from DB");
            e.printStackTrace();
        }
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/dataView.jsp?");
        request.setAttribute("columns", headers);
        request.setAttribute("data_map", data);
        dispatcher.forward(request, response);
        response.setStatus(200);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
