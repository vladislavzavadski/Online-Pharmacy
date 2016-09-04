<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 22.07.16
  Time: 18:50
  To change this template use File | Settings | File Templates.
--%>
<%@include file="header.jsp"%>
<%@include file="footer.jsp"%>
<c:set var="prevRequest" value="<%=request.getRequestURL().toString()%>" scope="session"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
    <title><jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/></title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/simple-sidebar.css" rel="stylesheet">
    <link href="css/sticky-footer-navbar.css" rel="stylesheet">
    <script src="js/bootstrap.js"></script>
</head>
<body>
    <div class="container">
        <!-- Sidebar -->
        <div class="container" style="background: white; padding-top: 10px">
            <div class="row">
                <div class="col-lg-6">
                     <img src="/controller?command=GET_PROFILE_IMAGE" class="img-responsive" alt="<jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/>" width="300" height="400"/>
                     <!--<h1><jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/></h1>-->
                </div>
                <div class="col-lg-6">
                    <h1><jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/></h1>
                    <b>${phoneNumber}: </b>&nbsp
                    <span><jsp:getProperty name="user" property="phone"/></span>
                    <br/>
                    <b>e-mail: </b>&nbsp
                    <span><a href="mailto:<jsp:getProperty name="user" property="mail"/>"><jsp:getProperty name="user" property="mail"/></a> </span>
                    <br/>
                    <b>${balance}:</b>
                    <span>${user.balance}</span>
                    <br/>
                    <b>${gender}: </b>&nbsp
                    <c:choose>
                        <c:when test="${user.gender eq 'MALE'}">
                            <span>${male}</span>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${user.gender eq 'FEMALE'}">
                                    <span>${female}</span>
                                </c:when>
                                <c:otherwise>
                                    <span>${unknown}</span>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>



</body>
</html>
