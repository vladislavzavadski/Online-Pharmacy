<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@include file="header.jsp"%>
<jsp:useBean id="specializations" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="doctorList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
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
        <style>
            #notifies {
                position:fixed;
                width:400px;
                height:auto;
                top:100px;
                right:20px;
            }
        </style>
    </head>
    <body>
        <div id="wrapper">
        <div class="container content">
            <!-- Sidebar -->
            <div id="notifies"></div>
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
            <c:if test="${user.userRole eq 'PHARMACIST'}">
                <button data-toggle="modal" data-target="#registration-modal" class="btn btn-default btn-success">Зарегистрировать врача</button>
            </c:if>
            <div class="container content" style="background:white">
                <div id="doctors" class="row">
                        <jsp:include page="/doctor"/>
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
                var thisWork=1;
                var loadUrl = "/controller?command=GET_ALL_DOCTORS&page=";
                function downloadContent(){
                    if(thisWork == 1){
                        thisWork = 0;
                        $.get(loadUrl + thisPageNum, function (data) {
                            $("#LoadedContent").html($("#LoadedContent").html()+" "+data);
                            thisPageNum = thisPageNum + 1;
                            thisWork = 1;
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
            </script>
            <c:if test="${user.userRole eq 'PHARMACIST'}">
            <div class="modal fade" id="registration-modal" tabindex="-1" role="dialog"  aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                            <img class="img-circle img-responsive" id="img_logo" src="images/reg.png" alt="Регистрация">
                        </div>
                        <form id="register-form" method="post" action="/controller">
                            <input name="command" type="hidden" value="DOCTOR_REGISTRATION">
                            <div class="modal-body">
                                <div id="div-register-msg">
                                    <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                                    <span id="text-register-msg">Регистрация аккаунта</span>
                                </div>
                                <div class="form_group">
                                    <label for="register_username">Логин: </label>
                                    <input max="30" id="register_username" class="form-control" type="text" placeholder="Логин" required name="login" autofocus>
                                    <span id="login_message"></span>
                                </div>
                                <div class="form_group">
                                    <label for="register_first_name">Имя: </label>
                                    <input max="30" id="register_first_name" class="form-control" type="text" placeholder="Имя" required name="first_name">
                                </div>
                                <div class="form_group">
                                    <label for="register_second_name">Фамилия: </label>
                                    <input max="30" id="register_second_name" class="form-control" type="text" placeholder="Фамилия" required name="second_name">
                                </div>
                                <div class="form_group">
                                    <label for="register_email">E-mail: </label>
                                    <input max="45" id="register_email" class="form-control" type="email" placeholder="E-Mail" required name="email">
                                </div>
                                <div class="form_group">
                                    <label for="specialization">Специализация:</label>
                                    <input max="20" id="specialization" class="form-control" type="text" placeholder="Специализация" required name="specialization">
                                </div>
                                <div class="form_group">
                                    <label for="description">Описание врача:</label>
                                    <textarea maxlength="100" required id="description" class="form-control" name="description" placeholder="Описание"></textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <div>
                                    <button type="submit" id="reg_button" class="btn btn-primary btn-lg btn-block" disabled>Зарегистрироваться</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                $("#register_username").blur(function () {
                    $.ajax({
                        url:"controller",
                        type:"POST",
                        dataType:'json',
                        success:function (data) {
                            if(data.isExist==false){
                                $("#login_message").html("<span style=\"color:green\">Данный логин свободен</span>");
                                $("#reg_button").prop("disabled", false);
                            }
                            else {
                                $("#login_message").html("<span style=\"color:red\">Данный логин занят</span>");
                                $("#reg_button").prop("disabled", true);
                            }

                        },
                        data: {login: $("#register_username").val(), command:"CHECK_LOGIN"}
                    });
                });
                $('#register-form').submit(function () {
                    $.ajax({
                        url:'controller',
                        type:'POST',
                        dataType:'json',
                        data:$(this).serialize(),
                        success: function (data) {
                            $('#login_message').html("");
                            $('#registration-modal').modal('toggle');
                            $(this).trigger('reset');
                            if(data.result==true){
                                Notify.generate('Новый врач успешно зарегестрирован', 'Готово', 1);

                            }
                            else {
                                Notify.generate('Ответ сервера '+data.message, 'Ошибка', 3);
                            }
                        }
                    });
                    return false;
                });

                Notify = {
                    TYPE_INFO: 0,
                    TYPE_SUCCESS: 1,
                    TYPE_WARNING: 2,
                    TYPE_DANGER: 3,

                    generate: function (aText, aOptHeader, aOptType_int) {
                        var lTypeIndexes = [this.TYPE_INFO, this.TYPE_SUCCESS, this.TYPE_WARNING, this.TYPE_DANGER];
                        var ltypes = ['alert-info', 'alert-success', 'alert-warning', 'alert-danger'];

                        var ltype = ltypes[this.TYPE_INFO];
                        if (aOptType_int !== undefined && lTypeIndexes.indexOf(aOptType_int) !== -1) {
                            ltype = ltypes[aOptType_int];
                        }

                        var lText = '';
                        if (aOptHeader) {
                            lText += "<h4>"+aOptHeader+"</h4>";
                        }
                        lText += "<p>"+aText+"</p>";

                        var lNotify_e = $("<div class='alert "+ltype+"'><button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>×</span></button>"+lText+"</div>");
                        setTimeout(function () {
                            lNotify_e.alert('close');
                        }, 3000);
                        lNotify_e.appendTo($("#notifies"));
                    }
                };
            </script>
            </c:if>
    </body>
</html>