<%@ page import="com.malevdb.Utils.PropertyReader" %>
<%@ page import="com.malevdb.Utils.PropertyType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav>
    <%String style = PropertyReader.getPropertyKey(PropertyType.STYLE, "header.nav");%>
    <div style="<%=style%>">
    <a href="/index.jsp">Home</a> | <a href="/authorization.jsp">Login</a> | <a href="#">About</a>
    <div style="float: right; padding-right: 10px;">
        Search <input name="search">
    </div>
    </div>
</nav>
