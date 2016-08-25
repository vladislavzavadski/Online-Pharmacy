<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="orderList" scope="request" class="java.util.ArrayList"/>
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
                <c:choose>
                    <c:when test="${order.orderStatus eq 'ORDERED'}">
                        <b>Статус:</b>&nbsp;<span class="label label-warning">Заказано</span>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${order.orderStatus eq 'PAID'}">
                                <b>Статус:</b>&nbsp;<span class="label label-success">Оплачено</span>
                            </c:when>
                            <c:otherwise>
                                <b>Статус:</b>&nbsp;<span class="label label-danger">Отменено</span>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
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
            </div>
            <c:if test="${order.orderStatus eq 'ORDERED'}">
                <div class="col-lg-4" style="padding-top:40px;">
                    <a class="btn btn-primary btn-danger cancel_order" href="/controller" data-order="${order.id}">Отменить</a>
                    <a href="#" class="btn btn-primary btn-success">Оплатить</a>
                </div>
            </c:if>
        </div>
    </div>
</c:forEach>
<div id="LoadedContent"></div>