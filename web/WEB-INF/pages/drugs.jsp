<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@include file="header.jsp"%>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta contentType="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Препараты</title>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/simple-sidebar.css" rel="stylesheet">
        <link href="css/sticky-footer-navbar.css" rel="stylesheet">
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
    <jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
    <jsp:useBean id="drugList" scope="request" class="java.util.ArrayList"/>
    <jsp:useBean id="drugClasses" scope="request" class="java.util.ArrayList"/>
    <jsp:useBean id="specializations" scope="request" class="java.util.ArrayList"/>
        <div id="wrapper">
        <div class="container content">
            <div id="notifies"></div>
            <!-- Sidebar -->
            <div id="sidebar-wrapper">
                <ul class="sidebar-nav">
                    <li class="sidebar-brand">
                        Классы лекарств:
                    </li>
                    <div id="drug_classes">
                        <c:forEach items="${drugClasses}" var="drugClass">
                            <li>
                                <a class="Class" href="/controller?command=GET_DRUGS_BY_CLASS&dr_class=${drugClass.name}&page=1" title="${drugClass.description}">${drugClass.name}</a>
                            </li>
                        </c:forEach>
                    </div>
                    <li>
                        <a id="all_dr" href="/controller?command=GET_ALL_DRUGS&overload=false&page=1">Все классы</a>
                    </li>
                </ul>
            </div>
            <script>
                var thisPageNum = 2;
                $('#drug_classes').on('click', '.Class',function () {
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
            <c:if test="${user.userRole eq 'PHARMACIST'}">
                <div class="btn-group" style="padding-top:20px;" role="group">
                    <button data-toggle="modal" data-target="#new-drug-modal" class="btn btn-primary btn-primary">Добавить лекарство</button>
                    <button data-toggle="modal" data-target="#new-drug-class-modal" class="btn btn-primary btn-primary">Добавить класс лекарств</button>
                    <button data-toggle="modal" data-target="#new-drug-manufacturer-modal" class="btn btn-primary btn-primary">Добавить производителя</button>
                </div>
            </c:if>
            <div id="drugs" class="row" align="justify" style="background:white;">
                    <jsp:include page="/drug"/>
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
            <c:if test="${user.userRole eq 'PHARMACIST'}">
            <div class="modal fade" id="new-drug-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                            <img class="img-circle img-responsive" id="img_logo" src="images/create.png" width="250" height="200" alt="Новое лекарство">
                        </div>
                        <form id="create-drug-form" action="/controller" method="post" enctype='multipart/form-data' accept-charset="utf-8">
                            <input type="hidden" name="command" value="CREATE_DRUG">
                            <input type="hidden" name="specialization" value="Терапевт">
                            <div class="modal-body">
                                <div id="div-register-msg">
                                    <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                                    <span id="text-register-msg">Новое лекарство</span>
                                </div>
                                <div class="form_group">
                                    <label for="drug_name11">Название: </label>
                                    <input type="text" class="form-control" id="drug_name11" name="drug_name" required/>
                                </div>
                                <div class="form_group">
                                    <label for="drug_image">Фото: </label>
                                    <input class="form-control" type="file" id="drug_image" name="drug_image"/>
                                </div>
                                <div class="form_group">
                                    <label for="manufacturer_name">Производитель: </label>
                                    <select id="manufacturer_name" class="form-control" name="dr_manufacturer">
                                        <c:forEach items="${drugManufactures}" var="man">
                                            <option value="${man.name},${man.country}">${man.name}(${man.country})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label id="drug_class">Класс лекарства:</label>
                                    <select id="drug_class_select" class="form-control" name="dr_class">
                                        <c:forEach items="${drugClasses}" var="cls">
                                            <option value="${cls.name}">${cls.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label id="prescription_enable">Требуется рецепт:</label>
                                    <select id="prescription_enable" class="form-control" name="pr_status">
                                        <option value="true">Да</option>
                                        <option value="false">Нет</option>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label id="in_stock">В наличии:</label>
                                    <input type="number" class="form-control" step="1" min="0" name="drugs_in_stock" required>
                                </div>
                                <div class="form_group">
                                    <label id="drug_type">Тип лекарста</label>
                                    <select id="drug_type" class="form-control" name="drug_type">
                                        <option value="tablet">Таблетки</option>
                                        <option value="capsule">Капсулы</option>
                                        <option value="salve">Мазь</option>
                                        <option value="syrop">Сироп</option>
                                        <option value="injection">Укол</option>
                                        <option value="candle">Свечи</option>
                                        <option value="drops">Капли</option>
                                        <option value="unknown" selected>Неизвестно</option>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label id="drug_type">Квалификация специалиста</label>
                                    <select id="drug_type" class="form-control" name="specialization">
                                        <c:forEach items="${specializations}" var="spec">
                                            <option value="${spec.specialization}">${spec.specialization}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label for="drug_active_substance">Активное вещество: </label>
                                    <input class="form-control" type="text" id="drug_active_substance" name="active_substance" required/>
                                </div>
                                <div class="form_group">
                                    <label for="drug_price">Цена: </label>
                                    <input class="form-control" type="number" min="0" id="drug_price" step="0.1" name="drug_price" required/>
                                </div>
                                <div class="form_group">
                                    <label>Дозировка: </label>
                                    <span id="dos_message"></span>
                                    <div class="row">
                                        <c:forEach var="i" begin="50" end="1000" step="50">
                                            <div class="col-lg-3">
                                                <div class="checkbox">
                                                    <label><input type="checkbox" value="${i}" name="drug_dosage">${i}</label>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div class="form_group">
                                    <label for="drug_price">Описание: </label>
                                    <textarea class="form-control" maxlength="300" id="description" name="drug_description" placeholder="Комментарий"></textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <div>
                                    <button id="create_drug" type="submit" class="btn btn-primary btn-lg btn-block">Сохранить</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                $("#create-drug-form").submit(function (event) {
                    event.preventDefault();
                    var formData = new FormData($(this)[0]);
                    $.ajax({
                        url:'controller',
                        type:'POST',
                        data:formData,
                        async: false,
                        cache: false,
                        contentType:false,
                        processData: false,
                        success:function (data) {
                            if(data.result==true) {
                                $('#create-drug-form').trigger('reset');
                                $('#new-drug-modal').modal('toggle');
                                $('#dos_message').html("");
                                Notify.generate("Новое лекарство создано", "Готово!", 1);
                            }
                            else {
                                if(data.message.contains("dosages")){
                                    $('#dos_message').html("<span style='color: red'>Выберите дозировку</span>");
                                }
                                else {
                                    $('#create-drug-form').trigger('reset');
                                    $('#new-drug-modal').modal('toggle');
                                    $('#dos_message').html("");
                                    Notify.generate("Не удалось создать лекарство ответ:"+data.message, "Ошибка", 3);
                                }
                            }
                        }
                    });
                    return false;
                });
            </script>
            <div class="modal fade" id="new-drug-class-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                            <img class="img-circle img-responsive" id="img_logo" src="images/create.png" width="250" height="200" alt="Новый класс лекарства"/>
                        </div>
                        <form id="create_class_form">
                            <input type="hidden" name="command" value="CREATE_CLASS">
                            <div class="modal-body">
                                <div id="div-register-msg">
                                    <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                                    <span id="text-register-msg">Новый класс</span>
                                </div>
                                <div class="form_group">
                                    <label for="drug_class_name">Название класса: </label>
                                    <input type="text" class="form-control" id="drug_class_name" name="dr_class" required/>
                                    <span id="create_class_message"></span>
                                </div>
                                <div class="form_group">
                                    <label for="drug_price">Описание класса: </label>
                                    <textarea class="form-control" id="description" name="class_description" placeholder="Комментарий" maxlength="200" required></textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <div>
                                    <button id="submit_drug_class" type="submit" class="btn btn-primary btn-lg btn-block" disabled>Сохранить</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                $('#drug_class_name').blur(function () {
                    var className = $(this).val();
                    var message = $('#create_class_message');
                    var button = $('#submit_drug_class');
                    button.prop('disabled', true);
                    if(className!=""){
                        $.ajax({
                            url:'controller',
                            type:'POST',
                            dataType:'json',
                            data:{command:'CHECK_CLASS', dr_class:className},
                            success:function (data) {
                                if(data.result==true){
                                    if(data.isExist==true) {
                                        message.html("<span style='color: red'>Данный класс уже существует</span>");
                                    }
                                    else {
                                        message.html("<span style='color: green'>Данный класс пока не создан</span>");
                                        button.prop('disabled', false);
                                    }
                                }

                            }
                        });
                    }
                });
                $('#create_class_form').submit(function () {
                    var data = $(this).serialize();
                    var className = $('#drug_class_name').val();
                    var classDescription = $('#description').val();
                    $.ajax({
                        url:'controller',
                        type:'POST',
                        dataType:'json',
                        data:data,
                        success:function (data) {
                            $('#create_class_message').html("");
                            $(this).trigger('reset');
                            $("#new-drug-class-modal").modal('toggle');
                            if(data.result==true){
                                Notify.generate("Новый класс успешно создан",'Готово!', 1);
                                var drugClasses = $('#drug_classes');
                                drugClasses.html("<li>" +
                                        "<a class='Class' href='/controller?command=GET_DRUGS_BY_CLASS&dr_class="+className+"&page=1' title='"+classDescription+"'>"+className+"</a> " +
                                        "</li>"+drugClasses.html());
                                $('#drug_class_select').html("<option value='"+className+"'>"+className+"</option>"+$('#drug_class_select').html());
                                $('#dr_class_select').html("<option value='"+className+"'>"+className+"</option>"+$('#dr_class_select').html());
                            }
                            else {
                                Notify.generate("Не удалось создать новый класс. Ответ сервера:"+data.message,'Ошибка', 2);

                            }
                        }
                    });
                    return false;
                });
            </script>
            <div class="modal fade" id="new-drug-manufacturer-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                            <img class="img-circle img-responsive" id="img_logo" src="images/create.png" width="250" height="200" alt="Новый производитель">
                        </div>
                        <form id="create_manufacturer">
                            <input type="hidden" name="command" value="CREATE_MANUFACTURER">
                            <div class="modal-body">
                                <div id="div-register-msg">
                                    <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                                    <span id="text-register-msg">Новый производитель</span>
                                </div>
                                <div class="form_group">
                                    <label for="drug_manufacturer_name">Название производителя: </label>
                                    <input max="50" type="text" class="form-control new_man" id="drug_manufacturer_name" name="man_name" required/>
                                </div>
                                <div class="form_group">
                                    <label for="drug_manufacturer_country">Страна производителя: </label>
                                    <input max="50" type="text" class="form-control new_man" id="drug_manufacturer_country" name="man_country" required/>
                                </div>
                                <span id="create_man_message"></span>
                                <div class="form_group">
                                    <label for="drug_price">Описание производителя: </label>
                                    <textarea maxlength="300" class="form-control" id="man_description" name="man_description" placeholder="Комментарий" required></textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <div>
                                    <button type="submit" id="submit_man" class="btn btn-primary btn-lg btn-block" disabled>Сохранить</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                $('.new_man').blur(function () {
                    var manName = $('#drug_manufacturer_name').val();
                    var manCountry = $('#drug_manufacturer_country').val();
                    $('#submit_man').prop('disabled', true);
                    if(manName==""||manCountry==""){
                        return false;
                    }
                    $.ajax({
                        url:'controller',
                        type:'POST',
                        dataType:'json',
                        data:{command:'CHECK_MANUFACTURER', man_name:manName, man_country:manCountry},
                        success: function (data) {

                            if(data.result==true){
                                if(data.isExist==true){
                                    $('#create_man_message').html("<span style='color:red'>Данный производитель уже существует</span>");
                                }
                                else {
                                    $('#create_man_message').html("<span style='color:green'>Данный производитель не существует</span>");
                                    $('#submit_man').prop('disabled', false);
                                }
                            }
                        }
                    });
                });
                $('#create_manufacturer').submit(function () {
                    var manName = $('#drug_manufacturer_name').val();
                    var manCountry = $('#drug_manufacturer_country').val();
                    var description = $('#man_description').val();
                    var data = $(this).serialize();
                    $.ajax({
                        url:'controller',
                        type:'POST',
                        dataType:'json',
                        data:data,
                        success:function (data) {
                            $(this).trigger('reset');
                            $('#new-drug-manufacturer-modal').modal('toggle');
                            $('#create_man_message').html("");
                            if(data.result==true){
                                Notify.generate("Новый производитель создан", "Готово!", 1);
                                $('#drug_manufacturer_s').html("<option value='"+manName+","+manCountry+"'>"+manName+"("+manCountry+")"+"</option>>"+$('#drug_manufacturer_s').html());
                            }
                            else {
                                Notify.generate("Не удалось создать нового производителя. Ответ сервера: "+data.message, 'Ошибка', 3);
                            }
                        }
                    });
                    return false;
                });
            </script>
        </c:if>
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