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
<fmt:message bundle="${loc}" key="locale.balance" var="balance"/>
<fmt:message bundle="${loc}" key="locale.requests" var="myRequests"/>
<fmt:message bundle="${loc}" key="locale.messages" var="messages"/>
<fmt:message bundle="${loc}" key="locale.extended_search" var="extSearch"/>
<fmt:message bundle="${loc}" key="locale.drug_name" var="drugName"/>
<fmt:message bundle="${loc}" key="locale.active_substance" var="actSubs"/>
<fmt:message bundle="${loc}" key="locale.max_price" var="maxPrice"/>
<fmt:message bundle="${loc}" key="locale.drug_class" var="drugClas"/>
<fmt:message bundle="${loc}" key="locale.manufacturer" var="manufacturer"/>
<fmt:message bundle="${loc}" key="locale.in_stock" var="inStock"/>
<fmt:message bundle="${loc}" key="locale.without_presc" var="withoutPresc"/>
<fmt:message bundle="${loc}" key="locale.refill" var="refill"/>
<fmt:message bundle="${loc}" key="locale.enter_card" var="entCard"/>
<fmt:message bundle="${loc}" key="locale.sum" var="sum"/>
<fmt:message bundle="${loc}" key="locale.card_number" var="cardNumber"/>
<fmt:message bundle="${loc}" key="locale.refilled_balance" var="balanceRefilled"/>
<fmt:message bundle="${loc}" key="locale.completed" var="completed"/>
<fmt:message bundle="${loc}" key="locale.specialization" var="specialization"/>
<fmt:message bundle="${loc}" key="locale.description" var="description"/>
<fmt:message bundle="${loc}" key="locale.message_for_doctor" var="docMessage"/>
<fmt:message bundle="${loc}" key="locale.message" var="message"/>
<fmt:message bundle="${loc}" key="locale.error" var="error"/>
<fmt:message bundle="${loc}" key="locale.message_sended" var="mesSended"/>
<fmt:message bundle="${loc}" key="locale.message_error" var="mesError"/>
<fmt:message bundle="${loc}" key="locale.doc_spec" var="docSpec"/>
<fmt:message bundle="${loc}" key="locale.all" var="all"/>
<fmt:message bundle="${loc}" key="locale.new_doctor" var="newDoctor"/>
<fmt:message bundle="${loc}" key="locale.registration" var="registration"/>
<fmt:message bundle="${loc}" key="locale.login" var="login"/>
<fmt:message bundle="${loc}" key="locale.first_name" var="firstName"/>
<fmt:message bundle="${loc}" key="locale.last_name" var="lastName"/>
<fmt:message bundle="${loc}" key="locale.register" var="register"/>
<fmt:message bundle="${loc}" key="locale.doctor_created" var="docCreated"/>
<fmt:message bundle="${loc}" key="locale.server_response" var="serverResponse"/>
<fmt:message bundle="${loc}" key="locale.country" var="country"/>
<fmt:message bundle="${loc}" key="locale.by_prescription" var="byPresc"/>
<fmt:message bundle="${loc}" key="locale.is_in_stock" var="isInStock"/>
<fmt:message bundle="${loc}" key="locale.drug_not_in_stock" var="drNotInStock"/>
<fmt:message bundle="${loc}" key="locale.order_drug" var="orderDrug"/>
<fmt:message bundle="${loc}" key="locale.order_prescription" var="orderPrescription"/>
<fmt:message bundle="${loc}" key="locale.edit" var="edit"/>
<fmt:message bundle="${loc}" key="locale.delete" var="delete"/>
<fmt:message bundle="${loc}" key="locale.drug_order" var="drugOrder"/>
<fmt:message bundle="${loc}" key="locale.dosage" var="dosage"/>
<fmt:message bundle="${loc}" key="locale.drug_count" var="drugCount"/>
<fmt:message bundle="${loc}" key="locale.drug_type" var="drugType"/>
<fmt:message bundle="${loc}" key="locale.prolong_to" var="prolongTo"/>
<fmt:message bundle="${loc}" key="locale.problem_description" var="problemDescription"/>
<fmt:message bundle="${loc}" key="locale.comment" var="comment"/>
<fmt:message bundle="${loc}" key="locale.send" var="send"/>
<fmt:message bundle="${loc}" key="locale.order_error" var="orderError"/>
<fmt:message bundle="${loc}" key="locale.order_success" var="orderSuccess"/>
<fmt:message bundle="${loc}" key="locale.request_success" var="requestSuccess"/>
<fmt:message bundle="${loc}" key="locale.non_processed_request" var="nonProcessedRequest"/>
<fmt:message bundle="${loc}" key="locale.edit_drug" var="editDrug"/>
<fmt:message bundle="${loc}" key="locale.photo" var="photo"/>
<fmt:message bundle="${loc}" key="locale.prescription_enable" var="prescriptionEnable"/>
<fmt:message bundle="${loc}" key="locale.yes" var="yes"/>
<fmt:message bundle="${loc}" key="locale.no" var="no"/>
<fmt:message bundle="${loc}" key="locale.tablet" var="tablet"/>
<fmt:message bundle="${loc}" key="locale.capsule" var="capsule"/>
<fmt:message bundle="${loc}" key="locale.salve" var="salve"/>
<fmt:message bundle="${loc}" key="locale.injection" var="injection"/>
<fmt:message bundle="${loc}" key="locale.syrop" var="syrop"/>
<fmt:message bundle="${loc}" key="locale.candle" var="candle"/>
<fmt:message bundle="${loc}" key="locale.drops" var="drops"/>
<fmt:message bundle="${loc}" key="locale.drug_price" var="drugPrice"/>
<fmt:message bundle="${loc}" key="locale.save" var="save"/>
<fmt:message bundle="${loc}" key="locale.check_one_dosage" var="checkOneDosage"/>
<fmt:message bundle="${loc}" key="locale.changes_saved" var="changesSaved"/>
<fmt:message bundle="${loc}" key="locale.deleting" var="deleting"/>
<fmt:message bundle="${loc}" key="locale.sure_delete" var="sureDelete"/>
<fmt:message bundle="${loc}" key="locale.cancel" var="cancel"/>
<fmt:message bundle="${loc}" key="locale.all_classes" var="allClasses"/>
<fmt:message bundle="${loc}" key="locale.add_drug" var="addDrug"/>
<fmt:message bundle="${loc}" key="locale.add_drug_class" var="addDrugClass"/>
<fmt:message bundle="${loc}" key="locale.add_drug_manufacturer" var="addManufacturer"/>
<fmt:message bundle="${loc}" key="locale.new_drug" var="newDrug"/>
<fmt:message bundle="${loc}" key="locale.not_image" var="notImage"/>
<fmt:message bundle="${loc}" key="locale.too_large" var="tooLarge"/>
<fmt:message bundle="${loc}" key="locale.new_drug_created" var="newDrugCreated"/>
<fmt:message bundle="${loc}" key="locale.new_drug_class" var="newDrugClass"/>
<fmt:message bundle="${loc}" key="locale.class_name" var="className"/>
<fmt:message bundle="${loc}" key="locale.class_description" var="classDescription"/>
<fmt:message bundle="${loc}" key="locale.take_photo" var="takePhoto"/>
<fmt:message bundle="${loc}" key="locale.class_exist" var="classExist"/>
<fmt:message bundle="${loc}" key="locale.class_not_exist" var="classNotExist"/>
<fmt:message bundle="${loc}" key="locale.class_created" var="newClassCreated"/>
<fmt:message bundle="${loc}" key="locale.new_manufacturer" var="newManufacturer"/>
<fmt:message bundle="${loc}" key="locale.man_name" var="manName"/>
<fmt:message bundle="${loc}" key="locale.man_country" var="manCountry"/>
<fmt:message bundle="${loc}" key="locale.man_exist" var="manufacturerExist"/>
<fmt:message bundle="${loc}" key="locale.man_not_exist" var="manufacturerNotExist"/>
<fmt:message bundle="${loc}" key="locale.man_created" var="manufacturerCreated"/>
<fmt:message bundle="${loc}" key="locale.in_progress" var="inProgress"/>
<fmt:message bundle="${loc}" key="locale.new" var="newMes"/>
<fmt:message bundle="${loc}" key="locale.was_read" var="wasRead"/>
<fmt:message bundle="${loc}" key="locale.all_messages" var="allMessages"/>
<fmt:message bundle="${loc}" key="locale.sended_from" var="sendedFrom"/>
<fmt:message bundle="${loc}" key="locale.sended_before" var="sendedBefore"/>
<fmt:message bundle="${loc}" key="locale.read" var="read"/>
<fmt:message bundle="${loc}" key="locale.for" var="forr"/>
<fmt:message bundle="${loc}" key="locale.answer" var="answer"/>
<fmt:message bundle="${loc}" key="locale.success_send" var="successSend"/>
<fmt:message bundle="${loc}" key="locale.error_send" var="errorSend"/>
<fmt:message bundle="${loc}" key="locale.order_status" var="orderStatus"/>
<fmt:message bundle="${loc}" key="locale.order_number" var="orderNumber"/>
<fmt:message bundle="${loc}" key="locale.status_ordered" var="statusOrdered"/>
<fmt:message bundle="${loc}" key="locale.status_paid" var="statusPaid"/>
<fmt:message bundle="${loc}" key="locale.status_canceled" var="statusCanceled"/>
<fmt:message bundle="${loc}" key="locale.status_completed" var="statusCompleted"/>
<fmt:message bundle="${loc}" key="locale.all_orders" var="allOrders"/>
<fmt:message bundle="${loc}" key="locale.reestablish_order" var="reestablish"/>
<fmt:message bundle="${loc}" key="locale.order_canceled" var="orderCanceled"/>
<fmt:message bundle="${loc}" key="locale.order_not_found" var="orderNotFound"/>
<fmt:message bundle="${loc}" key="locale.payment" var="payment"/>
<fmt:message bundle="${loc}" key="locale.sure_pay" var="surePay"/>
<fmt:message bundle="${loc}" key="locale.ins_funds" var="insFunds"/>
<fmt:message bundle="${loc}" key="locale.proceed" var="proceed"/>
<fmt:message bundle="${loc}" key="locale.expired" var="expired"/>
<fmt:message bundle="${loc}" key="locale.drug_class" var="drugClas"/>
<c:if test="${sessionScope.user eq null}">
    <c:redirect url="/index.jsp"/>
