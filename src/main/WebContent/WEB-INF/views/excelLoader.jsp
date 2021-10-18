<%@ page import="com.malevdb.Utils.PropertyType" %>
<%@ page import="com.malevdb.Utils.PropertyReader" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String style_form = PropertyReader.getPropertyKey(PropertyType.STYLE, "authorization.form"); %>
<% String background_color = PropertyReader.getPropertyKey(PropertyType.STYLE, "shared.background_color"); %>
<html>
<head>
    <title>Data load</title>
</head>
<body>
<jsp:include page="/WEB-INF/elements/header.jsp"/>
<div>
<form action="load" method="post" style="<%=style_form%>">
    <div style="<%=background_color%> width: 80%;">
        <ul>
            <p>Path:</p>
            <input type="text" name="path"/>
        </ul>
        <ul>
            <button type="submit" name="Load file" value="load">Load File</button>
        </ul>
    </div>
</form>
</div>
<jsp:include page="/WEB-INF/elements/footer.jsp" />
</body>
</html>