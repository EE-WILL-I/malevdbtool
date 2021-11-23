<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%/*HttpSession sess = request.getSession();*/%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>MaleVVV DataBase tool</title>
</head>
<body>
<jsp:include page="elements/header.jsp" />
<h1><%/*if(sess.getAttribute("auth") == null || sess.getAttribute("auth").equals("false") || ((String)sess.getAttribute("username")).isEmpty()) {
*/%>Hello, Database User<%/*
} else {
*/%>Hello, <%/*sess.getAttribute("username");
}*/%>

<%
    java.util.Date date = new java.util.Date();
%>

<h2>
    Now is
    <%=date.toString()%>
</h2>
<jsp:include page="elements/footer.jsp" />
</body>
</html>