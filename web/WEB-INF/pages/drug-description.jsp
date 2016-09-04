<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="header.jsp"%>
<jsp:useBean id="drug" scope="request" class="by.training.online_pharmacy.domain.drug.Drug"/>
<jsp:useBean id="prescriptionExist" scope="request" type="java.lang.Boolean" />
<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>${drug.name}</title>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/simple-sidebar.css" rel="stylesheet">
        <link href="css/sticky-footer-navbar.css" rel="stylesheet">
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
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
        <c:if test="${drug.prescriptionEnable}">
            <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/js/bootstrap-datepicker.min.js"></script>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/css/bootstrap-datepicker3.css"/>
            <script>
                $(document).ready(function(){
                    var date_input=$('input[name="date"]'); //our date input has the name "date"
                    var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
                    date_input.datepicker({
                        format: 'mm/dd/yyyy',
                        container: container,
                        todayHighlight: true,
                        autoclose: true,
                        disableEntry: true,
                    })
                })
            </script>
        </c:if>
    </head>
    <body>
    <jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
        <div class="container content">
            <div id="notifies"></div>
            <!-- Sidebar -->
            <div class="container" style="background:white;" align="justify">
                <div class="row">
                    <div class="col-xs-12 col-lg-4">
                        <h1 id="drug_name_desc" class="display_1">${drug.name}
                            <span class="label label-success">$${drug.price}</span>
                        </h1>
                        <div id="img_div">
                        <img id="drug_img" src="/controller?command=GET_DRUG_IMAGE&dr_id=${drug.id}" alt="Drug image" class="img-responsive"/>
                        </div>
                        <p id="drug_description_desc">
                            ${drug.description}
                        </p>
                        <b>
                            Производитель:
                        </b>&nbsp;
                        <span title="${drug.drugManufacturer.description}" id="drug_man_desc">
                            ${drug.drugManufacturer.name}
                        </span>
                        <br/>
                        <b>
                            Страна:
                        </b>&nbsp;
                        <span id="drug_country_desc">
                            ${drug.drugManufacturer.country}
                        </span>
                        <br/>
                        <b>
                            Отпуск по рецепту:
                        </b>&nbsp;
                        <span id="drug_prescription_desc">
                    <c:choose>
                        <c:when test="${drug.prescriptionEnable}">
                            Да
                        </c:when>
                        <c:otherwise>
                            Нет
                        </c:otherwise>
                    </c:choose>
                </span>
                        <br/>
                        <b>
                            Есть в наличии:
                        </b>&nbsp;
                        <span id="in_stock_desc">
                            ${drug.drugsInStock}
                        </span>
                        <br/>
                        <b>
                            Класс лекарства:
                        </b>&nbsp;
                        <span title="${drug.drugClass.description}" id="drug_class_desc">
                            ${drug.drugClass.name}
                        </span>
                        <br/>
                        <b>
                            Тип лекарства:
                        </b>&nbsp;
                        <span id="drug_type_desc">
                            ${drug.type}
                        </span>
                        <br/>
                        <b>
                            Активное вещество:
                        </b>&nbsp;
                        <span id="drug_substance_desc">
                            ${drug.activeSubstance}
                        </span>
                        <br/>
                        <c:if test="${not drug.inStock}">
                            <span style="color: red">Лекарсва нет в наличии</span>
                        </c:if>
                    </div>
                    <div class="col-xs-12 col-lg-4" style="padding-top:40px">
                        <c:if test="${((not drug.prescriptionEnable)or(drug.prescriptionEnable and prescriptionExist)) and drug.inStock and user.userRole eq 'CLIENT'}">
                            <button class="btn btn-lg btn-success" style="float:right" data-toggle="modal" data-target="#order-modal" >Заказать</button>
                        </c:if>
                        <c:if test="${drug.prescriptionEnable and user.userRole eq 'CLIENT'}">
                            <button data-toggle="modal" data-target="#request-prescription-modal" class="btn btn-primary btn-primary">Заказать рецепт</button>
                        </c:if>
                        <c:if test="${user.userRole eq 'PHARMACIST'}">
                            <button style="float:right" data-toggle="modal" data-target="#edit-modal" class="btn btn-primary"><span class="glyphicon glyphicon-edit"></span> Изменить</button>
                            <button style="float:right" data-toggle="modal" data-target="#delete-modal" class="btn btn-danger">Удалить</button>
                        </c:if>
                    </div>
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
    <c:if test="${user.userRole eq 'CLIENT'}">
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
                            <c:forEach items="${drug.dosages}" var="dosage">
                                <option value="${dosage}">${dosage}</option>
                            </c:forEach>
                        </select>
                     </div>
                     <div class="form_group">
                        <label for="drug_count">Количество: </label>     
                        <input id="drug_count"  class="form-control" type="number" placeholder="Количество" step="1" min="0" max="${drug.drugsInStock}" required>
                     </div>
                  </div>
                  <div class="modal-footer">
                     <div>
                        <button id="create_order" data-dismiss="modal" type="submit" class="btn btn-primary btn-lg btn-block" >Заказать</button>
                     </div>
                  </div>
               </form>
            </div>
         </div>
        </div>
    </c:if>
        <c:if test="${drug.prescriptionEnable and user.userRole eq 'CLIENT'}">
            <div class="modal fade" id="request-prescription-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                        </div>
                        <form>
                                <div class="form-group ">
                                    <label class="control-label" for="date">
                                        Продлить до:
                                        <span class="asteriskField">
                                        *
                                    </span>
                                    </label>
                                    <input class="form-control input-md" id="date" name="date" placeholder="MM/DD/YYYY" type="text" required/>
                                    <span id="date_comment"></span>
                                </div>
                                <div class="form-group">
                                    <label class="control-label" for="comment">Описание проблемы</label>
                                    <textarea class="form-control" id="comment" name="comment">Комментарий</textarea>
                                    <span id="comment_comment"></span>
                                </div>
                                <div class="form-group">
                                    <label class="control-label" for="create_order_button"></label>
                                    <button id="create_order_button" class="btn btn-primary" required>Отправить</button>
                                </div>
                        </form>
                    </div>
                </div>
            </div>
        </c:if>
        <script>
            $("#create_order").click(function () {
                var drugDosage = parseInt($("#dosage").val());
                var count = parseInt($("#drug_count").val());

                $.ajax({
                    url: 'controller',
                    dataType: 'json',
                    type: 'POST',
                    data: {command:'CREATE_ORDER', drug_count:count, drug_dosage:drugDosage, dr_id:${drug.id}},
                    success:function (data) {
                        $("#order-modal").modal('hide');
                        if(data.result){
                            Notify.generate('Ваш заказ успешно выполнен', 'Завершено', 1);
                            var inStock = parseInt($("#in_stock").html())-count;
                            $("#in_stock").html(inStock);
                            $("#drug_count").attr("max", inStock);
                            $("#drug_count").val(0);

                        }
                        else {
                            if(data.isCritical){
                                Notify.generate('Логин под которым вы авторизованы был удален из базы данных.', 'Критическая ошибка', 3);
                                setTimeout(function () {
                                   window.location.assign("/index.jsp");
                                }, 5000);
                            }
                            else {
                                Notify.generate('Не удалось совершить заказ. Ответ сервера: '+data.message, 'Не удалось совершить заказ', 2);
                            }
                        }
                    }
                });
                return false;
            });
            
            $('#create_order_button').click(function () {
                var date = $("#date").val();
                var comment = $("#comment").val();
                if(date==""){
                    $("#date_comment").html("<span style='color:red'>Поле дата должно быть заполнено</span>");
                    return false;
                }
                if(comment == ""){
                    $("#date_comment").html("<span style='color:red'>Поле комментарий должно быть заполнено</span>");
                    return false;
                }
                $.ajax({
                    url:'controller',
                    type:'POST',
                    dataType:'json',
                    data:{command:'CREATE_REQUEST', client_comment:comment, prolong_date:date, dr_id:${drug.id}},
                    success:function (data) {
                        $("#request-prescription-modal").modal('hide');
                        if(data.result){
                            Notify.generate('Ваш запрос на получение рецепта успешно отправлен', 'Успешно', 1);
                        }else if(data.isCritical){
                            Notify.generate('Логин под которым вы авторизованы был удален из базы данных', 'Критическая ошибка', 3);
                            window.location = '/index.jsp';
                        }else {
                            Notify.generate('Данное лекарство не обнаружено в базе, \n\r или отпускается без рецепта. \r\n Или у вас уже имеется необработанный запрос', 'Ошибка', 2);
                        }

                    }
                });
                return false;
            });
        </script>
        <c:if test="${user.userRole eq 'PHARMACIST'}">
            <jsp:useBean id="drugManufacturers" scope="request" class="java.util.ArrayList"/>
            <jsp:useBean id="drugClasses" scope="request" class="java.util.ArrayList"/>
            <jsp:useBean id="specializations" scope="request" class="java.util.ArrayList"/>
            <div class="modal fade" id="edit-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                            <img class="img-circle img-responsive" id="img_logo" src="images/edit.png" width="150" height="150" alt="Редактирование">
                        </div>
                        <form id="update_drug_form">
                            <input type="hidden" name="command" value="UPDATE_DRUG">
                            <input type="hidden" name="dr_id" value="${drug.id}">
                            <div class="modal-body">
                                <div id="div-register-msg">
                                    <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                                    <span id="text-register-msg">Редактировать лекарство</span>
                                </div>
                                <div class="form_group">
                                    <label for="drug_name">Название: </label>
                                    <input type="text" class="form-control" id="drug_name" value="${drug.name}" name="drug_name" required/>
                                </div>
                                <div class="form_group">
                                    <label for="drug_image">Фото: </label>
                                    <input class="form-control" type="file" id="drug_image" name="drug_image"/>
                                </div>
                                <div class="form_group">
                                    <label for="manufacturer_name">Производитель: </label>
                                    <select id="manufacturer_name" class="form-control" name="dr_manufacturer">
                                        <c:forEach items="${drugManufacturers}" var="drugMan">
                                            <option value="${drugMan.name},${drugMan.country}" <c:if test="${drugMan.name eq drug.drugManufacturer.name and drugMan.country eq drug.drugManufacturer.country}">selected</c:if>>${drugMan.name}(${drugMan.country})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label id="drug_class">Класс лекарства:</label>
                                    <select id="drug_class" class="form-control" name="dr_class">
                                        <c:forEach items="${drugClasses}" var="drugClass">
                                            <option value="${drugClass.name}" <c:if test="${drug.drugClass.name eq drugClass.name}">selected</c:if>>${drugClass.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label for="prescription_enable">Требуется рецепт:</label>
                                    <select id="prescription_enable" class="form-control" name="pr_status">
                                        <option value="true" <c:if test="${drug.prescriptionEnable}">selected</c:if>>Да</option>
                                        <option value="false" <c:if test="${ not drug.prescriptionEnable}">selected</c:if>>Нет</option>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label for="in_stock">В наличии:</label>
                                    <input id="in_stock" type="number" class="form-control" step="1" min="0" name="drugs_in_stock" value="${drug.drugsInStock}" required>
                                </div>
                                <div class="form_group">
                                    <label for="drug_type">Тип лекарста</label>
                                    <select id="drug_type" class="form-control" name="drug_type">
                                        <option value="tablet" <c:if test="${drug.type eq 'TABLET'}">selected</c:if>>Таблетки</option>
                                        <option value="capsule" <c:if test="${drug.type eq 'CAPSULE'}">selected</c:if>>Капсулы</option>
                                        <option value="salve" <c:if test="${drug.type eq 'SALVE'}">selected</c:if>>Мазь</option>
                                        <option value="syrop" <c:if test="${drug.type eq 'SYROP'}">selected</c:if>>Сироп</option>
                                        <option value="injection" <c:if test="${drug.type eq 'INJECTION'}">selected</c:if>>Укол</option>
                                        <option value="candle" <c:if test="${drug.type eq 'CANDLE'}">selected</c:if>>Свечи</option>
                                        <option value="drops" <c:if test="${drug.type eq 'DROPS'}">selected</c:if>>Капли</option>
                                        <option value="unknown" <c:if test="${drug.type eq 'UNKNOWN'}">selected</c:if>>Неизвестно</option>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label id="drug_type">Квалификация специалиста</label>
                                    <select id="drug_type" class="form-control" name="specialization">
                                        <c:forEach items="${specializations}" var="spec">
                                            <option value="${spec.specialization}" <c:if test="${drug.doctorSpecialization eq spec.specialization}">selected</c:if>>${spec.specialization}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label for="drug_active_substance">Активное вещество: </label>
                                    <input class="form-control" type="text" id="drug_active_substance" name="active_substance" value="${drug.activeSubstance}" required/>
                                </div>
                                <div class="form_group">
                                    <label for="drug_price">Цена: </label>
                                    <input class="form-control" type="number" min="0" id="drug_price" step="0.1" name="drug_price" value="${drug.price}" required/>
                                </div>
                                <div class="form_group">
                                    <label >Дозировка: </label>
                                    <span id="dosage_message" style="color: red;"></span>
                                    <div class="row">
                                        <c:forEach var="i" begin="50" end="1000" step="50">
                                            <div class="col-lg-3">
                                                <div class="checkbox">
                                                    <label><input type="checkbox" value="${i}" name="drug_dosage"<c:if test="${drug.dosages.contains(i)}">checked</c:if>>${i}</label>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div class="form_group">
                                    <label for="drug_price">Описание: </label>
                                    <textarea class="form-control" id="description" name="drug_description" placeholder="Комментарий">${drug.description}</textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <div>
                                    <button type="submit" id="save_edit" class="btn btn-primary btn-lg btn-block">Сохранить изменения</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                var checkboxes = $("input[type='checkbox']");
                checkboxes.click(function () {
                    $('#save_edit').prop('disabled', !checkboxes.is(':checked'));
                    if(!checkboxes.is(':checked')){
                        $('#dosage_message').html('Выберите хотя бы одну дозировку!');
                    }
                    else {
                        $('#dosage_message').html('');
                    }
                });
                $('#update_drug_form').submit(function () {
                    var data = new FormData($(this)[0]);
                    
                    $.ajax({
                        url:'controller',
                        type:'POST',
                        async: false,
                        cache: false,
                        contentType:false,
                        processData: false,
                        data:data,
                        success: function (data) {
                            if(data.result==true){
                                Notify.generate("Изменения сохранены", 'Готово!', 1);
                                $('#edit-modal').modal('toggle');
                                $('#drug_img').attr('src', '/controller?command=GET_DRUG_IMAGE&dr_id=${drug.id}&atr='+new Date().getTime());
                                $('#drug_name_desc').html($('#drug_name').val()+"<span class=\"label label-success\">$"+$('#drug_price').val()+"</span>");
                                $('#drug_description_desc').html($('#description').val());
                                var strings = $('#manufacturer_name').val().toString().split(",");
                                $('#drug_man_desc').html(strings[0]);
                                $('#drug_country_desc').html(strings[1]);
                                if($('#prescription_enable').val()=="true"){
                                    $('#drug_prescription_desc').html("Да");
                                }
                                else {
                                    $('#drug_prescription_desc').html("Нет");
                                }
                                $('#in_stock_desc').html($('#in_stock').val());
                                $('#drug_substance_desc').html($('#drug_active_substance').val());
                                $('#drug_type_desc').html($( "#drug_type option:selected" ).text());
                            }
                            else {
                                Notify.generate('Ответ сервера'+data.message, 'Ошибка!', 3);
                            }
                        }
                    });
                    return false;
                });
            </script>
            <div class="modal fade" id="delete-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">

                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel">Удаление</h4>
                        </div>

                        <div class="modal-body">
                            <p>Вы действительно хотите удалить лекарство?</p>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                            <a  class="btn btn-danger btn-ok" href="/controller?command=DELETE_DRUG&dr_id=${drug.id}">Удалить</a>
                        </div>
                    </div>
                </div>
            </div>
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