<%@ page import="com.malevdb.Utils.PropertyType" %>
<%@ page import="com.malevdb.Utils.PropertyReader" %>
<%@ page import="com.malevdb.Database.DataTable" %>
<%@ page import="com.malevdb.Localization.LocalizationManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%DataTable table = (DataTable) pageContext.getRequest().getAttribute("table");%>
<html>
<head>
    <title>Data view</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    <link rel="stylesheet" href="http://ajax.aspnetcdn.com/ajax/jquery.ui/1.10.3/themes/sunny/jquery-ui.css">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <script src="//ajax.aspnetcdn.com/ajax/jquery.ui/1.10.3/jquery-ui.min.js"></script>
</head>
<body onload="init()">
<jsp:include page="../elements/header.jsp"/>
<jsp:include page="../elements/popup.jsp"/>
<div class="content_holder">
    <form id="form" action="${pageContext.request.contextPath}/data" method="GET">
        <div>
            <h3><%=LocalizationManager.getString("dataView.select_a_table")%></h3>
            <jsp:include page="../elements/tableSelect.jsp"/>
            <button type="button" onclick="loadTable()"><%=LocalizationManager.getString("dataView.load_table")%></button>
        </div>
        <div>
            <jsp:include page="../elements/interactiveTable.jsp"/>
        </div>
    </form>
    <div id="dialog" title="Add new data" style="display:none;">
        <form id="insert_form" action="/data/insert/json/<%=table.getName()%>" method="post">
            <input type="hidden" name="new_data" id="new_data">
            <fieldset id="field_set">
                <%for(String column : table.getColumnLabels()) {%>
                <label for="in_<%=column%>"><%=column%></label>
                <br/>
                <input type="text" onchange="setDialogValue('<%=column%>',this.value)" name="<%=column%>" id="in_<%=column%>" class="text">
                <%}%>
                <button type="button" onclick="submitNewData()">Submit</button>
            </fieldset>
        </form>
    </div>
    <button onclick='addData()'><%=LocalizationManager.getString("dataView.add_row")%></button>
</div>
<jsp:include page="../elements/footer.jsp" />
</body>
<script type="text/javascript">
    function loadTable() {
        const form = document.getElementById("form");
        form.setAttribute("method", "GET");
        form.action = form.action.concat("/" + document.getElementById("selected_table").value);
        form.submit();
    }

    function addData() {
        var data = [];
        <%for(String column : table.getColumnLabels()) {%>
        data.push({"column":"<%=column%>", "newValue":"null"})
        <%}%>
        sessionStorage.setItem('new_row_data', JSON.stringify(data));
        $("#dialog").dialog();
    }

    function setDialogValue(column, value) {
        var data = JSON.parse(sessionStorage.getItem("new_row_data"));
        for(var i = 0; i < data.length; i++) {
            if(data[i].column === column) {
                data[i].newValue = value;
                sessionStorage.setItem('new_row_data', JSON.stringify(data));
                return;
            }
        }
        data.push({"column":column.toString(), "newValue":value.toString()});
        sessionStorage.setItem('new_row_data', JSON.stringify(data));
    }

    function submitNewData() {
        document.getElementById("new_data").value = sessionStorage.getItem("new_row_data").toString();
        document.getElementById("insert_form").submit();
    }
</script>
</html>
