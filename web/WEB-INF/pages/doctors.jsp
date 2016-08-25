<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@include file="header.jsp"%>
<jsp:useBean id="specializations" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="doctorList" scope="request" class="java.util.ArrayList"/>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Врачи</title>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/simple-sidebar.css" rel="stylesheet">
        <link href="css/sticky-footer-navbar.css" rel="stylesheet">
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <script src="js/bootstrap.js"></script>
    </head>
    <body>
        <div id="wrapper">
        <div class="container content">
            <!-- Sidebar -->
            
            <div id="sidebar-wrapper">
                <ul class="sidebar-nav">
                    <li class="sidebar-brand">

                        Классификация врачей:

                    </li>
                    <c:forEach items="${specializations}" var="spec">
                        <li>
                            <a class="spec" href="/controller?command=GET_DOCTORS_BY_SPECIALIZATION&specialization=${spec.specialization}">${spec.specialization}</a>
                        </li>
                    </c:forEach>
                    <li>
                        <a id="all_doctors" href="#">Все врачи</a>
                    </li>
                </ul>
            </div>
            <script>
                $(".spec").click(function () {
                    thisPageNum = 2;
                    loadUrl = $(this).attr("href")+"&page=";
                    $.get(loadUrl+1, function (data) {
                        $("#doctors").html(data);
                    });
                    load = true;
                    return false;
                });
                $("#all_doctors").click(function () {
                    thisPageNum = 2;
                    loadUrl = "/controller?command=GET_ALL_DOCTORS&page=";
                    $.get(loadUrl+1, function (data) {
                       $("#doctors").html(data);
                    });
                    load = true;
                    return false;
                });
                $("#search_button").click(function () {
                    thisPageNum = 2;
                    var query;
                    $("#query_string").val(function (index, value) {
                        query = value.replace(" ", "%20");
                        return value;
                    });
                    loadUrl = "/controller?command=SEARCH_DOCTORS&query="+query;
                    loadUrl+="&page=";
                    $.get(loadUrl+1, function (data) {
                        $("#doctors").html(data);
                    });
                    load = true;
                    return false;
                });
            </script>
            <h1 class="display_1">Врачи</h1>
            <div class="container content" style="background:white">
                <div id="doctors" class="row">
                    <c:forEach items="${doctorList}" var="doctor">
                        <div class="col-lg-4">
                            <a href="/controller?command=GET_USER_DETAILS&login=${doctor.login}&register_type=${doctor.registrationType}">
                                <h2>${doctor.secondName} ${doctor.firstName}</h2>
                                <img src="/controller?command=GET_USER_IMAGE&login=${doctor.login}&register_type=${doctor.registrationType}" class="img-responsive" width="150" height="200" alt="Фото доктора"/>
                            </a>
                            <br/>
                            <b>Специализация:</b>&nbsp;
                            <span>${doctor.userDescription.specialization}</span>
                        </div>
                    </c:forEach>
                        <div id="LoadedContent"></div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="about-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" align="center">
                        <img class="image-circle img-responsive" src="images/descr.jpg" alt="О проекте"/>  
                        <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть"/>
                            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                    </div>
                    <div class="modal-body" style="height:200; overflow:auto;">
                    <p align="justify">
                    Представляем вашему вниманию онлайн-аптеку.
                    Здесь вы можете заказывать и покупать лекарста. Так же возможно получение рецепта на то или иное лекарство.
                    </p>    
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="contacts-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" align="center">
                        <img class="img-circle img-responsive" src="images/contacts.jpg" alt="Контакты"/>    
                        <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть"/>
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                    </div> 
                    <div class="modal-body">
                        <div class="form_group">
                            <b>Адрес:</b>&nbsp;<span>Минск, ул. Купревича 1/2</span>
                            <br/>
                            <b>Телефон:</b>&nbsp;<span>+375447350720</span>
                            <br/>
                            <b>email:</b>&nbsp;<span><a href="mailto:vladislav.zavadski@gmail.com">vladislav.zavadski@gmail.com</a></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>      
            <footer class="footer">
                <div class="container">
                    <p class="navbar-text pull-left"> 
                        Site Built By <a href="mailto:vladislav.zavadski@gmail.com">Vladislav Zavadski</a>, EPAM Systems, 2016
                    </p>
                    <div class="nav navbar-nav navbar-left" style="line-height:50px">
                        <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#about-modal">О проекте</button>
                        <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#contacts-modal">Контакты</button>
                    </div>
                </div>
            </footer>
            <script>
                $("#menu-toggle").click(function(e) {
                    e.preventDefault();
                    $("#wrapper").toggleClass("toggled");
                });
                var thisPageNum = 2;
                var load = true;
                var loadUrl = "/controller?command=GET_ALL_DOCTORS&page=";
                function downloadContent(){
                    $.get(loadUrl + thisPageNum, function (data) {
                        $("#LoadedContent").html($("#LoadedContent").html() + " " + data);
                        thisPageNum = thisPageNum + 1;
                    });
                }
                $(document).ready(function(){
                    var scrH = $(window).height();
                    var scrHP = $("#drugs").height();
                    $(window).scroll(function(){
                        var scro = $(this).scrollTop();
                        var scrHP = $("#drugs").height();
                        var scrH2 = 0;
                        scrH2 = scrH + scro;
                        var leftH = scrHP - scrH2;

                        if(leftH < 200){
                            downloadContent();
                        }
                    });
                });
            </script>
    </body>
</html>