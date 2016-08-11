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
        <title>Анальгин</title>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/simple-sidebar.css" rel="stylesheet">
        <link href="css/sticky-footer-navbar.css" rel="stylesheet">
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
        <script src="js/bootstrap.js"></script>
    </head>
    <body>

        <div class="container content">
            <!-- Sidebar -->
            <div class="container" style="background:white;" align="justify">
                <div class="row">
                    <div class="col-xs-4 col-lg-8">
                        <h1 class="display_1">${drug.name}</h1>
                        <img src="${drug.pathToImage}" alt="Drug image" class="img-responsive"/>
                    </div>
                    <div class="col-xs-4 col-lg-4" style="padding-top:40px">
                        <button class="btn btn-lg btn-success" style="float:right" data-toggle="modal" data-target="#order-modal">Заказать</button>
                        <h2><span class="label label-success">$${drug.price}</span></h2>
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
                <span>
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
                        <input id="drug_count" class="form-control" type="number" placeholder="Количество" step="1" min="0" max="${drug.drugsInStock}" required>
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
</html>