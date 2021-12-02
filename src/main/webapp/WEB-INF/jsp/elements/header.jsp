<%@ page import="com.malevdb.Localization.LocalizationManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav>
    <div id="nav_content">
        <a href="/"><%=LocalizationManager.getString("header.home")%></a>
        | <a href="/data"><%=LocalizationManager.getString("header.data_view")%></a>
        | <a href="/load"><%=LocalizationManager.getString("header.load_file")%></a>
        | <a href="/mail"><%=LocalizationManager.getString("header.mail_srvc")%></a>
        | <a href="/login"><%=LocalizationManager.getString("header.login")%></a>
        | <a href="#"><%=LocalizationManager.getString("header.about")%></a>
        <div style="float: right; padding-right: 10px;">
            <%=LocalizationManager.getString("header.search")%> <input name="search">
        </div>
    </div>
</nav>
