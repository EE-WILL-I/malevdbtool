<%@ page import="com.malevdb.Utils.PropertyType" %>
<%@ page import="com.malevdb.Utils.PropertyReader" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--stryle init--%>
<% String style_header = PropertyReader.getProperty(PropertyType.STYLE, "shared.header"); %>
<% String style_form = PropertyReader.getProperty(PropertyType.STYLE, "authorization.form"); %>
<% String background_color = PropertyReader.getProperty(PropertyType.STYLE, "shared.background_color"); %>
<% String failed_request = request.getParameterMap().containsKey("failed") ? "block" : "none"; %>
<html>
<body>
<div>
<form action="login"  style="<%=style_form%>">
    <div style="<%=background_color%> width: 300px;">
        <h2 style="<%=style_header%>">Authorization</h2>
        <ul>
            <p>Login:</p>
            <input name="user" />
        </ul>
        <ul>
            <p>Password:</p>
            <input name="passwd"/>
        </ul>
        <ul>
            <input type="submit" value="Log in" />
        </ul>
        <ul style="color: red; display: <%=failed_request%>">Authentication failed</ul>
    </div>
</form>
</div>
</body>
</html>
