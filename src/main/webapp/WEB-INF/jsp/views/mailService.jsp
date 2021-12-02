<%@ page import="com.malevdb.Localization.LocalizationManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Mail Service</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    <link rel="stylesheet" href="http://ajax.aspnetcdn.com/ajax/jquery.ui/1.10.3/themes/sunny/jquery-ui.css">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <script src="//ajax.aspnetcdn.com/ajax/jquery.ui/1.10.3/jquery-ui.min.js"></script>
</head>
<body onload="init()">
<jsp:include page="../elements/header.jsp"/>
<jsp:include page="../elements/popup.jsp"/>
<div class="content_holder">
    <h2><%=LocalizationManager.getString("mailService.header")%></h2>
    <form id="mail_form" action="${pageContext.request.contextPath}/mail/send" method="post">
        <label for="recipients"><%=LocalizationManager.getString("mailService.recipients")%></label>
        <input id="recipients" name="recipients" type="text">
        <br/>
        <label for="subject"><%=LocalizationManager.getString("mailService.subject")%></label>
        <input id="subject" name="subject" type="text">
        <br/>
        <label for="message"><%=LocalizationManager.getString("mailService.content")%></label>
        <input id="message" name="message" type="text">
        <br/>
        <button type="submit" value="Send"><%=LocalizationManager.getString("mailService.send")%></button>
    </form>
</div>
<jsp:include page="../elements/footer.jsp" />
</body>
</html>
