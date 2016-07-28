<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 22.07.16
  Time: 18:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
    <title><jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/></title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/simple-sidebar.css" rel="stylesheet">
    <link href="css/sticky-footer-navbar.css" rel="stylesheet">
    <script src="js/bootstrap.js"></script>
</head>
<body>
<c:set var="language" value="${not empty sessionScope.language ? sessionScope.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="resource.locale" var="loc"/>
<fmt:message bundle="${loc}" key="locale.my_prescriptions" var="myPrescriptions"/>
<fmt:message bundle="${loc}" key="locale.my_settings" var="mySettings"/>
<fmt:message bundle="${loc}" key="locale.drugs" var="drugs"/>
<fmt:message bundle="${loc}" key="locale.doctor" var="doctors"/>
<fmt:message bundle="${loc}" key="locale.log_out" var="logOut"/>
<fmt:message bundle="${loc}" key="locale.my_orders" var="myOrders"/>
<fmt:message bundle="${loc}" key="locale.about" var="about"/>
<fmt:message bundle="${loc}" key="locale.contacts" var="contacts"/>
<fmt:message bundle="${loc}" key="locale.gender" var="gender"/>
<fmt:message bundle="${loc}" key="locale.phone_number" var="phoneNumber"/>
<fmt:message bundle="${loc}" key="locale.search" var="search"/>
<fmt:message bundle="${loc}" key="locale.site_builder" var="siteBUilder"/>
<fmt:message bundle="${loc}" key="locale.gender.male" var="male"/>
<fmt:message bundle="${loc}" key="locale.gender.female" var="female"/>
<fmt:message bundle="${loc}" key="locale.gender.unknown" var="unknown"/>
<nav class="navbar navbar-default navbar-fixed-top" style="background:#507ecf">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <img class="brand-logo image-logo" src="images/logo.jpg" alt="Green cross" width="60" height="50"/>
            <p class="name navbar-brand" style="color:azure">Online Pharmacy</p>
            <button type="button" class="navbar-toggle" style="float:right; background:white;" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <form class="navbar-form navbar-left" role="search" style="padding-top:20px;">
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="${search}">
                </div>
                <button type="submit" class="btn btn-info">${search}</button>
            </form>
            <div class="btn-group" style="padding-top:20px;" role="group">
                <c:choose>
                    <c:when test="${user.userRole eq 'CLIENT'}">
                        <a href="#" class="btn btn-lg btn-primary">${myOrders}</a>
                        <a href="#" class="btn btn-lg btn-primary">${myPrescriptions}</a>
                    </c:when>
                </c:choose>
                <a href="#" class="btn btn-lg btn-primary">${mySettings}</a>
                <a href="#" class="btn btn-lg btn-primary">${drugs}</a>
                <a href="#" class="btn btn-lg btn-primary">${doctors}</a>
                <a href="#" class="btn btn-lg btn-primary">${logOut}</a>
            </div>

                <a href="controller?command=SWITCH_LOCALE&language=en"><img src="images/united-kingdom-flag_9815.png" alt="english"></a>
                <a href="controller?command=SWITCH_LOCALE&language=ru"><img src="images/russia-flag_3763.png" alt="русский"></a>

        </div>
        </div>
</nav>
    <div class="container">
        <!-- Sidebar -->
        <div class="container" style="background: white; padding-top: 10px">
            <div class="row">
                <div class="col-lg-6">
                    <c:choose>
                        <c:when test="${user.userImage eq null}">
                            <img src="${user.pathToAlternativeImage}" class="img-responsive" alt="<jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/>" width="300" height="400"/>
                        </c:when>
                        <c:otherwise>
                            <img src="/controller?command=GET_PROFILE_IMAGE" class="img-responsive" alt="<jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/>" width="300" height="400"/>
                        </c:otherwise>
                    </c:choose>

                    <!--<h1><jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/></h1>-->
                </div>
                <div class="col-lg-6">
                    <h1><jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/></h1>
                    <b>${phoneNumber}: </b>&nbsp
                    <span><jsp:getProperty name="user" property="phone"/></span>
                    <br/>
                    <b>e-mail: </b>&nbsp
                    <span><a href="mailto:<jsp:getProperty name="user" property="mail"/>"><jsp:getProperty name="user" property="mail"/></a> </span>
                    <br/>
                    <b>${gender}: </b>&nbsp
                    <c:choose>
                        <c:when test="${user.gender eq 'MALE'}">
                            <span>${male}</span>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${user.gender eq 'FEMALE'}">
                                    <span>${female}</span>
                                </c:when>
                                <c:otherwise>
                                    <span>${unknown}</span>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
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
                            <b>${phoneNumber}:</b>&nbsp;<span>+375447350720</span>
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
                ${siteBUilder} <a href="mailto:vladislav.zavadski@gmail.com">Vladislav Zavadski</a>, EPAM Systems, 2016
            </p>
            <div class="nav navbar-nav navbar-left" style="line-height:50px">
                <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#about-modal">${about}</button>
                <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#contacts-modal">${contacts}</button>
            </div>
        </div>
    </footer>
</body>
</html>
