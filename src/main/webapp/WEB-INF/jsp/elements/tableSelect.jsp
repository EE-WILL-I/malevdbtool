<%@ page import="com.malevdb.Database.SQLExecutor" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.malevdb.Utils.PropertyReader" %>
<%@ page import="com.malevdb.Utils.PropertyType" %>
<%@ page import="com.malevdb.Database.DataTable" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    SQLExecutor executor = SQLExecutor.getInstance();
    ResultSet resultSet = executor.executeSelect(executor.loadSQLResource("get_tables.sql"),
            PropertyReader.getPropertyValue(PropertyType.DATABASE, "datasource.schema"));
    String table_name = ((DataTable)pageContext.getRequest().getAttribute("table")).getName();
%>
<div>
    <select id="selected_table" name="selected_table">
        <%while(resultSet.next()) {
        String table = resultSet.getString("table_name");%>
        <option value="<%=table%>" <%if(table.equals(table_name)) {%>selected<%}%>><%=table%></option>
        <%}%>
    </select>
</div>
