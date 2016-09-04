<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<jsp:useBean id="messageList" scope="request" class="java.util.ArrayList"/>
<c:forEach items="${messageList}" var="message">
    <div class="col-lg-6" style="background: white; margin-bottom: 70px">
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
    <div class="col-lg-6" style="background: white; margin-bottom: 70px">
        <div class="testimonial testimonial-success">
            <div class="testimonial-section">
                <c:choose>
                    <c:when test="${message.messageStatus ne 'IN_PROGRESS'}">
                        ${message.receiverMessage}
                    </c:when>
                    <c:otherwise>
                        <span style="color: red">Ваш собеседник еще не ответил</span>
                    </c:otherwise>
                </c:choose>

            </div>
            <div class="testimonial-desc">
                <img src="/controller?command=GET_USER_IMAGE&login=${message.receiver.login}&register_type=${message.receiver.registrationType}" alt="" />
                <div class="testimonial-writer">
                    <a href="/controller?command=GET_USER_DETAILS&login=${message.receiver.login}&register_type=${message.receiver.registrationType}">
                        <div class="testimonial-writer-name">${message.receiver.firstName} ${message.receiver.secondName}</div>
                    </a>
                    <div class="testimonial-writer-designation">${message.responseDate}</div>
                    <c:if test="${message.messageStatus eq 'NEW'}">
                        <div>
                            <a class="change_status" href="/controller?command=MARK_MESSAGE&me_id=${message.id} " data-id="${message.id}">Пометить сообщение как прочитанное</a>
                        </div>
                    </c:if>
                </div>

            </div>
        </div>
    </div>



</c:forEach>
<div id="LoadedContent"></div>