<%@ page import="com.malevdb.Database.SQLExecutor" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.malevdb.Utils.PropertyReader" %>
<%@ page import="com.malevdb.Utils.PropertyType" %>
<%@ page import="com.malevdb.Database.DataTable" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    SQLExecutor executor = SQLExecutor.getInstance();
    ResultSet resultSet = executor.executeSelect(executor.loadSQLResource("get_tables.sql"),
            PropertyReader.getPropertyValue(PropertyType.DATABASE, "datasource.schema"));
    String table_name = ((DataTable)pageContext.getRequest().getAttribute("table")).getName();
    if(table_name == null || table_name.equals("null") || table_name.isEmpty())
        table_name = "people";
%>
<div>
    <select id="selected_table" name="selected_table">
        <%while(resultSet.next()) {
        String table = table_name;
        try {
            table = resultSet.getString("table_name");
        } catch (SQLException e) {}%>
        <option value="<%=table%>" <%if(table.equals(table_name)) {%>selected<%}%>><%=table%></option>
        <%}%>
    </select>
</div>
