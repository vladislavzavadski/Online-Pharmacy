<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@include file="header.jsp"%>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Препараты</title>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/simple-sidebar.css" rel="stylesheet">
        <link href="css/sticky-footer-navbar.css" rel="stylesheet">
        <script src="js/bootstrap.js"></script>
    </head>
    <body>
    <jsp:useBean id="drugList" scope="request" class="java.util.ArrayList"/>
    <jsp:useBean id="drugClasses" scope="request" class="java.util.ArrayList"/>
        <div id="wrapper">
        <div class="container content">
            <!-- Sidebar -->
            <div id="sidebar-wrapper">
                <ul class="sidebar-nav">
                    <li class="sidebar-brand">
                        Классы лекарств:
                    </li>
                    <c:forEach items="${drugClasses}" var="drugClass">
                        <li>
                            <a class="Class" href="/controller?command=GET_DRUGS_BY_CLASS&dr_class=${drugClass.name}&page=1" title="${drugClass.description}">${drugClass.name}</a>
                        </li>
                    </c:forEach>
                    <li>
                        <a id="all_dr" href="/controller?command=GET_ALL_DRUGS&overload=false&page=1">Все классы</a>
                    </li>
                </ul>
            </div>
            <script>
                var thisPageNum = 2;
                $(".Class").click(function () {
                    //alert($(this).attr("href"));
                    var toLoad = $(this).attr("href");
                    //$("#drugs").load(toLoad);
                    $.get(toLoad, function (data) {
                        $("#drugs").html(data);
                    });
                    load = true;
                    thisPageNum = 2;
                    loadUrl="/controller?command=GET_DRUGS_BY_CLASS&dr_class="+$(this).html()+"&page=";
                    return false;
                });
                $("#all_dr").click(function () {
                    var toLoad = $(this).attr("href");
                    $.get(toLoad, function (data) {
                        $("#drugs").html(data);
                    });
                    thisPageNum = 2;
                    load = true;
                    loadUrl="/controller?command=GET_ALL_DRUGS&overload=false&page=";
                    return false;
                });
                $('#search_button').click(function () {
                    var query;
                    $('#query_string').val(function (index, value) {
                        query = value.replace(" ", "%20");
                        return value;
                    });
                    thisPageNum=2;
                    load = true;
                    var toLoad = "/controller?command=SEARCH_DRUGS&query="+query+"&page=1";
                    $.get(toLoad, function (data) {
                        $('#drugs').html(data);
                    });
                    loadUrl = "/controller?command=SEARCH_DRUGS&query="+query+"&page=";
                    return false;
                });
            </script>
            <h1 class="display_1">Препараты</h1>
            <div class="btn-toolbar">
                <div class="btn-group btn-group-sm">
                    <a type="button" href="#" class="btn btn-default">А</a>
                    <a type="button" href="#" class="btn btn-default">Б</a>
                    <a type="button" href="#" class="btn btn-default">В</a>
                    <a type="button" href="#" class="btn btn-default">Г</a>
                    <a type="button" href="#" class="btn btn-default">Д</a>
                    <a type="button" href="#" class="btn btn-default">Е</a>
                    <a type="button" href="#" class="btn btn-default">Ж</a>
                    <a type="button" href="#" class="btn btn-default">З</a>
                    <a type="button" href="#" class="btn btn-default">И</a>
                    <a type="button" href="#" class="btn btn-default">Й</a>
                    <a type="button" href="#" class="btn btn-default">К</a>
                    <a type="button" href="#" class="btn btn-default">Л</a>
                    <a type="button" href="#" class="btn btn-default">М</a>
                    <a type="button" href="#" class="btn btn-default">Н</a>
                    <a type="button" href="#" class="btn btn-default">О</a>
                    <a type="button" href="#" class="btn btn-default">П</a>
                    <a type="button" href="#" class="btn btn-default">Р</a>
                    <a type="button" href="#" class="btn btn-default">С</a>
                    <a type="button" href="#" class="btn btn-default">Т</a>
                    <a type="button" href="#" class="btn btn-default">У</a>
                    <a type="button" href="#" class="btn btn-default">Ф</a>
                    <a type="button" href="#" class="btn btn-default">Х</a>
                    <a type="button" href="#" class="btn btn-default">Ц</a>
                    <a type="button" href="#" class="btn btn-default">Ч</a>
                    <a type="button" href="#" class="btn btn-default">Ш</a>
                    <a type="button" href="#" class="btn btn-default">Щ</a>
                    <a type="button" href="#" class="btn btn-default">Э</a>
                    <a type="button" href="#" class="btn btn-default">Ю</a>
                    <a type="button" href="#" class="btn btn-default">Я</a>
                </div>
            </div>
            <div id="drugs" class="row" align="justify" style="background:white;">
                <c:forEach items="${drugList}" var="drugItem">
                    <div class="col-xs-6 col-lg-6" style="height:400px; overflow:hidden">
                        <a href="/controller?command=GET_DRUG_DETAILS&dr_id=${drugItem.id}">
                            <h2>${drugItem.name}
                                <span class="label label-success">$${drugItem.price}</span>
                            </h2>
                            <img src="/controller?command=GET_DRUG_IMAGE&dr_id=${drugItem.id}" class="img-responsive" alt="${drugItem.name}" height="150" width="150"/>
                        </a>
                        <b>
                            Класс лекарства:
                        </b>&nbsp;
                        <span title="${drugItem.drugClass.description}">
                                ${drugItem.drugClass.name}
                        </span>
                        <br/>
                        <b>
                            Активное вещество:
                        </b>&nbsp;
                        <span>
                                ${drugItem.activeSubstance}
                        </span>
                        <br/>
                        <p>${drugItem.description}</p>

                    </div>
                </c:forEach>
                <c:if test="${drugList.size()eq 0}">
                    <h2>По запросу "${param.dr_class}" ничего не найдено</h2>
                </c:if>
                <c:choose>
                    <c:when test="${drugList.size() ne 6}">
                        <div id="stop" data-stop="${drugList.size()<6}"></div>
                    </c:when>
                    <c:otherwise>
                        <div id="LoadedContent" data-stop="${drugList.size()<6}"></div>
                    </c:otherwise>
                </c:choose>
                <script>
                    var load = true;
                    var thisWork = true;
                    var loadUrl = "/controller?command=GET_ALL_DRUGS&overload=false&page="
                    function downloadContent(){
                        if(thisWork) {
                            thisWork = false;
                                $.get(loadUrl + thisPageNum, function (data) {
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
                            var scrHP = $("#drugs").height();
                            var scrH2 = 0;
                            scrH2 = scrH + scro;
                            var leftH = scrHP - scrH2;

                            if(leftH < 200){
                                downloadContent();
                            }
                        });
                    });

                    $("#ext_search").click(function () {
                        var url = "/controller?command=EXTENDED_DRUG_SEARCH";
                        url+="&name="+$("#drug_name").val();
                        url+="&active_substance="+$("#active_substance").val();
                        url+="&max_price="+$("#dr_price").val();
                        url+="&dr_class="+$("#dr_class").val();
                        url+="&dr_manufacturer="+$("#dr_man").val();
                        if($("#in_stock_only").attr("checked")=='checked') {
                            url += "&only_in_stock=" + $("#in_stock_only").val();
                        }
                        if($("#without_prescription").attr("checked")=='checked') {
                            url += "&only_free=" + $("#without_prescription").val();
                        }
                        url+="&page=";
                        $.get(url+1, function (data) {
                            $("#drugs").html(data);
                        });
                        thisPageNum=2;
                        loadUrl=url;
                        $("#toggler_call").attr("checked", false);
                        return false;
                    });
                </script>

                <!--/span-->
            </div>
            <!--/row-->

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
            </script>
    </body>
</html>