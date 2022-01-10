package com.malevdb.Application.Servlets;

import Utils.JSON.JSONBuilder;
import Utils.Logging.Logger;
import Utils.Properties.PropertyReader;
import Utils.Properties.PropertyType;
import com.malevtool.Proccessing.SQLExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;

@RestController
public class DatabaseRestServlet {
    @GetMapping("/data/tables")
    @ResponseBody
    public String getSchemaTables() {
        SQLExecutor executor = SQLExecutor.getInstance();
        ResultSet resultSet = executor.executeSelect(executor.loadSQLResource("get_tables.sql"),
                PropertyReader.getPropertyValue(PropertyType.DATABASE, "datasource.schema"));
        JSONBuilder data = new JSONBuilder();
        try {
            while (resultSet.next()) {
                data.addSubJSONElement(new JSONBuilder().addAVP("table", resultSet.getString("table_name")).getString());
            }
            resultSet.close();
            return "[" + data.getString().substring(1, data.getString().length() - 1) + "]";
        } catch (Exception e) {
            Logger.log(this, e.getMessage(), 2);
            return new JSONBuilder().addAVP("status", "error").addAVP("message", e.getMessage()).getString();
        }
    }
}
