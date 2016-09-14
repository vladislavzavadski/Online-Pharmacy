<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@include file="header.jsp"%>
<jsp:useBean id="messageList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Сообщения</title>
    <!-- Bootstrap -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <link href="../../css/bootstrap.css" rel="stylesheet">
    <link href="../../css/style.css" rel="stylesheet">
    <link href="../../css/simple-sidebar.css" rel="stylesheet">
    <link href="../../css/sticky-footer-navbar.css" rel="stylesheet">
    <link href="../../css/messages.css" rel="stylesheet">
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/js/bootstrap-datepicker.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/css/bootstrap-datepicker3.css"/>
    <![endif]-->
    <script src="../../js/bootstrap.js"></script>
    <style>
        #notifies {
            position:fixed;
            width:400px;
            height:auto;
            top:100px;
            right:20px;
        }
    </style>
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
</head>
<body>

<div id="wrapper">
    <div class="container content">
        <!-- Sidebar -->
        <div id="notifies"></div>
        <div id="sidebar-wrapper">
            <ul class="sidebar-nav">
                <li class="sidebar-brand">
                    Сообщения:
                </li>
                <c:if test="${user.userRole eq 'CLIENT'}">
                    <li>
                        <a href="#" class="status" data-status="IN_PROGRESS">В обработке</a>
                    </li>
                </c:if>
                <li>
                    <a href="#" class="status" data-status="NEW">Новые</a>
                </li>
                <li>
                     <a href="#" class="status" data-status="COMPLETED">Прочитанные</a>
                </li>
                <li>
                    <a href="#" class="status" data-status="">Все сообщения</a>
                </li>
            </ul>
        </div>
        <h1 class="display_1">Сообщения</h1>
        <form>
            <nobr>
                <label for="from_date">Отправлено с:</label>
                <input class="date" id="from_date" type="text" name="order_from">
                <label for="to_date">Отправлено до:</label>
                <input class="date" id="to_date" type="text" name="order_to">
                <button id="search_by_date" class="btn btn-primary btn-primary">Найти</button>
            </nobr>
        </form>
        <script>
            var url = "/controller?command=GET_ALL_MESSAGES&overload=false";
            var currentStatus = "";
            $(".status").click(function () {
                var requestUrl = url;
                currentStatus = $(this).attr('data-status');
                if(currentStatus==""){
                    $("#to_date").val("");
                    $("#from_date").val("");
                }
                requestUrl+="&message_status="+currentStatus;
                requestUrl+='&date_to='+$("#to_date").val();
                requestUrl+='&date_from='+$("#from_date").val();
                requestUrl+="&page=";
                currentPage = 2;
                loadUrl = requestUrl
                $.get(requestUrl+1, function (data) {
                    $("#messages").html(data);
                });
                return false;
            });

            $("#search_by_date").click(function () {
                var requestUrl = url;
                requestUrl+="&message_status="+currentStatus;
                requestUrl+='&date_to='+$("#to_date").val();
                requestUrl+='&date_from='+$("#from_date").val();
                requestUrl+="&page=";
                currentPage = 2;
                loadUrl = requestUrl;
                $.get(requestUrl+1, function (data) {
                    $("#messages").html(data);
                });
                return false;
            });
        </script>
        <div class="container content">
            <div id="messages" class="row">
                <jsp:include page="/message"/>
            </div>
        </div>
        <script>
            $("#messages").on('click', '.change_status', function () {
                var parent = $(this).parent();
                var messageId = $(this).attr('data-id');
                $.ajax({
                    url: 'controller',
                    type: 'POST',
                    dataType:'json',
                    data:{command:'MARK_MESSAGE', me_id:messageId},
                    success:function (data) {
                        if(data.result==true){
                            parent.html("<span style=\"color:green\">Прочитано!</span>");
                        }

                    }
                });
                return false;
            });
        </script>
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
        <c:if test="${user.userRole eq 'DOCTOR'}">
            <div class="modal fade" id="response-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display:none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть"/>
                            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                        </div>
                        <form class="form-horizontal" id="resp-form">
                            <div class="modal-body">
                                <b>Кому:</b>
                                <span id="receiver"></span>
                                <br/>

                                <input name="me_id" type="hidden" id="message_id">
                                <input type="hidden" name="command" value="ANSWER_MESSAGE">
                                <label for="response_message">Ответ</label>
                                <textarea id="response_message" class="form-control" name="rec_message" placeholder="Ответ" required></textarea>

                                </div>
                            <div class="modal-footer">
                                <input type="submit" class="btn btn-primary" value="Отправить">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                var parent;
                $('#messages').on('click', '.response-button', function () {
                    $('#message_id').val($(this).attr('data-message'));
                    $('#receiver').html($(this).attr('data-receiver'));
                    parent = $(this).parent();
                    $('#response_message').val("");
                });
                $('#resp-form').submit(function () {
                    var data = $(this).serialize();
                    $.ajax({
                        url:'controller',
                        type:'POST',
                        dataType:'json',
                        data:data,
                        success:function (data) {
                            if(data.result==true){
                                Notify.generate('Сообщение успешно отправлено', 'Готово', 1);
                                $('#response-modal').modal('toggle');
                                parent.html($('#response_message').val());
                            }
                            else {
                                Notify.generate('Ошибка при отправке сообщения', 'Ошибка', 3);
                            }
                        }
                    });
                    return false;
                });

            </script>
        </c:if>
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
            var currentPage = 2;
            var loadUrl = "/controller?command=GET_ALL_MESSAGES&overload=false&page=";
            var thisWork = true;
            function downloadContent(){
                if(thisWork) {
                    thisWork = false;
                    $.get(loadUrl + currentPage++, function (data) {
                        $("#LoadedContent").html($("#LoadedContent").html() + " " + data);
                        thisWork = true;
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
        <script>
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
        <c:if test="${user.userRole eq 'CLIENT' or user.userRole eq 'DOCTOR'}">
            <script src="js/sendRequest.js"></script>
        </c:if>
        <c:if test="${user.userRole eq 'DOCTOR'}">
            <script src="js/requestsForPrescription.js"></script>
        </c:if>
</body>
</html>