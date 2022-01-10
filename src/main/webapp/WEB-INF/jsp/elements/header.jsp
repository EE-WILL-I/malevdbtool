<%@ page import="com.malevdb.Localization.LocalizationManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav>
    <div id="nav_content">
        <a href="${pageContext.request.contextPath}/"><%=LocalizationManager.getString("header.home")%></a>
        | <a href="${pageContext.request.contextPath}/data"><%=LocalizationManager.getString("header.data_view")%></a>
        | <a href="${pageContext.request.contextPath}/load"><%=LocalizationManager.getString("header.load_file")%></a>
        | <a href="${pageContext.request.contextPath}/mail"><%=LocalizationManager.getString("header.mail_srvc")%></a>
        | <a href="${pageContext.request.contextPath}/login"><%=LocalizationManager.getString("header.login")%></a>
        | <a href="#"><%=LocalizationManager.getString("header.about")%></a>
        <div style="float: right; padding-right: 10px;">
            <%=LocalizationManager.getString("header.search")%> <input name="search">
        </div>
    </div>
</nav>
