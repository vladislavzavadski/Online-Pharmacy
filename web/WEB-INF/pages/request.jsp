<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 06.09.16
  Time: 22:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="requests" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
<c:set var="language" value="${not empty sessionScope.language ? sessionScope.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="resource.locale" var="loc"/>
<fmt:message bundle="${loc}" key="locale.in_progress" var="inProgress"/>
<fmt:message bundle="${loc}" key="locale.denied" var="denied"/>
<fmt:message bundle="${loc}" key="locale.confirmed" var="confirmed"/>
<fmt:message bundle="${loc}" key="locale.client" var="client"/>
<fmt:message bundle="${loc}" key="locale.doc" var="doc"/>
<fmt:message bundle="${loc}" key="locale.not_found" var="notFound"/>
<fmt:message bundle="${loc}" key="locale.confirm" var="confirm"/>
<fmt:message bundle="${loc}" key="locale.doctor_comment" var="doctorComment"/>
<fmt:message bundle="${loc}" key="locale.client_comment" var="clientComment"/>
<fmt:message bundle="${loc}" key="locale.response_date" var="responseDate"/>
<fmt:message bundle="${loc}" key="locale.request_date" var="requestDate"/>
<fmt:message bundle="${loc}" key="locale.prolong_to" var="prolongTo"/>
<c:forEach items="${requests}" var="req">
    <div class="col-lg-4">
        <a href="/controller?command=GET_DRUG_DETAILS&dr_id=${req.drug.id}"><h2>${req.drug.name}</h2>
            <img src="/controller?command=GET_DRUG_IMAGE&dr_id=${req.drug.id}" width="200" height="200">
        </a>
        <br/>
        <b>Статус:</b>&nbsp;
        <c:choose>
            <c:when test="${req.requestStatus eq 'IN_PROGRESS'}">
                <span class="label label-warning">${inProgress}</span>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${req.requestStatus eq 'DENIED'}">
                        <span class="label label-danger">${denied}</span>
                    </c:when>
                    <c:otherwise>
                        <span class="label label-success">${confirmed}</span>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <br/>
        <b>${client}:</b>&nbsp;
        <span>${req.client.firstName} ${req.client.secondName}</span>
        <br/>
        <b>${doc}:</b>&nbsp;
        <span><a href="/controller?command=GET_USER_DETAILS&login=${req.doctor.login}&register_type=${req.doctor.registrationType}"><label>${req.doctor.firstName} ${req.doctor.secondName}</label></a></span>
        <br/>
        <b>${prolongTo}:</b>&nbsp;
        <span>${req.prolongDate}</span>
        <br/>
        <b>${requestDate}: </b>&nbsp;
        <span>${req.requestDate}</span>
        <br/>
        <c:if test="${req.requestStatus ne 'IN_PROGRESS'}">
            <b>${responseDate}: </b>
            <span>${req.responseDate}</span>
        </c:if>
    </div>
    <div class="col-lg-4" align="justify">
        <h3>${clientComment}:</h3>
        <p style="height:150px; overflow:auto">
                ${req.clientComment}
        </p>
    </div>
    <c:choose>
        <c:when test="${req.requestStatus ne 'IN_PROGRESS'}">
            <div class="col-lg-4" align="justify">
                <h3>${doctorComment}:</h3>
                <p style="height:150px; overflow:auto">
                        ${req.doctorComment}
                </p>
            </div>
        </c:when>
        <c:otherwise>
            <c:if test="${user.userRole eq 'DOCTOR'}">
                <div class="col-lg-4" style="top: 50px">
                    <button data-id="${req.id}" data-type="confirmed" data-exp="${req.prolongDate}" data-toggle="modal" data-target="#confirm-request" data-dosages="<c:forEach items="${req.drug.dosages}" var="dos">${dos} </c:forEach>" class="submit-request btn btn-primary btn-success">${confirm}</button>
                    <button data-type="denied" data-id="${req.id}"  data-toggle="modal" data-target="#denied-request" class="submit-request btn btn-primary btn-danger">Отклонить</button>
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>
</c:forEach>
<c:if test="${requests.size() eq 0 and param.page eq 1}">
    <h2>${notFound}</h2>
</c:if>
<div id="LoadedContent"></div>
