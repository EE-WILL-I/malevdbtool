package com.malevdb.Application.Servlets;

import com.malevdb.Application.Logging.Logger;
import com.malevdb.Database.DataTable;
import com.malevdb.Database.InsertQueryBuilder;
import com.malevdb.Database.SQLExecutor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Controller
public class DataServlet {
    @GetMapping("/data")
    public String doGetDef() {
        return "redirect:/data/people";
    }

    @GetMapping("data/{tableName}")
    public String doGet(Model model, @PathVariable(value = "tableName") String tableName) {
        if(tableName.isEmpty() || tableName.equals("none"))
            return "redirect:/data";
        loadTable(model, tableName);
        return "views/dataView";
    }

    @PostMapping("/data/update/{tableName}")
    public String doPut(HttpServletRequest request, @PathVariable(value = "tableName") String table) {
        JSONParser jsonParser = new JSONParser();
        String json = request.getParameter("updated_values");
        if((json != null && json.isEmpty()) || table.isEmpty())
            return "redirect:/view";
        try {
            JSONArray data = (JSONArray) jsonParser.parse(json);
            if(data.size() == 0)
                return "redirect:/view";
            Iterator<JSONObject> iterator = data.iterator();
            while(iterator.hasNext()) {
                JSONObject obj = iterator.next();
                String rowId = (String) obj.get("id");
                String columnName = (String) obj.get("col");
                String newValue = (String) obj.get("val");
                SQLExecutor executor = SQLExecutor.getInstance();
                executor.executeUpdate(executor.loadSQLResource("update_any.sql"),
                        table, columnName, newValue, "id = '" + rowId + "'");
            }
            Logger.log(this, "Updated table " + table, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "redirect:/data/" + table;
    }

    @PostMapping("/data/delete/{tableName}")
    public String doDelete(@PathVariable(value = "tableName") String table,
                           @RequestParam(value = "column") String column,
                           @RequestParam(value = "value") String value) {
        if(table.isEmpty())
            return "redirect:/data";
        SQLExecutor.getInstance().executeUpdate(SQLExecutor.getInstance().loadSQLResource("delete_any.sql"),
                table, column, value);
        return "redirect:/data/" + table;
    }

    @PostMapping("/data/insert/{tableName}")
    public String doPost(HttpServletRequest request, @PathVariable("tableName") String tableName,
                         RedirectAttributes attributes) {
        if (tableName.isEmpty() || tableName.equals("none"))
            return "redirect:/data";
        DataTable table = (DataTable) request.getSession().getAttribute("table");
        SQLExecutor executor = SQLExecutor.getInstance();
        try {
            InsertQueryBuilder insertBuilder = new InsertQueryBuilder(tableName, executor.loadSQLResource("insert_" + tableName + ".sql"));
            for (Map<String, String> rowData : table.getDataRows()) {
                List<String> row = new ArrayList<>(table.columnCount);
                for (String column : table.getColumnLabels()) {
                    row.add(rowData.get(column));
                }
                insertBuilder.addRow(row.toArray(new String[0]));
            }
            //System.out.println(insertBuilder.getStatement());
            executor.executeUpdate(insertBuilder.getStatement());;
            return "redirect:/data/" + tableName;
        } catch (Exception e) {
            Logger.log(this, e.getMessage(), 3);
            attributes.addAttribute("error", e.getLocalizedMessage());
            return "redirect:/data";
        }
    }

    @PostMapping("/data/insert/json/{tableName}")
    public String doPostJSON(HttpServletRequest request, @PathVariable("tableName") String tableName) {
        JSONParser jsonParser = new JSONParser();
        String json = request.getParameter("new_data");
        if((json != null && json.isEmpty()) || tableName.isEmpty())
            return "redirect:/view";
        try {
            JSONArray data = (JSONArray) jsonParser.parse(json);
            if(data.size() == 0)
                return "redirect:/view";
            Iterator<JSONObject> iterator = data.iterator();
            SQLExecutor executor = SQLExecutor.getInstance();
            InsertQueryBuilder queryBuilder = new InsertQueryBuilder(tableName, executor.loadSQLResource("insert_" + tableName + ".sql"));
            ArrayList<String> rowData = new ArrayList<>(data.size());
            while(iterator.hasNext()) {
                JSONObject obj = iterator.next();
                rowData.add((String) obj.get("newValue"));
            }
            queryBuilder.addRow(rowData.toArray(new String[0]));
            //System.out.println(queryBuilder.getStatement().toString());
            executor.executeUpdate(queryBuilder.getStatement());
            Logger.log(this, "Updated table " + tableName, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "redirect:/data/" + tableName;
    }

    private boolean loadTable(Model model, String tableName) {
        SQLExecutor executor = SQLExecutor.getInstance();
        ResultSet resultSet = executor.executeSelect(executor.loadSQLResource("select_any.sql"),
                "*", tableName);
        try {
            DataTable table = new DataTable(resultSet.getMetaData().getTableName(1));
            table.populateTable(resultSet);
            resultSet.close();
            model.addAttribute("table", table);
            return true;
        } catch (SQLException e) {
            Logger.log(this, "Error during parsing data from DB", 2);
            e.printStackTrace();
            return false;
        }
    }
}
