<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="drugList" scope="request" class="java.util.ArrayList"/>
<c:set var="language" value="${not empty sessionScope.language ? sessionScope.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="resource.locale" var="loc"/>
<fmt:message bundle="${loc}" key="locale.drug_class" var="drugClass"/>
<fmt:message bundle="${loc}" key="locale.active_substance" var="actSubs"/>
<fmt:message bundle="${loc}" key="locale.not_found" var="notFound"/>
<c:forEach items="${drugList}" var="drug">
    <div class="col-xs-6 col-lg-6" style="height:400px; overflow:hidden">
        <a href="/controller?command=GET_DRUG_DETAILS&dr_id=${drug.id}">
            <h2>${drug.name}
                <span class="label label-success">$${drug.price}</span></h2>
            <img src="/controller?command=GET_DRUG_IMAGE&dr_id=${drug.id}" class="img-responsive" alt="${drug.name}" height="150" width="150"/>
        </a>
        <b>
           ${drugClass} :
        </b>&nbsp;
        <span title="${drug.drugClass.description}">
                ${drug.drugClass.name}
        </span>
        <br/>
        <b>
            ${actSubs}:
        </b>&nbsp;
        <span>
                ${drug.activeSubstance}
        </span>
        <br/>
        <p>${drug.description}</p>
    </div>
</c:forEach>
<c:if test="${drugList.size() eq 0 and param.page eq 1}">
    <h2>${notFound}</h2>
</c:if>
<c:if test="${drugList.size() >0}">
    <div id="LoadedContent" data-stop="${drugList.size()<6}"></div>
</c:if>