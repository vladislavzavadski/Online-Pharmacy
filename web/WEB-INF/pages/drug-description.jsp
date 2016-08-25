<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="header.jsp"%>
<jsp:useBean id="drug" scope="request" class="by.training.online_pharmacy.domain.drug.Drug"/>
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

        <div class="container content">
            <div id="notifies"></div>
            <!-- Sidebar -->
            <div class="container" style="background:white;" align="justify">
                <div class="row">
                    <div class="col-xs-4 col-lg-4">
                        <h1 class="display_1">${drug.name}
                            <span class="label label-success">$${drug.price}</span>
                        </h1>
                        <img src="/controller?command=GET_DRUG_IMAGE&dr_id=${drug.id}" alt="Drug image" class="img-responsive"/>
                    </div>
                    <div class="col-xs-4 col-lg-4" style="padding-top:40px">
                        <button class="btn btn-lg btn-success" style="float:right" data-toggle="modal" data-target="#order-modal" >Заказать</button>
                        <c:if test="${drug.prescriptionEnable}">
                            <button data-toggle="modal" data-target="#request-prescription-modal" class="btn btn-primary btn-primary">Заказать рецепт</button>
                        </c:if>
                    </div>
                </div>
                <p>            
                    ${drug.description}
                </p>
                <b>
                    Производитель:
                </b>&nbsp;
                <span title="${drug.drugManufacturer.description}">
                    ${drug.drugManufacturer.name}
                </span>
                <br/>
                <b>
                    Страна:
                </b>&nbsp;
                <span>
                    ${drug.drugManufacturer.country}
                </span>
                <br/>
                <b>
                    Отпуск по рецепту:
                </b>&nbsp;
                <span>
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
                <span id="in_stock">
                    ${drug.drugsInStock}
                </span>
                <br/>
                <b>
                    Класс лекарства:
                </b>&nbsp;
                <span title="${drug.drugClass.description}">
                    ${drug.drugClass.name}
                </span>
                <br/>
                <b>
                    Тип лекарства:
                </b>&nbsp;
                <span>
                    ${drug.type}
                </span>
                <br/>
                <b>
                    Активное вещество:
                </b>&nbsp;
                <span>
                    ${drug.activeSubstance}
                </span>
                <br/>
                <c:if test="${not drug.inStock}">
                    <span style="color: red">Лекарсва нет в наличии</span>
                </c:if>
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
        <c:if test="${drug.prescriptionEnable}">
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