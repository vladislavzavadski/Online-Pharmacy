<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@include file="header.jsp"%>
<jsp:useBean id="orderList" scope="request" class="java.util.ArrayList"/>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Заказы</title>
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
        <style>
            #notifies {
                position:absolute;
                width:400px;
                height:auto;
                top:100px;
                right:20px;
            }
        </style>
        <script>
            var loadUrl = "/controller?command=GET_ALL_ORDERS&overload=false";
            var status = "&or_status=";
            var currentStatus = "";
            var dateFrom = "&date_from=";
            var dateTo = "&date_to=";
            var drugName = "&drug_name=";
            var currentPage=2;

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
            <div id="notifies"></div>
            <!-- Sidebar -->
            <div id="sidebar-wrapper">
                <ul class="sidebar-nav">
                    <li class="sidebar-brand">
                        <a href="#">
                        Статус заказа:
                        </a>
                    </li>
                    <li>
                        <a class="status" data-status="ORDERED" href="#">Заказано</a>
                    </li>
                    <li>
                        <a class="status" data-status="CANCELED" href="#">Отменено</a>
                    </li>
                    <li>
                        <a class="status" data-status="PAID" href="#">Оплачено</a>
                    </li>
                    <li>
                        <a id="all_orders" href="#">Все заказы</a>
                    </li>
                </ul>
            </div>

            <h1 class="display_1">Заказы</h1>
            <form>
                <label for="drug_name">Название лекарства:</label>
                <input id="drug_name" type="text" name="drug_name">
                <nobr>
                    <label for="from_date">Заказано с:</label>
                    <input class="date" id="from_date" type="text" name="order_from">
                    <label for="to_date">Заказано до:</label>
                    <input class="date" id="to_date" type="text" name="order_to">
                    <button id="search_by_date" class="btn btn-primary btn-primary">Найти</button>
                </nobr>
            </form>
            <div class="container" style="background:white">
                <div id="orders" class="row">
                    <c:forEach items="${orderList}" var="order">
                    <div id="${order.id}" class="col-xs-12 col-lg-6" >
                        <div class="row">
                            <div class="col-lg-4" style="height:200px; overflow:hidden">
                                <a href="/controller?command=GET_DRUG_DETAILS&dr_id=${order.drug.id}">
                                    <h2>${order.drug.name}</h2>
                                    <img src="/controller?command=GET_DRUG_IMAGE&dr_id=${order.drug.id}" class="img-responsive" alt="${order.drug.name}" height="150" width="100"/>
                                </a>
                            </div>
                            <div class="col-lg-4" style="padding-top:40px;">
                                <c:choose>
                                    <c:when test="${order.orderStatus eq 'ORDERED'}">
                                        <b>Статус:</b>&nbsp;<span class="label label-warning">Заказано</span>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${order.orderStatus eq 'PAID'}">
                                                <b>Статус:</b>&nbsp;<span class="label label-success">Оплачено</span>
                                            </c:when>
                                            <c:otherwise>
                                                <b>Статус:</b>&nbsp;<span class="label label-danger">Отменено</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                                <br/>
                                <b>Дозировка:</b>&nbsp;
                                <span>${order.drugDosage}мг</span>
                                <br/>
                                <b>Количество:</b>&nbsp;
                                <span>${order.drugCount}</span>
                                <br/>
                                <b>Сумма: </b>&nbsp;
                                <span>$${order.totalSum}</span>
                                <br/>
                                <b>Дата заказа: </b>&nbsp;
                                <span>${order.orderDate}</span>
                            </div>
                            <c:if test="${order.orderStatus eq 'ORDERED'}">
                                <div class="col-lg-4" style="padding-top:40px;">
                                    <a class="btn btn-primary btn-danger cancel_order" href="/controller" data-order="${order.id}">Отменить</a>
                                    <a href="#" class="btn btn-primary btn-success">Оплатить</a>
                                </div>
                            </c:if>
                        </div>         
                    </div>
                    </c:forEach>
                    <div id="LoadedContent"></div>
                </div>
            </div>
        </div>
            <script>
                $(".status").click(function () {
                    currentStatus = $(this).attr("data-status");
                    var url = loadUrl+status+currentStatus;
                    var name;
                    url+=dateFrom+$("#from_date").val();
                    url+=dateTo+$("#to_date").val();
                    $("#drug_name").val(function (index, value) {
                        name = value.replace(" ", "%20");
                        return value;
                    });
                    url+=drugName+name;
                    url+="&page=";
                    paginationUrl=url;
                    $.get(url+1, function (data) {
                        $("#orders").html(data);
                    });
                    thisPageNum = 2;
                });
                $("#search_by_date").click(function () {
                    var url=loadUrl+status+currentStatus;
                    var name;
                    url+=dateFrom+$("#from_date").val();
                    url+=dateTo+$("#to_date").val();
                    $("#drug_name").val(function (index, value) {
                        name = value.replace(" ", "%20");
                        return value;
                    });
                    url+=drugName+name;
                    url+="&page=";
                    paginationUrl=url;
                    $.get(url+1, function (data) {
                        $("#orders").html(data);
                    })value;
                    thisPageNum = 2;
                    return false;
                });
                $("#all_orders").click(function () {
                    currentStatus = "";
                    $("#from_date").val("");
                    $("#to_date").val("");
                    $("#drug_name").val("");
                    var url=loadUrl+status+currentStatus;
                    url+="&page=";
                    paginationUrl=url;
                    $.get(url+1, function (data) {
                        $("#orders").html(data);
                    })
                    thisPageNum = 2;
                });

                var thisPageNum = 2;
                var paginationUrl = "/controller?command=GET_ALL_ORDERS&overload=false&page="
                function downloadContent(){
                    $.get(paginationUrl + thisPageNum, function (data) {
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

                $("#orders").on('click', '.cancel_order', function () {
                    var parent = $(this).parent().parent();
                    var buttons = $(this).parent();
                    $.ajax({
                        url: 'controller',
                        dataType:'json',
                        type: 'POST',
                        data: {command:'CANCEL_ORDER', order_id:$(this).attr('data-order')},
                        success:function (data) {
                            if(data.result){
                                Notify.generate('Ваш заказ успешно удален', 'Успешно', 1);
                                if(currentStatus=="ORDERED") {
                                    parent.hide('slow');
                                }else if(currentStatus==""){
                                    var child = parent.find('.label-warning');
                                    child.removeClass('label-warning');
                                    child.html("Отменено");
                                    child.addClass('label-danger');
                                    buttons.hide('slow');
                                }
                            }
                            else {
                                Notify.generate('Заказ не был найден', 'Ошибка', 3);
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
            <div class="modal fade" id="order-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                            <img class="img-circle img-responsive" id="img_logo" src="images/order.jpg" alt="Заказ">
                        </div>
                        <form id="register-form">
                            <div class="modal-body">
                                <div id="div-register-msg">
                                    <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                                    <span id="text-register-msg">Заказ лекарства</span>
                                </div>
                                <div class="form_group">    
                                    <label for="dosage">Дозировка: </label>
                                    <select id="dosage" class="form-control">
                                        <option value="one">150</option>
                                        <option value="two">300</option>
                                        <option value="three">500</option>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label for="drug_number">Количество: </label>     
                                    <input id="drug_number" class="form-control" type="number" placeholder="Количество" step="0.2" min="1" max="20" required>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <div>
                                    <button type="submit" class="btn btn-primary btn-lg btn-block">Заказать</button>
                                </div>
                            </div>
                        </form>
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
</html>