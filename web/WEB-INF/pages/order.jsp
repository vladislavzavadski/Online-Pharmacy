<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="orderList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
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
                    <b>Статус:</b>&nbsp;<span class="label label-warning">Заказано</span>
                </c:if>
                <c:if test="${order.orderStatus eq 'PAID'}">
                    <b>Статус:</b>&nbsp;<span class="label label-success">Оплачено</span>
                </c:if>
                <c:if test="${order.orderStatus eq 'CANCELED'}">
                    <b>Статус:</b>&nbsp;<span class="label label-danger">Отменено</span>
                </c:if>
                <c:if test="${order.orderStatus eq 'COMPLETED'}">
                    <b>Статус:</b>&nbsp;<span class="label label-default">Завершено</span>
                </c:if>
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
                <br/>
                <b>Номер заказа:</b>&nbsp;
                <span>${order.id}</span>
                <br/>
                <b>Клиент:</b>&nbsp;
                <span>${order.client.firstName} ${order.client.secondName}</span>
            </div>
            <c:if test="${order.orderStatus eq 'ORDERED'}">
                <div class="col-lg-4" style="padding-top:40px;">
                    <a class="btn btn-primary btn-danger cancel_order" href="/controller" data-order="${order.id}">Отменить</a>
                    <a data-toggle="modal" data-target="#confirm-pay" class="btn btn-primary btn-success pay_order" data-sum="${order.totalSum}" data-order="${order.id}">Оплатить</a>
                </div>
            </c:if>
            <c:if test="${user.userRole eq 'PHARMACIST' and order.orderStatus eq 'PAID'}">
                <div class="col-lg-4" style="padding-top:40px;">
                    <a class="btn btn-primary btn-success complete_order" href="/controller" data-order="${order.id}">Завершить</a>
                </div>
            </c:if>
        </div>
    </div>
</c:forEach>
<c:if test="${orderList.size() eq 0 and param.page eq 1}">
    <h2>По вашему запросу ничего не найдено</h2>
</c:if>
<div id="LoadedContent"></div>