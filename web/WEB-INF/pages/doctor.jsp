<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty sessionScope.language ? sessionScope.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="resource.locale" var="loc"/>
<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 13.08.16
  Time: 14:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="doctorList" scope="request" class="java.util.ArrayList"/>
<fmt:message bundle="${loc}" key="locale.specialization" var="specialization"/>
<fmt:message bundle="${loc}" key="locale.not_found" var="notFound"/>
<c:forEach items="${doctorList}" var="doctor">
    <div class="col-lg-6" style="height: 400; overflow: hidden">
        <a href="/controller?command=GET_USER_DETAILS&login=${doctor.login}&register_type=${doctor.registrationType}">
            <h2>${doctor.secondName} ${doctor.firstName}</h2>
            <img src="/controller?command=GET_USER_IMAGE&login=${doctor.login}&register_type=${doctor.registrationType}" class="img-responsive" width="150" height="200" alt="Фото доктора"/>
        </a>
        <br/>
        <b>${specialization}:</b>&nbsp;
        <span>${doctor.userDescription.specialization}</span>
    </div>
</c:forEach>
<c:if test="${doctorList.size() eq 0 and param.page eq 1}">
    <h2>${notFound}</h2>
</c:if>
<c:choose>
    <c:when test="${doctorList.size() ne 6}">
        <div id="stop" data-stop="${doctorList.size()<6}"></div>
    </c:when>
    <c:otherwise>
        <div id="LoadedContent" data-stop="${doctorList.size()<6}"></div>
    </c:otherwise>
</c:choose>

