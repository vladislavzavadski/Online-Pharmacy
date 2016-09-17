<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@include file="header.jsp"%>
<%@include file="footer.jsp"%>
<jsp:useBean id="doctor" scope="request" class="by.training.online_pharmacy.domain.user.User"/>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>${doctor.secondName} ${doctor.firstName}</title>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet">
        <script src="js/bootstrap-datetimepicker.min.js"></script>
        <link rel="stylesheet" href="https://formden.com/static/cdn/bootstrap-iso.css" /> 
        <script type="text/javascript" src="http://mybootstrap.ru/wp-content/themes/clear-theme/js/bootstrap-typeahead.js"></script>
        <link rel="stylesheet" href="https://formden.com/static/cdn/font-awesome/4.4.0/css/font-awesome.min.css" />
        <style>.bootstrap-iso .formden_header h2, .bootstrap-iso .formden_header p, .bootstrap-iso form{font-family: Arial, Helvetica, sans-serif; color: black}.bootstrap-iso form button, .bootstrap-iso form button:hover{color: white !important;} .asteriskField{color: red;}</style>
        <script type="text/javascript" src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/simple-sidebar.css" rel="stylesheet">
        <link href="css/sticky-footer-navbar.css" rel="stylesheet">
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <script src="js/bootstrap.js"></script>
        <style>
            #notifies {
                position:fixed;
                width:400px;
                height:auto;
                top:100px;
                right:20px;
                z-index: 1;
            }
        </style>
    </head>
    <body>
        <div class="container content">
            <div id="notifies"></div>
            <div class="container" style="background:white;">
                <div class="row">
                    <div class="col-lg-6">
                        <h1>${doctor.secondName} ${doctor.firstName}</h1>
                        <img src="/controller?command=GET_USER_IMAGE&login=${doctor.login}&register_type=${doctor.registrationType}" alt="Фото доктора" width="220" height="250"/>
                    </div>
                    <div class="col-lg-6" style="padding-top:30px;">
                            <b>${specialization}:</b>&nbsp;
                            <span>${doctor.userDescription.specialization}</span>
                            <br/>
                            <b>${phoneNumber}:</b>&nbsp;
                            <span>${doctor.phone}</span>
                            <br/>
                            <b>${gender}:</b>&nbsp;
                        <c:choose>
                            <c:when test="${doctor.gender eq 'MALE'}">
                                <span>${male}</span>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${doctor.gender eq 'FEMALE'}">
                                        <span>${female}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span>${unknown}</span>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                            <br/>
                            <b>e-mail</b>&nbsp;
                            <span><a href="mailto:${doctor.mail}">${doctor.mail}</a></span>
                            <br/>
                            <b>${description}:</b>
                            <p align="justify" style="height:200px; overflow:auto">
                                ${doctor.userDescription.description}
                            </p>

                    </div>
                </div>
                <form class="form-horizontal">
                    <fieldset>
                        <legend>${docMessage}</legend>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="comment">${message}: (<span id="symbols_enable">1000</span>)</label>
                            <div class="col-md-4">
                                <textarea class="form-control" maxlength="1000" id="comment" name="comment" placeholder="${message}..."></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="send_message"></label>

                            <div class="col-md-4">
                                <button id="send_message" class="btn btn-primary">Отправить</button>
                            </div>
                        </div>
                    </fieldset>
                </form>
                <script>
                    $("#comment").on('input',function (e) {
                        var commentLength = $(this).val().length;
                        $("#symbols_enable").html(1000-commentLength);
                    });
                    $("#send_message").click(function () {
                        var comment = $("#comment").val();
                        $.ajax({
                            url: 'controller',
                            dataType: 'json',
                            type: 'POST',
                            data: {command:'SEND_MESSAGE', message:comment, receiver_login:'${doctor.login}', receiver_login_via: '${doctor.registrationType}'},
                            success:function (data) {
                                if(data.result){
                                    Notify.generate('${mesSended}', '${completed}', 1);
                                }
                                else {
                                    Notify.generate('${mesError}', '${error}', 2);
                                }
                            }
                        });
                        return false;
                    });
                </script>
            </div>
        </div>
    </body>
    <script>
        Notify = {
            TYPE_INFO: 0,
            TYPE_SUCCESS: 1,
            TYPE_WARNING: 2,
            TYPE_DANGER: 3,
            generate: function (aText, aOptHeader, aOptType_int) {
                var lTypeIndexes = [this.TYPE_INFO, this.TYPE_SUCCESS, this.TYPE_WARNING, this.TYPE_DANGER];
                var ltypes = ['alert-info', 'alert-success', 'alert-warning', 'alert-danger'];
                var ltype = ltypes[this.TYPE_INFO];
                if (aOptType_int !== undefined && lTypeIndexes.indexOf(aOptType_int) !== -1) {
                    ltype = ltypes[aOptType_int];
                }
                var lText = '';
                if (aOptHeader) {
                    lText += "<h4>"+aOptHeader+"</h4>";
                }
                lText += "<p>"+aText+"</p>";
                var lNotify_e = $("<div class='alert "+ltype+"'><button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>×</span></button>"+lText+"</div>");
                setTimeout(function () {
                    lNotify_e.alert('close');
                }, 3000);
                lNotify_e.appendTo($("#notifies"));
            }
        };
    </script>
    <c:if test="${doctor.userRole eq 'CLIENT' or doctor.userRole eq 'DOCTOR'}">
        <script src="js/sendRequest.js"></script>
    </c:if>
    <c:if test="${doctor.userRole eq 'DOCTOR'}">
        <script src="js/requestsForPrescription.js"></script>
    </c:if>
    <script src="js/switchLocale.js"></script>
</html>