<%@ page import="com.malevdb.Utils.PropertyType" %>
<%@ page import="com.malevdb.Utils.PropertyReader" %>
<%@ page import="com.malevdb.Application.SessionManagement.SessionManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--style init--%>
<% String style_header = PropertyReader.getPropertyValue(PropertyType.STYLE, "shared.header"); %>
<% String style_form = PropertyReader.getPropertyValue(PropertyType.STYLE, "authorization.form"); %>
<% String background_color = PropertyReader.getPropertyValue(PropertyType.STYLE, "shared.background_color"); %>
<html>
<head>
    <title>Login page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>
<div style="width: fit-content; margin-left: auto; margin-right: auto;">
    <%if(!SessionManager.checkSession(request)) {%>
        <form action="login" method="post" style="<%=style_form%>">
            <div style="<%=background_color%> width: 300px;">
                <h2 style="<%=style_header%>">Authorization</h2>
                <ul>
                    <p>Login:</p>
                    <input name="login" />
                </ul>
                <ul>
                    <p>Password:</p>
                    <input name="passwd" type="password"/>
                </ul>
                <ul>
                    <input type="submit" value="Log in" />
                </ul>
                <%if(pageContext.getRequest().getAttribute("displayError") != null) {%>
                <ul style="color: red;>">Authentication failed</ul>
                <%}%>
            </div>
        </form>
    <%} else {%>
        <div style="display: flex; justify-content: center; width:100%; ">
            <div style="<%=background_color%> justify-content: center; width: 300px;">
                <h2 style="<%=style_header%>">You're already logged in.</h2>
                <ul>
                    <a name="logout" href="/logout">Logout</a>
                </ul>
            </div>
        </div>
    <%}%>
</div>
</body>
</html>
