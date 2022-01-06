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
    <!--Update connection credentials-->
    <div id="dialog_creds" title="update credentials" style="display:none;">
        <form id="update_creds_form" action="/mail/updateCredentials" method="post">
            <fieldset id="field_set">
                <label for="user">User:</label>
                <br/>
                <input type="text" onchange="setNewUser(this.value)" name="new_user" id="new_user" class="text">

                <label for="pass">Password:</label>
                <br/>
                <input type="password" onchange="setNewPass(this.value)" name="new_pass" id="new_pass" class="text">
                <button type="button" onclick="updateCreds()">Update</button>
            </fieldset>
        </form>
    </div>
    <button onclick='showUpdateCredsDialog()'><%=LocalizationManager.getString("mailService.update_creds")%></button>
    <!--Update account credentials-->
    <div id="dialog_acc" title="update credentials" style="display:none;">
            <form id="update_acc_form" action="/mail/updateAccount" method="post">
                <fieldset id="field_set">
                    <label for="new_login">Login:</label>
                    <br/>
                    <input type="text" name="new_login" id="new_login" class="text">

                    <label for="new_pass">Password:</label>
                    <br/>
                    <input type="password" name="new_pass" id="new_pass" class="text">
                    <button type="submit">Update</button>
                </fieldset>
            </form>
        </div>
        <button onclick='showUpdateAccDialog()'><%=LocalizationManager.getString("mailService.update_acc")%></button>
</div>
<jsp:include page="../elements/footer.jsp" />
</body>
<script type="text/javascript">
    function setNewUser(val) {
        document.getElementById("new_user").value = val;
    }
    function setNewPass(val) {
        document.getElementById("new_pass").value = val;
    }
    function showUpdateCredsDialog() {
        $("#dialog_creds").dialog();
    }
    function showUpdateAccDialog() {
        $("#dialog_acc").dialog();
    }
    function updateCreds() {
        document.getElementById("update_creds_form").submit();
    }
</script>
</html>
