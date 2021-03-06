<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@include file="header.jsp"%>
<%@include file="footer.jsp"%>
<jsp:useBean id="orderList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>${myOrders}</title>
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
                position:absolute;
                width:400px;
                height:auto;
                top:100px;
                right:20px;
                z-index: 1;
            }
        </style>
        <script>
            var loadUrl = "/controller?command=GET_ALL_ORDERS";
            var status = "&or_status=";
            var currentStatus = "";
            var dateFrom = "&date_from=";
            var dateTo = "&date_to=";
            var drugName = "&drug_name=";
            var currentPage=2;

            $(document).ready(function(){
                var date_input=$('input[class=date]'); //our date input has the name "date"
                var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
                date_input.datepicker({
                    format: 'yyyy-mm-dd',
                    container: container,
                    todayHighlight: true,
                    autoclose: true,
                    disableEntry: true,
                }).on('changeDate', function () {
                    if($(this).attr("id")=='from_date'){
                        if($("#to_date").val()!=""){
                            if(new Date($(this).val())>new Date($('#to_date').val())){
                                $(this).val("");
                            }
                        }
                    }
                    else {
                        if($("#from_date").val()!=""){
                            if(new Date($(this).val())<new Date($("#from_date").val())){
                                $(this).val("");
                            }
                        }
                    }
                })
            });

        </script>
    </head>
    <body>
        <div id="wrapper">
        <div class="container content">
            <div id="notifies"></div>
            <!-- Sidebar -->
            <div id="sidebar-wrapper">
                <ul class="sidebar-nav">
                    <li class="sidebar-brand">
                        <a href="#">
                        ${orderStatus}:
                        </a>
                    </li>
                    <li>
                        <a class="status" data-status="ORDERED" href="#">${statusOrdered}</a>
                    </li>
                    <li>
                        <a class="status" data-status="CANCELED" href="#">${statusCanceled}</a>
                    </li>
                    <li>
                        <a class="status" data-status="PAID" href="#">${statusPaid}</a>
                    </li>
                    <li>
                        <a class="status" data-status="COMPLETED" href="#">${statusCompleted}</a>
                    </li>
                    <li>
                        <a id="all_orders" href="#">${allOrders}</a>
                    </li>
                </ul>
            </div>

            <h1 class="display_1">${myOrders}</h1>
            <form>
                <label for="drug_name">${drugName}:</label>
                <input id="drug_name" type="text" name="drug_name">
                <nobr>
                    <label for="from_date">${sendedFrom}:</label>
                    <input class="date" id="from_date" type="text" name="order_from">
                    <label for="to_date">${sendedBefore}:</label>
                    <input class="date" id="to_date" type="text" name="order_to">
                    <button id="search_by_date" class="btn btn-primary btn-primary">${search}</button>
                </nobr>
            </form>
            <br/>
            <c:if test="${user.userRole eq 'PHARMACIST'}">
                <form id="search_by_id">
                    <input type="hidden" name="command" value="GET_ORDER_BY_ID">
                    <label for="order_number">${orderNumber}:</label>
                    <input type="number" min="0" step="1" max="2147483647" name="order_id" id="order_number" required>
                    <button class="btn btn-primary btn-primary">${search}</button>
                </form>
            </c:if>

            <div class="container" style="background:white">
                <div id="orders" class="row">
                    <jsp:include page="/order"/>
                </div>
            </div>
        </div>
            <script>
                $(".status").click(function () {
                    currentStatus = $(this).attr("data-status");
                    var url = loadUrl+status+currentStatus;
                    var name;
                    url+=dateFrom+$("#from_date").val();
                    url+=dateTo+$("#to_date").val();
                    $("#drug_name").val(function (index, value) {
                        name = value.replace(" ", "%20");
                        return value;
                    });
                    url+=drugName+name;
                    url+="&page=";
                    paginationUrl=url;

                    $.get(url+1, function (data) {
                        $("#orders").html(data);
                    });

                    var pushedPage = {foo:"page"};
                    window.history.pushState(pushedPage, "page", url+1+"&overload=true");
                    thisPageNum = 2;
                });

                $("#search_by_date").click(function () {
                    var url=loadUrl+status+currentStatus;
                    var name;
                    url+=dateFrom+$("#from_date").val();
                    url+=dateTo+$("#to_date").val();
                    $("#drug_name").val(function (index, value) {
                        name = value.replace(" ", "%20");
                        return value;
                    });
                    url+=drugName+name;
                    url+="&page=";
                    paginationUrl=url;
                    $.get(url+1, function (data) {
                        $("#orders").html(data);
                    });
                    thisPageNum = 2;
                    var pushedPage = {foo:"page"};

                    window.history.pushState(pushedPage, "page", url+1+"&overload=true");
                    return false;
                });

                $("#all_orders").click(function () {
                    currentStatus = "";
                    $("#from_date").val("");
                    $("#to_date").val("");
                    $("#drug_name").val("");
                    var url=loadUrl+status+currentStatus;
                    url+="&page=";
                    paginationUrl=url;

                    $.get(url+1, function (data) {
                        $("#orders").html(data);
                    })
                    var pushedPage = {foo:"page"};

                    window.history.pushState(pushedPage, "page", url+1+"&overload=true");
                    thisPageNum = 2;
                });

                var thisPageNum = 2;
                var paginationUrl = "/controller?command=${param.command}&or_status=${param.or_status}&date_from=${param.date_from}&date_to=${param.date_to}&drug_name=${param.drug_name}&page=";
                <c:if test="${param.command eq 'GET_ORDER_BY_ID'}">
                    paginationUrl=null;
                </c:if>
                var thisWork = true;
                function downloadContent(){
                    if(thisWork && paginationUrl!=null) {
                        thisWork = false;
                        $.get(paginationUrl + thisPageNum, function (data) {
                            $("#LoadedContent").html($("#LoadedContent").html() + " " + data);
                            thisPageNum = thisPageNum + 1;
                            thisWork = true;
                        });
                    }
                }
                $(document).ready(function(){
                    var scrH = $(window).height();
                    var scrHP = $("#drugs").height();
                    $(window).scroll(function(){
                        var scro = $(this).scrollTop();
                        var scrHP = $("#drugs").height();
                        var scrH2 = 0;
                        scrH2 = scrH + scro;
                        var leftH = scrHP - scrH2;

                        if(leftH < 200){
                            downloadContent();
                        }
                    });
                });
                var tmpHtml;
                $("#orders").on('click', '.cancel_order', function () {
                    var buttons = $(this).parent();
                    var orderId = $(this).attr('data-order');
                    $.ajax({
                        url: 'controller',
                        dataType:'json',
                        type: 'POST',
                        data: {command:'CANCEL_ORDER', order_id:orderId},
                        success:function (data) {
                            if(data.result){
                                tmpHtml  = buttons.html();
                                buttons.html("<span style='color:green'>${orderCanceled}...</span>" +
                                        "<a href='#' class='reestablish' data-order='"+orderId+"'>${reestablish}</a>");
                            }
                            else {
                                Notify.generate('${orderNotFound}', '${error}', 3);
                            }
                        }
                    });

                    return false;
                });
                $("#orders").on('click', '.reestablish', function () {
                    var buttons = $(this).parent();
                    var orderId = $(this).attr('data-order');
                    $.ajax({
                       url: 'controller',
                       dataType: 'json',
                       type: 'POST',
                       data:{command: 'REESTABLISH_ORDER', order_id:orderId},
                       success: function (data) {
                           if(data.result==true){
                               buttons.html(tmpHtml);
                           }
                       }
                    });
                    return false;
                });
                var currentParent;

                $("#orders").on('click', '.pay_order', function () {
                    currentParent = $(this).parent();
                    var parent = $(this).attr("data-sum");
                    $('#price').html(parent);
                    $('#buy_drug').attr("data-order", $(this).attr("data-order"));
                });

            </script>
            <div class="modal fade" id="confirm-pay" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">

                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel">${payment}</h4>
                        </div>

                        <div class="modal-body">
                            <p>${surePay} $<span id="price"></span></p>
                            <p class="debug-url"></p>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">${cancel}</button>
                            <button id="buy_drug" class="btn btn-danger btn-ok" data-dismiss="modal" data-order="">${payment}</button>
                        </div>
                    </div>
                </div>
            </div>
            <script>
                $('#buy_drug').click(function () {
                    var orderId = $(this).attr('data-order');
                    $.ajax({
                        url: 'controller',
                        dataType: 'json',
                        type: 'POST',
                        data:{command: 'PAY_ORDER', order_id:orderId},
                        success: function (data) {
                            if(data.result==true){
                                currentParent.html("<span style='color: green'>${statusPaid}!</span>");
                            }
                            else {
                                if(data.message=="Order not found"){
                                    Notify.generate("${orderNotFound}", "${error}", 2);
                                }
                                else if(data.message=="You does not have money to pay for this order"){
                                    Notify.generate("${insFunds}", "${error}", 2);
                                }
                            }
                        }
                    });
                });
            </script>
            <script>
                $("#menu-toggle").click(function(e) {
                    e.preventDefault();
                    $("#wrapper").toggleClass("toggled");
                });
            </script>
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
    <c:if test="${user.userRole eq 'CLIENT' or user.userRole eq 'DOCTOR'}">
        <script src="js/sendRequest.js"></script>
    </c:if>
    <c:if test="${user.userRole eq 'DOCTOR'}">
        <script src="js/requestsForPrescription.js"></script>
    </c:if>
    <script src="js/switchLocale.js"></script>
    <c:if test="${user.userRole eq 'PHARMACIST'}">
        <script src="js/completeOrder.js"></script>
    </c:if>
</html>