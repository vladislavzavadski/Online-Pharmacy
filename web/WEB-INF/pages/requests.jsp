<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 06.09.16
  Time: 11:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp"%>
<%@include file="footer.jsp"%>
<jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
<c:set var="language" value="${not empty sessionScope.language ? sessionScope.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="resource.locale" var="loc"/>
<fmt:message bundle="${loc}" key="locale.requests_for_prescriptions" var="requestsForPrescriptions"/>
<fmt:message bundle="${loc}" key="locale.in_progress" var="inProgress"/>
<fmt:message bundle="${loc}" key="locale.denied" var="denied"/>
<fmt:message bundle="${loc}" key="locale.confirmed" var="confirmed"/>
<fmt:message bundle="${loc}" key="locale.end_acts" var="endActs"/>
<fmt:message bundle="${loc}" key="locale.request_status" var="requestStatus"/>
<fmt:message bundle="${loc}" key="locale.answer_sended" var="answerSended"/>
<fmt:message bundle="${loc}" key="locale.request_denied" var="requestDenied"/>
<fmt:message bundle="${loc}" key="locale.request_confirmed" var="requestConfirmed"/>
<fmt:message bundle="${loc}" key="locale.error_sending" var="errorSending"/>
<fmt:message bundle="${loc}" key="locale.critical_error" var="criticalError"/>
<fmt:message bundle="${loc}" key="locale.date" var="date"/>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>${requestsForPrescriptions}</title>
    <!-- Bootstrap -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/simple-sidebar.css" rel="stylesheet">
    <link href="css/sticky-footer-navbar.css" rel="stylesheet">
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/js/bootstrap-datepicker.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/css/bootstrap-datepicker3.css"/>
    <![endif]-->
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
    <script>
        $(document).ready(function(){
            var date_input=$('input[data-type=date]'); //our date input has the name "date"
            var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
            date_input.datepicker({
                maxDate: 'now',
                format: 'yyyy-mm-dd',
                container: container,
                todayHighlight: true,
                autoclose: true,
                disableEntry: true
            }).on('changeDate', function () {
                if($(this).attr('id')!='exp_date') {
                    if ($(this).attr("id") == 'from_date') {
                        if ($("#to_date").val() != "") {
                            if (new Date($(this).val()) > new Date($('#to_date').val())) {
                                $(this).val("");
                            }
                        }
                    }
                    else {
                        if ($("#from_date").val() != "") {
                            if (new Date($(this).val()) < new Date($("#from_date").val())) {
                                $(this).val("");
                            }
                        }
                    }
                }
                else {
                    if(new Date()>new Date($(this).val())){
                        $(this).val("");
                    }
                }

            });
        });
    </script>
    <jsp:useBean id="requests" scope="request" class="java.util.ArrayList"/>