</c:if>
<c:if test="${param.command eq 'GET_ALL_DRUGS' or param.command eq 'GET_DRUGS_BY_CLASS' or param.command eq 'EXTENDED_DRUG_SEARCH' or param.command eq 'SEARCH_DRUGS'}">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<style>
    * { padding: 0px; margin: 0px; font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; font-size: 1em; font-weight: 300;}
    #feedback {
        position: absolute;
        /*left: 22%;*/
        /*bottom: 120px;*/
        top:45px;
        background: #fff;
        border-radius: 5px;
        box-shadow: 0px 0px 3px 2px rgba(0,0,0,.5);
        z-index: 1;
        margin-top: 5px;
        width: 204px;
    }
    #feedback div {
        text-align: center; cursor: pointer; padding: 2px;
        z-index: 1;
    }
    #feedback ul { padding: 0px 10px 5px 10px; list-style: none; }
    #feedback li { margin-bottom: 5px; }
    #feedback input {
        display: inline-block;
        /*width: 225px;*/
    }

    #feedback input[type="submit"] {
        display: block;
        width: 225px;
    }

    #feedback a {
        background: #999;
        color: #000;
        border-radius: 5px;
        padding: 0px 5px;
        text-decoration: none;
        transition: background 0.3s 0s;}
    #feedback a:hover { background: #333; color: #fff; }
    #feedback .last { text-align: right; }
    #feedback {
        height: 24px;
        transition: all 0.3s;
        overflow: hidden;
    }

    #toggler_call:checked + #feedback {
        height: 450px;
        bottom: 32px;
        width: 250px;
    }

    #toggler_call{display: none}

