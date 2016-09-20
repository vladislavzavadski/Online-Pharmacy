<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="orderList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
<c:set var="language" value="${not empty sessionScope.language ? sessionScope.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="resource.locale" var="loc"/>
<fmt:message bundle="${loc}" key="locale.sum" var="sum"/>
<fmt:message bundle="${loc}" key="locale.dosage" var="dosage"/>
<fmt:message bundle="${loc}" key="locale.order_status" var="orderStatus"/>
<fmt:message bundle="${loc}" key="locale.status_ordered" var="statusOrdered"/>
<fmt:message bundle="${loc}" key="locale.status_paid" var="statusPaid"/>
<fmt:message bundle="${loc}" key="locale.status_canceled" var="statusCanceled"/>
<fmt:message bundle="${loc}" key="locale.status_completed" var="statusCompleted"/>
<fmt:message bundle="${loc}" key="locale.drug_count" var="drugCount"/>
<fmt:message bundle="${loc}" key="locale.not_found" var="notFound"/>
<fmt:message bundle="${loc}" key="locale.order_date" var="orderDate"/>
<fmt:message bundle="${loc}" key="locale.order_number" var="orderNumber"/>
<fmt:message bundle="${loc}" key="locale.client" var="client"/>
<fmt:message bundle="${loc}" key="locale.cancel" var="cancel"/>
<fmt:message bundle="${loc}" key="locale.pay" var="pay"/>
<fmt:message bundle="${loc}" key="locale.complete" var="complete"/>
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
                <c:if test="${order.orderStatus eq 'ORDERED'}">
                    <b>${orderStatus}:</b>&nbsp;<span class="label label-warning">${statusOrdered}</span>
                </c:if>
                <c:if test="${order.orderStatus eq 'PAID'}">
                    <b>${orderStatus}:</b>&nbsp;<span class="label label-success">${statusPaid}</span>
                </c:if>
                <c:if test="${order.orderStatus eq 'CANCELED'}">
                    <b>${orderStatus}:</b>&nbsp;<span class="label label-danger">${statusCanceled}</span>
                </c:if>
                <c:if test="${order.orderStatus eq 'COMPLETED'}">
                    <b>${orderStatus}:</b>&nbsp;<span class="label label-default">${statusCompleted}</span>
                </c:if>
                <br/>
                <b>${dosage}:</b>&nbsp;
                <span>${order.drugDosage}</span>
                <br/>
                <b>${drugCount}:</b>&nbsp;
                <span>${order.drugCount}</span>
                <br/>
                <b>${sum}: </b>&nbsp;
                <span>$${order.totalSum}</span>
                <br/>
                <b>${orderDate}: </b>&nbsp;
                <span>${order.orderDate}</span>
                <br/>
                <b>${orderNumber}:</b>&nbsp;
                <span>${order.id}</span>
                <br/>
                <b>${client}:</b>&nbsp;
                <span>${order.client.firstName} ${order.client.secondName}</span>
            </div>
            <c:if test="${order.orderStatus eq 'ORDERED'}">
                <div class="col-lg-4" style="padding-top:40px;">
                    <a class="btn btn-primary btn-danger cancel_order" href="/controller" data-order="${order.id}">${cancel}</a>
                    <a data-toggle="modal" data-target="#confirm-pay" class="btn btn-primary btn-success pay_order" data-sum="${order.totalSum}" data-order="${order.id}">${pay}</a>
                </div>
            </c:if>
            <c:if test="${user.userRole eq 'PHARMACIST' and order.orderStatus eq 'PAID'}">
                <div class="col-lg-4" style="padding-top:40px;">
                    <a class="btn btn-primary btn-success complete_order" href="/controller" data-order="${order.id}">${complete}</a>
                </div>
            </c:if>
        </div>
    </div>
</c:forEach>
<c:if test="${orderList.size() eq 0 and param.page eq 1}">
    <h2>${notFound}</h2>
</c:if>
<div id="LoadedContent"></div>