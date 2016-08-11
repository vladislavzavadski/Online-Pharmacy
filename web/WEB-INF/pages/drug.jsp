<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="drugList" scope="request" class="java.util.ArrayList"/>
<c:forEach items="${drugList}" var="drug">
    <div class="col-xs-6 col-lg-6" style="height:400px; overflow:hidden">
        <a href="/controller?command=GET_DRUG_DETAILS&dr_id=${drug.id}">
            <h2>${drug.name}</h2>
            <img src="${drug.pathToImage}" class="img-responsive" alt="${drug.name}" height="150" width="150"/>
        </a>
        <b>
            Класс лекарства:
        </b>&nbsp;
        <span title="${drug.drugClass.description}">
                ${drug.drugClass.name}
        </span>
        <b>
            Активное вещество:
        </b>&nbsp;
        <span>
                ${drug.activeSubstance}
        </span>
        <p>${drug.description}</p>
    </div>
</c:forEach>
<div id="LoadedContent"></div>