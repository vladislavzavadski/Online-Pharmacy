<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 02.08.16
  Time: 10:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
<fmt:message bundle="${loc}" key="locale.my_cabinet" var="myCabinet"/>
<c:if test="${sessionScope.user eq null}">
    <c:redirect url="/index.jsp"/>
</c:if>
<nav class="navbar navbar-default navbar-fixed-top" style="background:#507ecf">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <c:if test="${param.command eq 'GET_ALL_DRUGS'}">
            <a href="#menu-toggle" type="button" class="navbar-toggle" style="display:block; background:white;" id="menu-toggle">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
        </c:if>
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
            <c:if test="${param.command eq 'GET_ALL_DRUGS'}">
                <form class="navbar-form navbar-left" role="search" style="padding-top:20px;">
                    <div class="form-group">
                        <input id="query_string" type="text" class="form-control" placeholder="${search}">
                    </div>
                    <button id="search_button" type="submit" class="btn btn-info">${search}</button>
                </form>
            </c:if>
            <div class="btn-group" style="padding-top:20px;" role="group">
                <a href="/main.jsp" class="btn btn-lg btn-primary">${myCabinet}</a>
                <c:if test="${user.userRole eq 'CLIENT'}">
                   <a href="#" class="btn btn-lg btn-primary">${myOrders}</a>
                   <a href="#" class="btn btn-lg btn-primary">${myPrescriptions}</a>
                </c:if>
                <a href="/settings" class="btn btn-lg btn-primary">${mySettings}</a>
                <a href="/controller?command=GET_ALL_DRUGS&page=1" class="btn btn-lg btn-primary">${drugs}</a>
                <a href="#" class="btn btn-lg btn-primary">${doctors}</a>
                <a href="/controller?command=LOG_OUT" class="btn btn-lg btn-primary">${logOut}</a>
            </div>

            <a href="controller?command=SWITCH_LOCALE&language=en"><img src="images/united-kingdom-flag_9815.png" alt="english"></a>
            <a href="controller?command=SWITCH_LOCALE&language=ru"><img src="images/russia-flag_3763.png" alt="русский"></a>

        </div>
    </div>
</nav>
