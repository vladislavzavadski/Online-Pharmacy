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
<jsp:useBean id="drugManufactures" scope="request" class="java.util.ArrayList"/>
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
<fmt:message bundle="${loc}" key="locale.balance" var="balance"/>
<c:if test="${sessionScope.user eq null}">
    <c:redirect url="/index.jsp"/>
</c:if>
<c:if test="${param.command eq 'GET_ALL_DRUGS'}">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<style>
    * { padding: 0px; margin: 0px; font-family: 'Calibri', serif; font-size: 1em;}
    #feedback { position: absolute; left: 22%; bottom: 120px; top:44px; background: #FF0; width: 180px; border-radius: 5px; box-shadow: 0px 0px 3px 2px rgba(0,0,0,.5);}
    #feedback div {text-align: center; cursor: pointer; padding: 2px;}
    #feedback ul { padding: 0px 10px 5px 10px; list-style: none; }
    #feedback li { margin-bottom: 5px; }
    #feedback input { display: block; width: 225px; }
    #feedback a { background: #999; color: #000; border-radius: 5px; padding: 0px 5px; text-decoration: none; transition: background 0.3s 0s;}
    #feedback a:hover { background: #333; color: #fff; }
    #feedback .last { text-align: right; }
    #feedback {
        height: 24px;
        transition: all 0.3s;
        overflow: hidden;
        margin-top: 0;
    }

    #toggler_call:checked + #feedback {
        height: 600px;
        bottom: 32px;
        width: 250px;
    }

    #toggler_call{display: none}
</style>
</c:if>
<nav class="navbar navbar-default navbar-fixed-top" style="background:#507ecf">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <c:if test="${param.command eq 'GET_ALL_DRUGS' or param.command eq 'GET_ALL_DOCTORS' or param.command eq 'GET_ALL_MESSAGES'}">
            <a href="#menu-toggle" type="button" class="navbar-toggle" style="display:block; background:white;" id="menu-toggle">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
        </c:if>
        <div class="navbar-header">
            <img class="brand-logo image-logo" src="images/logo.jpg" alt="Green cross" width="60" height="50"/>
            <p class="name navbar-brand" style="color:azure">Pharmacy</p>
            <button type="button" class="navbar-toggle" style="float:right; background:white;" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <c:if test="${param.command eq 'GET_ALL_DRUGS'}">
        <input type="checkbox" id="toggler_call" />
        <div id="feedback">
            <label for="toggler_call"><div>Расширенный поиск</div></label>
            <form action="/controller" id="ext_search_form">
                <input type="hidden" name="command" value="EXTENDED_DRUG_SEARCH">
                <ul>
                    <div class="form-group">
                        <label for="drug_name">Название лекарства</label>
                        <input id="drug_name" class="form-control" type="text" name="name"/>
                    </div>

                    <div class="form-group">
                        <label for="active_substance">Активное вещество</label>
                        <input type="text" class="form-control" id="active_substance" name="active_substance"/>
                    </div>
                    <div class="form-group">
                        <label for="dr_price">Максимальная цена</label>
                        <input type="number" class="form-control" name="max_price" min="10" id="dr_price" step="0.1"/>
                    </div>
                    <li>
                        <label>Класс лекарства</label>
                        <select id="dr_class_select" name="dr_class">
                            <option value="" selected>Unknown</option>
                            <c:forEach items="${drugClasses}" var="drugClass">
                                <option value="${drugClass.name}">${drugClass.name}</option>
                            </c:forEach>
                        </select>
                    </li>
                    <li>
                        <label>Производитель</label>
                        <select id="drug_manufacturer_s" name="dr_manufacture">
                            <option value="" selected>Unknown</option>
                            <c:forEach items="${drugManufactures}" var="man">
                                <option value="${man.name},${man.country}">${man.name}(${man.country})</option>
                            </c:forEach>
                        </select>
                    </li>
                    <li>
                        <input id="in_stock_only" type="checkbox" name="only_in_stock" value="true"> Только в наличии<br>
                    </li>
                    <li>
                        <input id="without_prescription" type="checkbox" name="only_free" value="false"> Только без рецепта<br>
                    </li>
                    <input id="ext_search" type="submit"/>

                </ul>
            </form>
        </div>
        </c:if>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

            <c:if test="${param.command eq 'GET_ALL_DRUGS' or param.command eq 'GET_ALL_DOCTORS'}">
                <form class="navbar-form navbar-left navbar-search" role="search" style="padding-top:5px;">
                    <div class="form-group">
                        <input id="query_string" type="text" class="form-control" placeholder="${search}">
                    </div>
                    <button id="search_button" type="submit" class="btn btn-info">${search}</button>
                </form>
            </c:if>

            <div class="btn-group" style="padding-top:20px;" role="group">
                <a href="/main.jsp" class="btn btn-default btn-primary">${myCabinet}</a>
                <c:if test="${user.userRole eq 'CLIENT'}">
                    <a href="/controller?command=GET_ALL_ORDERS&page=1&overload=true" class="btn btn-default btn-primary">${myOrders}</a>
                </c:if>
                <c:if test="${user.userRole eq 'CLIENT' or user.userRole eq 'DOCTOR'}">
                    <a href="/controller?command=GET_PRESCRIPTIONS&page=1&overload=true" class="btn btn-default btn-primary">${myPrescriptions}</a>
                    <a href="/controller?command=GET_REQUESTS&page=1&overload=true" class="btn btn-default btn-primary">My requests
                        <c:if test="${user.userRole eq 'DOCTOR'}">
                            <jsp:useBean id="request_count" scope="session" type="java.lang.Integer"/>
                            <span id="req_count" class="badge">${request_count}</span>
                        </c:if>
                    </a>
                    <jsp:useBean id="count" scope="session" type="java.lang.Integer"/>
                    <a href="/controller?command=GET_ALL_MESSAGES&page=1&overload=true" class="btn btn-default btn-primary">Messages <span id="mes_count" class="badge">${count}</span></a>
                </c:if>
                <a href="/controller?command=OPEN_SETTINGS" class="btn btn-default btn-primary">${mySettings}</a>
                <a href="/controller?command=GET_ALL_DRUGS&overload=true&page=1" class="btn btn-default btn-primary">${drugs}</a>
                <a href="/controller?command=GET_ALL_DOCTORS&overload=true&page=1" class="btn btn-default btn-primary">${doctors}</a>
                <a href="/controller?command=LOG_OUT" class="btn btn-default btn-primary">${logOut}</a>
            </div>

            <a href="controller?command=SWITCH_LOCALE&language=en"><img src="images/united-kingdom-flag_9815.png" alt="english"></a>
            <a href="controller?command=SWITCH_LOCALE&language=ru"><img src="images/russia-flag_3763.png" alt="русский"></a>

        </div>
    </div>
</nav>
<script>
    $("#ext_search").click(function(){
        $("#toggler_call").attr("checked", false);
        return false;
    });
</script>