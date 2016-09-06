<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                    <span class="label label-success">Действует</span>
                </c:if>
                <c:if test="${today.time gt presc.expirationDate.time}">
                    <span class="label label-danger">Истёк</span>
                </c:if>
                <br/>
                <b>Дозировка: </b>&nbsp;
                <span>${presc.drugDosage}мг</span>
                <br/>
                <b>Количество: </b>&nbsp;
                <span>${presc.drugCount}</span>
                <br/>
                <b>Начало действия: </b>&nbsp;
                <span>${presc.appointmentDate}</span>
                <br/>
                <b>Окончание действия: </b>&nbsp;
                <span>${presc.expirationDate}</span>
                <br/>
                <b>Доктор: </b>&nbsp;
                <a href="/controller?command=GET_USER_DETAILS&login=${presc.doctor.login}&register_type=${presc.doctor.registrationType}">
                    <span>${presc.doctor.firstName} ${presc.doctor.secondName}</span>
                </a>
            </div>
        </div>
    </div>
</c:forEach>
<div id="LoadedContent"></div>