</head>
<body>
    <div class="container content">
        <div id="notifies"></div>
        <h1 class="display_1">${myRequests}</h1>
        <form id="req_form">
            <nobr>
            <label for="drug_name">${drugName}:</label>
            <input id="drug_name" type="text" name="drug_name">

                <label for="from_date">${sendedFrom}:</label>
                <input class="date" id="from_date" type="text" name="date_from" data-type="date">
                <label for="to_date">${sendedBefore}:</label>
                <input class="date" id="to_date" type="text" name="date_to" data-type="date">
            </nobr>
                <label for="pr_status">${requestStatus}</label>
                <select id="pr_status" name="status">
                    <option value="" selected>${unknown}</option>
                    <option value="in_progress">${inProgress}</option>
                    <option value="denied">${denied}</option>
                    <option value="confirmed">${confirmed}</option>
                </select>
                <button id="search_by_date" class="btn btn-primary btn-primary">${search}</button>

        </form>
        <script>
            var url = "/controller?command=GET_REQUESTS&";
            var loadUrl = "/controller?command=${param.command}&drug_name=${param.drug_name}&date_from=${param.date_from}&date_to=${param.date_to}&status=${param.status}&page=";
            var thisPageNum = 2;

            $('#req_form').submit(function () {
                currentUrl=url+$(this).serialize()+"&page=";
                $.get(currentUrl+1, function (data) {
                    $('#requests').html(data);
                });

                var page = {foo:"page"};

                window.history.pushState(page, "page", currentUrl+1+"&overload=true");
                thisPageNum = 2;
                return false;
            });

        </script>
        <div class="container" style="background:white">
            <div class="row border-between" id="requests">
                <jsp:include page="/request"/>
            </div>
        </div>
        <script>
            var thisWork = true;
            function downloadContent(){
                if(thisWork) {
                    thisWork = false;
                    $.get(loadUrl + thisPageNum, function (data) {
                        $("#LoadedContent").html($("#LoadedContent").html() + " " + data);
                        thisWork = true;
                        thisPageNum = thisPageNum + 1;
                    });
                }

            }
            $(document).ready(function(){
                var scrH = $(window).height();
                $(window).scroll(function(){
                    var scro = $(this).scrollTop();
                    var scrHP = $("#requests").height();
                    var scrH2 = 0;
                    scrH2 = scrH + scro;
                    var leftH = scrHP - scrH2;

                    if(leftH < 200){
                        downloadContent();
                    }
                });
            });
        </script>
    </div>
    <c:if test="${user.userRole eq 'DOCTOR'}">
        <div class="modal fade" id="confirm-request" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" align="center">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть"/>
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                    </div>
                    <form id="confirm_request" class="form-horizontal" method="post" action="/controller">
                    <div class="modal-body">
                            <input type="hidden" name="command" value="ANSWER_FOR_REQUEST">
                            <input type="hidden" name="status" value="CONFIRMED">
                            <input id="re_id" type="hidden" name="request_id" >
                        <div class="form-group">
                            <label for="drug_count">${drugCount}:</label>
                            <input placeholder="${drugCount}" class="form-control" id="drug_count" type="number" min="1" max="10" step="1" name="drug_count" required>
                        </div>
                        <div class="form-group">
                            <label for="drug_dosage">${dosage}:</label>
                            <select id="drug_dosage" name="drug_dosage" class="form-control" required>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="exp_date">${endActs}:</label>
                            <input placeholder="${date}" id="exp_date" type="text" class="form-control" data-type="date" name="exp_date" required>
                        </div>
                        <div class="form-group">
                            <label for="doc_comment">${comment}</label>
                            <textarea id="doc_comment" maxlength="400" class="form-control" name="doc_comment" placeholder="${comment}" required></textarea>
                        </div>
                    </div>
                        <div class="modal-footer">
                            <input class="btn btn-block btn-primary btn-default" type="submit" value="${send}">
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="modal fade" id="denied-request" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" align="center">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть"/>
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                    </div>
                    <form id="denied_request">
                        <input type="hidden" name="command" value="ANSWER_FOR_REQUEST">
                        <input type="hidden" name="status" value="DENIED">
                        <input id="req_id" type="hidden" name="request_id" >
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="resp_comment">${comment}</label>
                               <textarea id="resp_comment" name="doc_comment" class="form-control" placeholder="${comment}"></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <input class="btn btn-block btn-primary btn-default" type="submit" value="${send}">
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script>
            var parent;
            $('#requests').on('click', '.submit-request', function () {
                if($(this).attr('data-type')=='confirmed') {
                    var dosages = $(this).attr('data-dosages');
                    var array = dosages.split(' ');
                    var select = "";
                    for (var i = 0; i < array.length; i++) {
                        if (array[i] != "") {
                            select += "<option value=\"" + array[i] + "\"" + ">" + array[i] + "</option>";
                        }
                    }
                    $('#exp_date').val($(this).attr('data-exp'));
                    $('#drug_dosage').html(select);
                    $('#re_id').val($(this).attr('data-id'));
                    $('#doc_comment').val("");
                    $('#drug_count').val("");
                }
                else {
                    $('#req_id').val($(this).attr('data-id'));
                    $('#resp_comment').val("");
                }
                parent = $(this).parent();
            });

            $('#denied_request').submit(function () {
                var data = $(this).serialize();
                $.ajax({
                    url:'controller',
                    type:'POST',
                    dataType:'json',
                    data:data,
                    success:function (data) {
                        $('#denied-request').modal('toggle');
                        if(data.result==true){
                            Notify.generate('${answerSended}', '${completed}', 1);
                            parent.html("<span style='color: red'>${requestDenied}!</span>");
                        }
                        else {
                            Notify.generate('${errorSending}', '${error}', 2);
                        }
                    },
                    error:function () {
                        $('#confirm-request').modal('toggle');
                        Notify.generate('${criticalError}', '${error}', 3);
                    }
                });
                return false;
            });

            $('#confirm_request').submit(function () {
                var data = $(this).serialize();
                $.ajax({
                    url:'controller',
                    type:'POST',
                    dataType:'json',
                    data:data,
                    success:function (data) {
                        $('#confirm-request').modal('toggle');
                        if(data.result==true){
                            Notify.generate('${answerSended}', '${completed}', 1);
                            parent.html("<span style='color: green'>${requestConfirmed}!</span>");
                        }
                        else {
                            Notify.generate('${errorSending}', '${error}', 2);
                        }
                    },
                    error:function () {
                        $('#confirm-request').modal('toggle');
                        Notify.generate('${criticalError}', '${error}', 3);
                    }
                });
                return false;
            });
        </script>
    </c:if>
</body>
<c:if test="${user.userRole eq 'DOCTOR'}">
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
</c:if>
<c:if test="${user.userRole eq 'CLIENT' or user.userRole eq 'DOCTOR'}">
    <script src="js/sendRequest.js"></script>
</c:if>
<c:if test="${user.userRole eq 'DOCTOR'}">
    <script src="js/requestsForPrescription.js"></script>
</c:if>
<script src="js/switchLocale.js"></script>
</html>
