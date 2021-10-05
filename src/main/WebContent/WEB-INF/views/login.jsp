<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login page</title>
</head>
<body>
<jsp:include page="/WEB-INF/elements/header.jsp"/>
<h2>Authorized!</h2>
<h1>Hello <%= request.getParameter("user") %></h1>
<form action="/load">
    <button type="submit">Load excel file</button>
</form>
<jsp:include page="/WEB-INF/elements/footer.jsp" />
</body>
</html>
