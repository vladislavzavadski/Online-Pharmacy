<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="mft" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<jsp:useBean id="messageList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
<c:set var="language" value="${not empty sessionScope.language ? sessionScope.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="resource.locale" var="loc"/>
<fmt:message bundle="${loc}" key="locale.not_response" var="notResponse"/>
<fmt:message bundle="${loc}" key="locale.not_found" var="notFound"/>
<fmt:message bundle="${loc}" key="locale.response" var="response"/>
<fmt:message bundle="${loc}" key="locale.mark_as_read" var="asReaded"/>
<c:forEach items="${messageList}" var="message">
    <div class="row" style="background: white; margin-bottom: 70px">
        <div class="col-lg-6">
            <div class="testimonial testimonial-primary">
                <div class="testimonial-section">
                        ${message.senderMessage}
                </div>
                <div class="testimonial-desc">
                    <img src="/controller?command=GET_USER_IMAGE&login=${message.sender.login}&register_type=${message.sender.registrationType}" alt="" />
                    <div class="testimonial-writer">
                        <div class="testimonial-writer-name">${message.sender.firstName} ${message.sender.secondName}</div>
                        <div class="testimonial-writer-designation">${message.requestDate}</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-6" >
            <div class="testimonial testimonial-success">
                <div class="testimonial-section">
                    <c:choose>
                        <c:when test="${message.messageStatus ne 'IN_PROGRESS'}">
                            ${message.receiverMessage}
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${user.userRole eq 'DOCTOR'}">
                                    <button data-receiver="${message.sender.firstName} ${message.sender.secondName}" data-toggle="modal" data-message="${message.id}" data-target="#response-modal" class="response-button btn btn-primary btn-primary">${response}</button>
                                </c:when>
                                <c:otherwise>
                                    <span style="color: red">${notResponse}</span>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>

                </div>
                <div class="testimonial-desc">
                    <img src="/controller?command=GET_USER_IMAGE&login=${message.receiver.login}&register_type=${message.receiver.registrationType}" alt="" />
                    <div class="testimonial-writer">
                        <c:choose>
                            <c:when test="${user.userRole eq 'CLIENT'}">
                                <a href="/controller?command=GET_USER_DETAILS&login=${message.receiver.login}&register_type=${message.receiver.registrationType}">
                                    <div class="testimonial-writer-name">${message.receiver.firstName} ${message.receiver.secondName}</div>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <div class="testimonial-writer-name">${message.receiver.firstName} ${message.receiver.secondName}</div>
                            </c:otherwise>
                        </c:choose>
                        <div class="testimonial-writer-designation">${message.responseDate}</div>
                        <c:if test="${message.messageStatus eq 'NEW' and user.userRole eq 'CLIENT'}">
                            <div>
                                <a class="change_status" href="/controller?command=MARK_MESSAGE&me_id=${message.id} " data-id="${message.id}">${asReaded}</a>
                            </div>
                        </c:if>
                    </div>

                </div>
            </div>
        </div>
    </div>
</c:forEach>
<c:if test="${messageList.size() eq 0 and param.page eq 1}">
    <h2>${notFound}</h2>
</c:if>
<div id="LoadedContent"></div>