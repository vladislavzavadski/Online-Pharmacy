<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 22.07.16
  Time: 18:50
  To change this template use File | Settings | File Templates.
--%>
<%@include file="header.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="footer.jsp"%>
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
    <script src="js/webcam.js"></script>
    <div class="container">
        <!-- Sidebar -->
        <div id="notifies"></div>
        <div class="container" style="background: white; padding-top: 10px">
            <div class="row">
                <div class="col-lg-6">
                     <img id="cabinet_image" src="/controller?command=GET_PROFILE_IMAGE" class="img-responsive" alt="<jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/>" width="300" height="400"/>
                     <!--<h1><jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/></h1>-->
                    <button data-toggle="modal" data-target="#take-photo-modal" class="btn btn-primary"><span class="glyphicon glyphicon-camera" ></span> ${takePhoto}</button>
                </div>
                <div class="col-lg-6">
                    <h1><jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/></h1>
                    <c:if test="${user.phone ne null}">
                        <b>${phoneNumber}: </b>&nbsp

                        <span><jsp:getProperty name="user" property="phone"/></span>
                    </c:if>
                    <br/>
                    <b>e-mail: </b>&nbsp
                    <span><a href="mailto:<jsp:getProperty name="user" property="mail"/>"><jsp:getProperty name="user" property="mail"/></a> </span>
                    <br/>
                    <c:if test="${user.userRole eq 'CLIENT'}">
                        <b>${balance}:</b>
                        <span id="us_balance">${user.balance}</span>
                        <button data-toggle="modal" data-target="#replish-modal" class="btn btn-primary">${refill}</button>
                        <br/>
                    </c:if>
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
    <div class="modal fade" id="take-photo-modal" tabindex="-1" role="dialog" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" align="center">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                    </button>
                </div>
                <div class="modal-body">
                    <div align="center" class="center-block" id="my_camera" style="width:320px; height:240px;"></div>
                </div>
                <div class="modal-footer">
                    <a href="javascript:void(submit_to_server())" class="btn btn-block btn-primary btn-default"><span class="glyphicon glyphicon-camera"></span> ${takePhoto}</a>
                </div>
            </div>
        </div>
    </div>
    <script>
        $('#take-photo-modal').on('shown.bs.modal', function() {
            Webcam.attach('#my_camera');
        });
        $('#take-photo-modal').on('hidden.bs.modal', function () {
            Webcam.reset();
        });

        function submit_to_server(){
            Webcam.snap(function(data_uri){
                Webcam.upload( data_uri, '/controller?command=UPLOAD_PROFILE_IMAGE', function(code, text) {
                    $('#take-photo-modal').modal('toggle');
                    $('#cabinet_image').attr('src', '/controller?command=GET_PROFILE_IMAGE'+'&time='+new Date());

                } );
            });
        }
    </script>
    <c:if test="${user.userRole eq 'CLIENT'}">
    <div class="modal fade" id="replish-modal" tabindex="-1" role="dialog"  aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" align="center">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                    </button>
                </div>
                <form id="replenish-balance" action="/controller" method="post">
                    <input name="command" type="hidden" value="REPLISH_BALANCE">
                    <div class="modal-body">
                        <div id="div-login-msg">
                            <div id="icon-login-msg" class="glyphicon glyphicon-chevron-right"></div>
                            <span id="text-login-msg">${entCard}</span>
                        </div>
                        <div class="form-group">
                            <label for="replenish-sum">${sum} $: </label>
                            <input id="replenish-sum" class="form-control" max="${999.99-user.balance}" type="number" min="1" step="0.1" placeholder="${sum}" name="payment" required>
                        </div>
                        <div class="form-group">
                            <label for="replenish-card">${cardNumber}:</label>
                            <input id="replenish-card" class="form-control" type="text" placeholder="${cardNumber}" name="card_number" required>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <div>
                            <button id="submitButton" type="submit" class="btn btn-primary btn-lg btn-block">${refill}</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
        <script>
            $('#replenish-balance').submit(function () {
                var data  = $(this).serialize();
                var currentBalance = parseFloat($('#us_balance').html());
                var addSum = parseFloat($('#replenish-sum').html());
                $.ajax({
                    url:'controller',
                    type:'POST',
                    dataType:'json',
                    data:data,
                    success:function (data) {
                        $('#replish-modal').modal('toggle');
                        $(this).trigger('reset');
                        if(data.result==true){
                            Notify.generate('${balanceRefilled}', '${completed}', 1);
                            $('#us_balance').html(currentBalance+addSum+"");
                        }
                    }
                });
                return false;
            });

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
</body>
</html>
