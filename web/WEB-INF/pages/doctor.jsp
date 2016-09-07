<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 13.08.16
  Time: 14:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="doctorList" scope="request" class="java.util.ArrayList"/>
<c:forEach items="${doctorList}" var="doctor">
    <div class="col-lg-4">
        <a href="/controller?command=GET_USER_DETAILS&login=${doctor.login}&register_type=${doctor.registrationType}">
            <h2>${doctor.secondName} ${doctor.firstName}</h2>
            <img src="/controller?command=GET_USER_IMAGE&login=${doctor.login}&register_type=${doctor.registrationType}" class="img-responsive" width="150" height="200" alt="Фото доктора"/>
        </a>
        <br/>
        <b>Специализация:</b>&nbsp;
        <span>${doctor.userDescription.specialization}</span>
    </div>
</c:forEach>
<c:choose>
    <c:when test="${doctorList.size() ne 6}">
        <div id="stop" data-stop="${doctorList.size()<6}"></div>
    </c:when>
    <c:otherwise>
        <div id="LoadedContent" data-stop="${doctorList.size()<6}"></div>
    </c:otherwise>
</c:choose>

</>
