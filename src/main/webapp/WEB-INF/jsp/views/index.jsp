<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.malevdb.Localization.LocalizationManager" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>MaleVVV DataBase tool</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>
<jsp:include page="../elements/header.jsp" />
<jsp:include page="../elements/popup.jsp"/>
<div class="content_holder">
    <h1><%=LocalizationManager.getString("index.hello")%> <%=pageContext.getSession().getAttribute("user_login")%></h1>
    <%java.util.Date date = new java.util.Date();%>
    <h2>
        <%=LocalizationManager.getString("index.time")%> <%=date.toString()%>
    </h2>
</div>
<jsp:include page="../elements/footer.jsp" />
</body>
</html>