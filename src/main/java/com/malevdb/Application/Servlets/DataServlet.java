package com.malevdb.Application.Servlets;

import Utils.Logging.Logger;
import com.malevdb.Database.DataTable;
import com.malevtool.Proccessing.SQLExecutor;
import com.malevtool.SQL.InsertQueryBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
    private static final String defaultTable = "people";
    @GetMapping("/data")
    public String doGetDef(@ModelAttribute(name = "show_popup") String showPopup,
                           @ModelAttribute(name = "popup_message") String popupMessage,
                           RedirectAttributes attributes) {
        if(!showPopup.isEmpty() && !popupMessage.isEmpty())
            ServletUtils.showPopup(attributes, popupMessage, showPopup);
        return "redirect:/data/people";
    }

    @GetMapping("data/{tableName}")
    public String doGet(Model model, @PathVariable(value = "tableName") String tableName, RedirectAttributes attributes) {
        if(tableName.isEmpty() || tableName.equals("none")) {
            return "redirect:/data/"+defaultTable;
        }
        if(loadTable(model, tableName)) {
            return "views/dataView";
        } else if(tableName.equals(defaultTable)) {
            ServletUtils.showPopup(attributes, "Can't load page because unable to fetch data from the database. Check connection status.", "error");
            return "redirect:/";
        } else {
            ServletUtils.showPopup(attributes, "Can't load table " + tableName, "error");
            return "redirect:/data/"+defaultTable;
        }
    }

    @PostMapping("/data/update/{tableName}")
    public String doPut(HttpServletRequest request, @PathVariable(value = "tableName") String table,
                        RedirectAttributes attributes) {
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
        } catch (Exception e) {
            Logger.log(this, e.getMessage(), 2);
            ServletUtils.showPopup(attributes, e.getLocalizedMessage(), "error");
        }
        return "redirect:/data/" + table;
    }

    @PostMapping("/data/delete/{tableName}")
    public String doDelete(@PathVariable(value = "tableName") String table,
                           @RequestParam(value = "column") String column,
                           @RequestParam(value = "value") String value,
                           RedirectAttributes attributes) {
        if(table.isEmpty())
            return "redirect:/data";
        try {
            SQLExecutor.getInstance().executeUpdate(SQLExecutor.getInstance().loadSQLResource("delete_any.sql"),
                    table, column, value);
        } catch (SQLException e) {
            Logger.log(this, e.getLocalizedMessage(), 2);
            ServletUtils.showPopup(attributes, e.getLocalizedMessage(), "error");
        }
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
            InsertQueryBuilder insertBuilder = new InsertQueryBuilder(tableName,
                    executor.loadSQLResource("insert_" + tableName + ".sql"));
            for (Map<String, String> rowData : table.getDataRows()) {
                List<String> row = new ArrayList<>(table.columnCount);
                for (String column : table.getColumnLabels()) {
                    row.add(rowData.get(column));
                }
                insertBuilder.addRow(row.toArray(new String[0]));
            }
            executor.executeUpdate(insertBuilder.getStatement());;
            return "redirect:/data/" + tableName;
        } catch (Exception e) {
            Logger.log(this, e.getMessage(), 3);
            ServletUtils.showPopup(attributes, e.getMessage(), "error");
            return "redirect:/data";
        }
    }

    @PostMapping("/data/insert/json/{tableName}")
    public String doPostJSON(HttpServletRequest request, @PathVariable("tableName") String tableName,
                             RedirectAttributes attributes) {
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
            InsertQueryBuilder queryBuilder = new InsertQueryBuilder(tableName,
                    executor.loadSQLResource("insert_" + tableName + ".sql"));
            ArrayList<String> rowData = new ArrayList<>(data.size());
            while(iterator.hasNext()) {
                JSONObject obj = iterator.next();
                rowData.add((String) obj.get("newValue"));
            }
            queryBuilder.addRow(rowData.toArray(new String[0]));
            executor.executeUpdate(queryBuilder.getStatement());
            Logger.log(this, "Updated table " + tableName, 1);
        } catch (Exception e) {
            Logger.log(this, e.getMessage(), 2);
            ServletUtils.showPopup(attributes, e.getLocalizedMessage(), "error");
        }
        return "redirect:/data/" + tableName;
    }

    private boolean loadTable(Model model, String tableName) {
        SQLExecutor executor = SQLExecutor.getInstance();
        try {
        ResultSet resultSet = executor.executeSelect(executor.loadSQLResource("select_any.sql"),
                "*", tableName);
            DataTable table = new DataTable(resultSet.getMetaData().getTableName(1));
            table.populateTable(resultSet);
            resultSet.close();
            model.addAttribute("table", table);
            return true;
        } catch (Exception e) {
            Logger.log(this, "Error during parsing data from DB: " + e.getMessage(), 2);
            return false;
        }
    }
}
