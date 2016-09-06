<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 06.09.16
  Time: 11:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp"%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Запросы на рецепты</title>
    <!-- Bootstrap -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/simple-sidebar.css" rel="stylesheet">
    <link href="css/sticky-footer-navbar.css" rel="stylesheet">
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/js/bootstrap-datepicker.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/css/bootstrap-datepicker3.css"/>
    <![endif]-->
    <script src="js/bootstrap.js"></script>
    <script>
        $(document).ready(function(){
            var date_input=$('input[class=date]'); //our date input has the name "date"
            var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
            date_input.datepicker({
                format: 'mm/dd/yyyy',
                container: container,
                todayHighlight: true,
                autoclose: true,
                disableEntry: true,
            }).on('changeDate', function () {
                if($(this).attr("id")=='from_date'){
                    if($("#to_date").val()!=""){
                        if(new Date($(this).val())>new Date($('#to_date').val())){
                            $(this).val("");
                        }
                    }
                }
                else {
                    if($("#from_date").val()!=""){
                        if(new Date($(this).val())<new Date($("#from_date").val())){
                            $(this).val("");
                        }
                    }
                }
            })
        });
    </script>
    <jsp:useBean id="requests" scope="request" class="java.util.ArrayList"/>
</head>
<body>
    <div class="container content">
        <h1 class="display_1">Запросы</h1>
        <form id="req_form">
            <nobr>
            <label for="drug_name">Название лекарства:</label>
            <input id="drug_name" type="text" name="drug_name">

                <label for="from_date">Заказано с:</label>
                <input class="date" id="from_date" type="text" name="date_from">
                <label for="to_date">Заказано до:</label>
                <input class="date" id="to_date" type="text" name="date_to">
            </nobr>
                <label for="pr_status">Статус запроса</label>
                <select id="pr_status" name="status">
                    <option value="" selected>Неважно</option>
                    <option value="in_progress">В обработке</option>
                    <option value="denied">Отказано</option>
                    <option value="confirmed">Одобрено</option>
                </select>
                <button id="search_by_date" class="btn btn-primary btn-primary">Найти</button>

        </form>
        <script>
            var url = "/controller?command=GET_REQUESTS&overload=false&";
            var loadUrl = url+"&page=";
            var thisPageNum = 2;
            $('#req_form').submit(function () {
                currentUrl=url+$(this).serialize()+"&page=";
                $.get(currentUrl+1, function (data) {
                    $('#requests').html(data);
                });
                thisPageNum = 2;
                return false;
            });
        </script>
        <div class="container" style="background:white">
            <div class="row border-between" id="requests">
                <jsp:include page="/request"/>
            </div>
        </div>
        <script>
            var thisWork = true;
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
                $(window).scroll(function(){
                    var scro = $(this).scrollTop();
                    var scrHP = $("#requests").height();
                    var scrH2 = 0;
                    scrH2 = scrH + scro;
                    var leftH = scrHP - scrH2;

                    if(leftH < 200){
                        downloadContent();
                    }
                });
            });
        </script>
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
</body>
</html>
