<%@ page import="com.malevdb.Utils.PropertyType" %>
<%@ page import="com.malevdb.Utils.PropertyReader" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String style_form = PropertyReader.getPropertyKey(PropertyType.STYLE, "authorization.form"); %>
<% String background_color = PropertyReader.getPropertyKey(PropertyType.STYLE, "shared.background_color"); %>
<html>
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
        <div>
            <p><%= request.getParameter("data") %></p>
        </div>
        <div>
            <table>
                <tr>
                    <th>name</th>
                    <th>desc</th>
                    <th>contact<th>
                </tr>

                <c:forEach items="${key_list}" var="usr" varStatus="idx">
                    <tr>
                        <td>${usr.name}</td><td>${usr.desc}</td> <td>${usr.contact}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</form>
</div>
</body>
</html>