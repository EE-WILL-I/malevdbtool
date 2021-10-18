<%@ page import="com.malevdb.Utils.PropertyType" %>
<%@ page import="com.malevdb.Utils.PropertyReader" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String background_color = PropertyReader.getPropertyKey(PropertyType.STYLE, "shared.background_color"); %>
<% String table = PropertyReader.getPropertyKey(PropertyType.STYLE, "shared.table"); %>
<html>
<head>
    <title>Data view</title>
    <style>
        th, tr, td, table {
            <%=table%>
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/elements/header.jsp"/>
<div style="<%=background_color%>">
    <table>
            <tr>
        <%List<String> columns = (List<String>) request.getAttribute("columns");
            for(String name : columns) {%>
                <th>
                    <%=name%>
                </th>
                <%}%>
            </tr>
        <%
            List<Map> data = (List<Map>) request.getAttribute("data_map");
            for(Map<String, String> map : data) {
        %>
            <tr><%
              for(String name : columns)  {%>
                <td><%=map.get(name)%></td>
                <%}%>
            </tr>
        <%}%>
    </table>
</div>
<jsp:include page="/WEB-INF/elements/footer.jsp" />
</body>
</html>
