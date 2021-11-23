<%@ page import="com.malevdb.Utils.PropertyReader" %>
<%@ page import="com.malevdb.Utils.PropertyType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav>
    <%String style = PropertyReader.getPropertyValue(PropertyType.STYLE, "header.nav");%>
    <div style="<%=style%>">
    <a href="/WEB-INF/jsp/index.jsp">Home</a> | <a href="/dataView">Data view</a> | <a href="/load">Load File</a> | <a href="/WEB-INF/jsp/authorization.jsp">Login</a> | <a href="#">About</a>
    <div style="float: right; padding-right: 10px;">
        Search <input name="search">
    </div>
    </div>
</nav>
