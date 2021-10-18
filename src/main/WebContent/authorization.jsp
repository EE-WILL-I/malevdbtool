<%@ page import="com.malevdb.Utils.PropertyType" %>
<%@ page import="com.malevdb.Utils.PropertyReader" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--style init--%>
<% String style_header = PropertyReader.getPropertyKey(PropertyType.STYLE, "shared.header"); %>
<% String style_form = PropertyReader.getPropertyKey(PropertyType.STYLE, "authorization.form"); %>
<% String background_color = PropertyReader.getPropertyKey(PropertyType.STYLE, "shared.background_color"); %>
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
        <%if(request.getParameterMap().containsKey("failed")) {%>
        <ul style="color: red;>">Authentication failed</ul>
        <%}%>
    </div>
</form>
</div>
</body>
</html>
