<%@ page import="com.malevdb.Utils.PropertyType" %>
<%@ page import="com.malevdb.Utils.PropertyReader" %>
<%@ page import="com.malevdb.Localization.LocalizationManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Data load</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>
<jsp:include page="../elements/header.jsp"/>
<jsp:include page="../elements/popup.jsp"/>
<div class="content_holder" style="display: flex; justify-content: center;">
    <div style="width: 100%;">
        <ul>
            <form action = "load/excel" method = "post" enctype = "multipart/form-data">
                <input type = "file" name = "file" size = "50" />
                <br/>
                <input type = "submit" value = "<%=LocalizationManager.getString("excelLoader.upload")%>" />
            </form>
        </ul>
        <% if(pageContext.getRequest().getAttribute("errorMessage") != null) {%>
            <ul style="color: red"><%=pageContext.getRequest().getAttribute("errorMessage")%></ul>
        <%}%>
    </div>
</div>
<jsp:include page="../elements/footer.jsp" />
</body>
</html>