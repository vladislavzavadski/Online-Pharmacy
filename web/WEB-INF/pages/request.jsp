<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 06.09.16
  Time: 22:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="requests" scope="request" class="java.util.ArrayList"/>
<c:forEach items="${requests}" var="req">
    <div class="col-lg-4">
        <a href="/controller?command=GET_DRUG_DETAILS&dr_id=${req.drug.id}"><h2>${req.drug.name}</h2>
            <img src="/controller?command=GET_DRUG_IMAGE&dr_id=${req.drug.id}" width="200" height="200">
        </a>
        <br/>
        <b>Статус:</b>&nbsp;
        <c:choose>
            <c:when test="${req.requestStatus eq 'IN_PROGRESS'}">
                <span class="label label-warning">В обработке</span>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${req.requestStatus eq 'DENIED'}">
                        <span class="label label-danger">Отказано</span>
                    </c:when>
                    <c:otherwise>
                        <span class="label label-success">Одобрено</span>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <br/>
        <b>Доктор:</b>&nbsp;
        <span><a href="/controller?command=GET_USER_DETAILS&login=${req.doctor.login}&register_type=${req.doctor.registrationType}"><label>${req.doctor.firstName} ${req.doctor.secondName}</label></a></span>
        <br/>
        <b>Продлить до:</b>&nbsp;
        <span>${req.prolongDate}</span>
        <br/>
        <b>Дата запроса: </b>&nbsp;
        <span>${req.requestDate}</span>
        <br/>
        <c:if test="${req.requestStatus ne 'IN_PROGRESS'}">
            <b>Дата ответа: </b>
            <span>${req.responseDate}</span>
        </c:if>
    </div>
    <div class="col-lg-4" align="justify">
        <h3>Ваш комментарий:</h3>
        <p style="height:150px; overflow:auto">
                ${req.clientComment}
        </p>
    </div>
    <c:if test="${req.requestStatus ne 'IN_PROGRESS'}">
        <div class="col-lg-4" align="justify">
            <h3>Комментарий врача:</h3>
            <p style="height:150px; overflow:auto">
                    ${req.doctorComment}
            </p>
        </div>
    </c:if>
</c:forEach>
<div id="LoadedContent"></div>