</style>
</c:if>
<nav class="navbar navbar-default" style="background:#507ecf">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <c:if test="${param.command eq 'GET_ALL_DRUGS' or param.command eq 'GET_DOCTORS'
         or param.command eq 'GET_DRUGS_BY_CLASS' or param.command eq 'EXTENDED_DRUG_SEARCH'
          or param.command eq 'SEARCH_DRUGS' or param.command eq 'SEARCH_DOCTORS' or param.command eq 'GET_DOCTORS_BY_SPECIALIZATION'
          or param.command eq 'GET_ALL_ORDERS'}">
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

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

            <c:if test="${param.command eq 'GET_ALL_DRUGS' or param.command eq 'GET_DOCTORS'
            or param.command eq 'GET_DRUGS_BY_CLASS' or param.command eq 'EXTENDED_DRUG_SEARCH'
            or param.command eq 'SEARCH_DRUGS' or param.command eq 'SEARCH_DOCTORS' or param.command eq 'GET_DOCTORS_BY_SPECIALIZATION'}">
                <div class="navbar-form navbar-left navbar-search" role="search" style="padding-top:5px;">
                    <div class="form-group">
                        <input id="query_string" type="text" class="form-control" placeholder="${search}">
                    </div>
                    <button id="search_button" type="submit" class="btn btn-info">${search}</button>
                    <c:if test="${param.command eq 'GET_ALL_DRUGS' or param.command eq 'GET_DRUGS_BY_CLASS' or param.command eq 'EXTENDED_DRUG_SEARCH' or param.command eq 'SEARCH_DRUGS'}">
                        <input type="checkbox" id="toggler_call" />

                        <div id="feedback">
                            <label for="toggler_call"><div>${extSearch}</div></label>

                            <form action="/controller" id="ext_search_form">
                                <input type="hidden" name="command" value="EXTENDED_DRUG_SEARCH">
                                <ul>
                                    <div class="form-group">
                                        <label for="drug_name">${drugName}</label>
                                        <input id="drug_name" class="form-control" type="text" name="name"/>
                                    </div>

                                    <div class="form-group">
                                        <label for="active_substance">${actSubs}</label>
                                        <input type="text" class="form-control" id="active_substance" name="active_substance"/>
                                    </div>
                                    <div class="form-group">
                                        <label for="dr_price">${maxPrice}</label>
                                        <input type="number" class="form-control" name="max_price" min="10" id="dr_price" step="0.1"/>
                                    </div>
                                    <li style="text-align: center;">
                                        <label>${drugClass}</label>
                                        <select id="dr_class_select" class="form-control" name="dr_class">
                                            <option value="" selected>${unknown}</option>
                                            <c:forEach items="${drugClasses}" var="drugClass">
                                                <option value="${drugClass.name}">${drugClass.name}</option>
                                            </c:forEach>
                                        </select>
                                    </li>
                                    <li style="text-align: center;">
                                        <label>${manufacturer}</label>
                                        <select id="drug_manufacturer_s" class="form-control" name="dr_manufacture">
                                            <option value="" selected>${unknown}</option>
                                            <c:forEach items="${drugManufactures}" var="man">
                                                <option value="${man.name},${man.country}">${man.name}(${man.country})</option>
                                            </c:forEach>
                                        </select>
                                    </li>
                                    <li>
                                        <input id="in_stock_only" type="checkbox" name="only_in_stock" value="true"> ${inStock}<br>
                                    </li>
                                    <li>
                                        <input id="without_prescription" type="checkbox" name="only_free" value="false"> ${withoutPresc}<br>
                                    </li>
                                    <input id="ext_search" class="btn btn-success" type="submit" value="${search}"/>

                                </ul>
                            </form>

                        </div>
                    </c:if>
                </div>
            </c:if>

            <div class="btn-group" style="padding-top:20px;" role="group">
                <a href="/main.jsp" class="btn btn-default btn-primary">${myCabinet}</a>
                <c:if test="${user.userRole ne 'DOCTOR'}">
                    <a href="/controller?command=GET_ALL_ORDERS&page=1&overload=true" class="btn btn-default btn-primary">${myOrders}</a>
                </c:if>
                <c:if test="${user.userRole eq 'CLIENT' or user.userRole eq 'DOCTOR'}">
                    <a href="/controller?command=GET_PRESCRIPTIONS&page=1&overload=true" class="btn btn-default btn-primary">${myPrescriptions}</a>
                    <a href="/controller?command=GET_REQUESTS&page=1&overload=true" class="btn btn-default btn-primary">${myRequests}
                        <c:if test="${user.userRole eq 'DOCTOR'}">
                            <jsp:useBean id="request_count" scope="session" type="java.lang.Integer"/>
                            <span id="req_count" class="badge">${request_count}</span>
                        </c:if>
                    </a>
                    <jsp:useBean id="count" scope="session" type="java.lang.Integer"/>
                    <a href="/controller?command=GET_ALL_MESSAGES&page=1&overload=true" class="btn btn-default btn-primary">${messages} <span id="mes_count" class="badge">${count}</span></a>
                </c:if>
                <a href="/controller?command=OPEN_SETTINGS" class="btn btn-default btn-primary">${mySettings}</a>
                <a href="/controller?command=EXTENDED_DRUG_SEARCH&overload=true&page=1" class="btn btn-default btn-primary">${drugs}</a>
                <a href="/controller?command=GET_DOCTORS&overload=true&page=1" class="btn btn-default btn-primary">${doctors}</a>
                <a href="/controller?command=LOG_OUT" class="btn btn-default btn-primary">${logOut}</a>
            </div>

            <a class="switch_locale" href="controller?command=SWITCH_LOCALE&language=en"><img src="images/united-kingdom-flag_9815.png" alt="english"></a>
            <a class="switch_locale" href="controller?command=SWITCH_LOCALE&language=ru"><img src="images/russia-flag_3763.png" alt="русский"></a>

        </div>
    </div>
</nav>
<script>
    $("#ext_search").click(function(){
        $("#toggler_call").attr("checked", false);
        return false;
    });
</script>