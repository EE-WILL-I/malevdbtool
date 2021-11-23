<%@ page import="com.malevdb.Utils.PropertyType" %>
<%@ page import="com.malevdb.Utils.PropertyReader" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String style_form = PropertyReader.getPropertyValue(PropertyType.STYLE, "authorization.form"); %>
<% String background_color = PropertyReader.getPropertyValue(PropertyType.STYLE, "shared.background_color"); %>
<html>
<head>
    <title>Data load</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/elements/header.jsp"/>
<div>
<form action="load" method="post" style="<%=style_form%>">
    <div style="<%=background_color%> width: 80%;">
        <ul>
            <form action = "file_chooser" method = "post"
                  enctype = "multipart/form-data">
                <input type = "file" name = "file" size = "50" />
                <br />
                <input type = "submit" value = "Upload Excel File" />
            </form>
        </ul>
        <% if(request.getParameterMap().containsKey("notfound")) {%>
        <ul style="color: red;>">File not found</ul>
        <%}%>
    </div>
</form>
</div>
<jsp:include page="/WEB-INF/jsp/elements/footer.jsp" />
</body>
</html>