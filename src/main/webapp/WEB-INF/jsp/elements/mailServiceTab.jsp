<%@ page import="com.malevdb.Localization.LocalizationManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav>
    <div id="nav_content">
        <a href="${pageContext.request.contextPath}/mail"><%=LocalizationManager.getString("mailServiceTab.send")%></a>
        | <a href="${pageContext.request.contextPath}/mail/templates/get"><%=LocalizationManager.getString("mailServiceTab.template")%></a>
    </div>
</nav>
