<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 05.09.16
  Time: 23:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp"%>
<%@include file="footer.jsp"%>
<jsp:useBean id="prescriptions" scope="request" class="java.util.ArrayList"/>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Рецепты</title>
    <!-- Bootstrap -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet">
    <script src="js/bootstrap-datetimepicker.min.js"></script>
    <link rel="stylesheet" href="https://formden.com/static/cdn/bootstrap-iso.css" />

    <!--Font Awesome (added because you use icons in your prepend/append)-->
    <link rel="stylesheet" href="https://formden.com/static/cdn/font-awesome/4.4.0/css/font-awesome.min.css" />

    <!-- Inline CSS based on choices in "Settings" tab -->
    <style>.bootstrap-iso .formden_header h2, .bootstrap-iso .formden_header p, .bootstrap-iso form{font-family: Arial, Helvetica, sans-serif; color: black}.bootstrap-iso form button, .bootstrap-iso form button:hover{color: white !important;} .asteriskField{color: red;}</style>

    <script type="text/javascript" src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/simple-sidebar.css" rel="stylesheet">
    <link href="css/sticky-footer-navbar.css" rel="stylesheet">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script src="js/bootstrap.js"></script>
</head>
<body>

    <div class="container content">

        <h1 class="display_1">Рецепты</h1>
        <form id="presc_form">
            <label for="drug_name">Название лекарства:</label>
            <input id="drug_name" type="text" name="drug_name">
            <nobr>
                <label for="pr_status">Статус рецепта</label>
                <select id="pr_status" name="pr_status">
                    <option value="" selected>Неважно</option>
                    <option value="ACTIVE">Действует</option>
                    <option value="NON_ACTIVE">Истёк</option>
                </select>
                <button id="search_by_date" class="btn btn-primary btn-primary">Найти</button>
            </nobr>
        </form>
        <script>
            var loadUrl = "/controller?command=${param.command}&pr_status=${param.pr_status}&drug_name=${param.drug_name}&";
            var currentUrl;
            var thisPageNum = 2;
            $('#presc_form').submit(function () {
                var data = $(this).serialize();
                currentUrl = loadUrl+data;

                $.get(currentUrl+"&page="+1, function (data) {
                    $('#prescriptions').html(data);
                });

                var pushedPage = {foo:"bar"}
                window.history.pushState(pushedPage, "page", currentUrl+"&page="+1+"&overload=true");
                thisPageNum = 2;
                return false;
            });
        </script>
        <div class="container" style="background:white">
            <jsp:useBean id="today" class="java.util.Date" />
            <div class="row" id="prescriptions">
                <jsp:include page="/prescription"/>
                <div id="LoadedContent"></div>
            </div>
        </div>
    </div>
<script>
    var thisWork = true;
    function downloadContent(){
        if(thisWork) {
            thisWork = false;
            $.get(loadUrl+"&page=" + thisPageNum, function (data) {
                $("#LoadedContent").html($("#LoadedContent").html() + " " + data);
                thisWork = true;
                thisPageNum = thisPageNum + 1;
            });


        }

    }
    $(document).ready(function(){
        var scrH = $(window).height();
        var scrHP = $("#drugs").height();
        $(window).scroll(function(){
            var scro = $(this).scrollTop();
            var scrHP = $("#prescriptions").height();
            var scrH2 = 0;
            scrH2 = scrH + scro;
            var leftH = scrHP - scrH2;

            if(leftH < 200){
                downloadContent();
            }
        });
    });
</script>
    <c:if test="${user.userRole eq 'CLIENT' or user.userRole eq 'DOCTOR'}">
        <script src="js/sendRequest.js"></script>
    </c:if>
    <c:if test="${user.userRole eq 'DOCTOR'}">
        <script src="js/requestsForPrescription.js"></script>
    </c:if>
    <script src="js/switchLocale.js"></script>
</body>
</html>
