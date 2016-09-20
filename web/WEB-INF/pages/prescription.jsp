<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 06.09.16
  Time: 8:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="prescriptions" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="today" class="java.util.Date" />
<c:set var="language" value="${not empty sessionScope.language ? sessionScope.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="resource.locale" var="loc"/>
<fmt:message bundle="${loc}" key="locale.not_found" var="notFound"/>
<fmt:message bundle="${loc}" key="locale.dosage" var="dosage"/>
<fmt:message bundle="${loc}" key="locale.drug_count" var="drugCount"/>
<fmt:message bundle="${loc}" key="locale.client" var="client"/>
<fmt:message bundle="${loc}" key="locale.doc" var="doc"/>
<fmt:message bundle="${loc}" key="locale.proceed" var="proceed"/>
<fmt:message bundle="${loc}" key="locale.expired" var="expired"/>
<fmt:message bundle="${loc}" key="locale.onset_act" var="onsetAct"/>
<fmt:message bundle="${loc}" key="locale.end_acts" var="endActs"/>
<c:forEach items="${prescriptions}" var="presc">
    <div class="col-xs-3 col-lg-6" >
        <div class="row">
            <div class="col-lg-6" style="height:200px; overflow:hidden">
                <a href="/controller?command=GET_DRUG_DETAILS&dr_id=${presc.drug.id}">
                    <h2>${presc.drug.name}</h2>
                    <img src="/controller?command=GET_DRUG_IMAGE&dr_id=${presc.drug.id}" class="img-responsive" alt="${presc.drug.name}" height="150" width="100"/>
                </a>
            </div>
            <div class="col-lg-6" style="padding-top:40px;">
                <b>Статус: </b>&nbsp;
                <c:if test="${presc.expirationDate.time ge today.time}">
                    <span class="label label-success">${proceed}</span>
                </c:if>
                <c:if test="${today.time gt presc.expirationDate.time}">
                    <span class="label label-danger">${expired}</span>
                </c:if>
                <br/>
                <b>${dosage}: </b>&nbsp;
                <span>${presc.drugDosage}мг</span>
                <br/>
                <b>${drugCount}: </b>&nbsp;
                <span>${presc.drugCount}</span>
                <br/>
                <b>${onsetAct}: </b>&nbsp;
                <span>${presc.appointmentDate}</span>
                <br/>
                <b>${endActs}: </b>&nbsp;
                <span>${presc.expirationDate}</span>
                <br/>
                <b>${doc}: </b>&nbsp;
                <a href="/controller?command=GET_USER_DETAILS&login=${presc.doctor.login}&register_type=${presc.doctor.registrationType}">
                    <span>${presc.doctor.firstName} ${presc.doctor.secondName}</span>
                </a>
                <br/>
                <b>${client}: </b>&nbsp;
                <span>${presc.client.firstName} ${presc.client.secondName}</span>
            </div>
        </div>
    </div>
</c:forEach>
<c:if test="${prescriptions.size() eq 0 and param.page eq 1}">
    <h2>${notFound}</h2>
</c:if>
<div id="LoadedContent"></div>
