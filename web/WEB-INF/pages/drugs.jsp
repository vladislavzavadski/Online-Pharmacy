<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@include file="header.jsp"%>
<%@include file="footer.jsp"%>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta contentType="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>${drugs}</title>
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
    <jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
    <jsp:useBean id="drugList" scope="request" class="java.util.ArrayList"/>
    <jsp:useBean id="drugClasses" scope="request" class="java.util.ArrayList"/>
    <jsp:useBean id="specializations" scope="request" class="java.util.ArrayList"/>
        <div id="wrapper">
        <div class="container content">
            <div id="notifies"></div>
            <!-- Sidebar -->
            <div id="sidebar-wrapper">
                <ul class="sidebar-nav">
                    <li class="sidebar-brand">
                        ${drugClass}:
                    </li>
                    <div id="drug_classes">
                        <c:forEach items="${drugClasses}" var="drugClass">
                            <li>
                                <a class="Class" href="/controller?command=EXTENDED_DRUG_SEARCH&overload=false&dr_class=${drugClass.name}&page=1" title="${drugClass.description}">${drugClass.name}</a>
                            </li>
                        </c:forEach>
                    </div>
                    <li>
                        <a id="all_dr" href="/controller?command=EXTENDED_DRUG_SEARCH&overload=false&page=1">${allClasses}</a>
                    </li>
                </ul>
            </div>
            <script>
                var thisPageNum = 2;
                $('#drug_classes').on('click', '.Class',function () {
                    var toLoad = $(this).attr("href");

                    $.get(toLoad, function (data) {
                        $("#drugs").html(data);
                    });

                    load = true;
                    thisPageNum = 2;
                    loadUrl="/controller?command=EXTENDED_DRUG_SEARCH&dr_class="+$(this).html()+"&page=";
                    var pageState = {foo:"bar"};
                    window.history.pushState(pageState, "page2", loadUrl+1+"&overload=true");
                    return false;
                });

                $("#all_dr").click(function () {
                    var toLoad = $(this).attr("href");
                    $.get(toLoad, function (data) {
                        $("#drugs").html(data);
                    });
                    thisPageNum = 2;
                    load = true;
                    loadUrl="/controller?command=EXTENDED_DRUG_SEARCH&overload=false&page=";
                    var pushedPage = {foo:"bar"};
                    window.history.pushState(pushedPage, "page", loadUrl+1+"&overload=true");
                    return false;
                });

                $('#search_button').click(function () {
                    var query;
                    $('#query_string').val(function (index, value) {
                        query = value.replace(" ", "%20");
                        return value;
                    });
                    thisPageNum=2;
                    load = true;
                    var toLoad = "/controller?command=SEARCH_DRUGS&query="+query+"&page=";
                    $.get(toLoad+1, function (data) {
                        $('#drugs').html(data);
                    });
                    loadUrl = toLoad;
                    var pageState = {foo:"bar"};
                    window.history.pushState(pageState, "page2", loadUrl+1+"&overload=true");
                    return false;
                });
            </script>
            <h1 class="display_1">${drugs}</h1>
            <c:if test="${user.userRole eq 'PHARMACIST'}">
                <div class="btn-group" style="padding-top:20px;" role="group">
                    <button data-toggle="modal" data-target="#new-drug-modal" class="btn btn-primary btn-primary">${addDrug}</button>
                    <button data-toggle="modal" data-target="#new-drug-class-modal" class="btn btn-primary btn-primary">${addDrugClass}</button>
                    <button data-toggle="modal" data-target="#new-drug-manufacturer-modal" class="btn btn-primary btn-primary">${addManufacturer}</button>
                </div>
            </c:if>
            <div id="drugs" class="row" align="justify" style="background:white;">
                    <jsp:include page="/drug"/>
                <c:choose>
                    <c:when test="${drugList.size() ne 6}">
                        <div id="stop" data-stop="${drugList.size()<6}"></div>
                    </c:when>
                    <c:otherwise>
                        <div id="LoadedContent" data-stop="${drugList.size()<6}"></div>
                    </c:otherwise>
                </c:choose>

                <script>
                    var load = true;
                    var thisWork = true;
                    var loadUrl = "/controller?query=${param.query}&command=${param.command}&name=${param.name}&active_substance=${param.active_substance}&max_price=${param.max_price}&dr_class=${param.dr_class}&dr_manufacture=${param.dr_manufacture}&only_in_stock=${param.only_in_stock}&only_free=${param.only_free}&overload=false&page="
                    function downloadContent(){
                        if(thisWork) {
                            thisWork = false;
                                $.get(loadUrl + thisPageNum, function (data) {
                                    $("#LoadedContent").html($("#LoadedContent").html() + " " + data);
                                    thisWork = true;
                                    thisPageNum = thisPageNum + 1;
                                    if(data)
                                        console.log("empty");
                                    else
                                        console.log("not empty");
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

                    $('#ext_search_form').submit(function () {
                        var data = $(this).serialize();
                        var url = "/controller?"+data;
                        url+="&page=";
                        $.get(url+1, function (data) {
                            $("#drugs").html(data);
                        });
                        thisPageNum=2;
                        loadUrl=url;
                        $("#toggler_call").attr("checked", false);
                        var pageState = {foo:"bar"};
                        window.history.pushState(pageState, "page2", url+1+"&overload=true");

                        return false;
                    });
                </script>

                <!--/span-->
            </div>
            <!--/row-->

        </div>
            <c:if test="${user.userRole eq 'PHARMACIST'}">
                <jsp:useBean id="drugManufacturers" scope="request" class="java.util.ArrayList"/>
            <div class="modal fade" id="new-drug-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                            <img class="img-circle img-responsive" id="img_logo" src="images/create.png" width="250" height="200" alt="Новое лекарство">
                        </div>
                        <form id="create-drug-form" action="/controller" method="post" enctype='multipart/form-data' accept-charset="utf-8">
                            <input type="hidden" name="command" value="CREATE_DRUG">
                            <div class="modal-body">
                                <div id="div-register-msg">
                                    <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                                    <span id="text-register-msg">${newDrug}</span>
                                </div>
                                <div class="form_group">
                                    <label for="drug_name11">${drugName}: </label>
                                    <input type="text" class="form-control" id="drug_name11" maxlength="20" name="drug_name" required/>
                                </div>
                                <div class="form_group">
                                    <label for="drug_image">${photo}: </label>
                                    <input class="form-control" type="file" id="drug_image" name="drug_image" accept="image/*"/>
                                    <span id="mes_img"></span>
                                </div>
                                <div class="form_group">
                                    <label for="manufacturer_name">${manufacturer}: </label>
                                    <select id="manufacturer_name" class="form-control" name="dr_manufacturer">
                                        <c:forEach items="${drugManufacturers}" var="man">
                                            <option value="${man.name},${man.country}">${man.name}(${man.country})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label id="drug_class">${drugClass}:</label>
                                    <select id="drug_class_select" class="form-control" name="dr_class">
                                        <c:forEach items="${drugClasses}" var="cls">
                                            <option value="${cls.name}">${cls.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label id="prescription_enable">${prescriptionEnable}:</label>
                                    <select id="prescription_enable" class="form-control" name="pr_status">
                                        <option value="true">${yes}</option>
                                        <option value="false">${no}</option>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label id="in_stock">${inStock}:</label>
                                    <input type="number" class="form-control" step="1" min="0" max="1000" name="drugs_in_stock" required>
                                </div>
                                <div class="form_group">
                                    <label for="drug_type">${drugType}</label>
                                    <select id="drug_type" class="form-control" name="drug_type">
                                        <option value="tablet">${tablet}</option>
                                        <option value="capsule">${capsule}</option>
                                        <option value="salve">${salve}</option>
                                        <option value="syrop">${syrop}</option>
                                        <option value="injection">${injection}</option>
                                        <option value="candle">${candle}</option>
                                        <option value="drops">${drops}</option>
                                        <option value="unknown" selected>${unknown}</option>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label id="drug_type">${specialization}</label>
                                    <select id="drug_type" class="form-control" name="specialization">
                                        <c:forEach items="${specializations}" var="spec">
                                            <option value="${spec.specialization}">${spec.specialization}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label for="drug_active_substance">${actSubs}: </label>
                                    <input class="form-control" maxlength="45"  type="text" id="drug_active_substance" name="active_substance" required/>
                                </div>
                                <div class="form_group">
                                    <label for="drug_price">Цена: </label>
                                    <input class="form-control" type="number" max="999.99" min="0" id="drug_price" step="0.1" name="drug_price" required/>
                                </div>
                                <div class="form_group">
                                    <label>${dosage}: </label>
                                    <span id="dos_message"></span>
                                    <div class="row">
                                        <c:forEach var="i" begin="50" end="1000" step="50">
                                            <div class="col-lg-3">
                                                <div class="checkbox">
                                                    <label><input type="checkbox" value="${i}" name="drug_dosage">${i}</label>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div class="form_group">
                                    <label for="drug_price">${description}: </label>
                                    <textarea class="form-control" maxlength="300" id="description" name="drug_description" placeholder="${comment}"></textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <div>
                                    <button id="create_drug" type="submit" class="btn btn-primary btn-lg btn-block">${save}</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                $("#create-drug-form").submit(function (event) {
                    event.preventDefault();
                    var formData = new FormData($(this)[0]);

                    if($('#drug_image').val()!=""&&$('#drug_image')[0].files[0].type!="image/jpeg"){
                        $('#mes_img').html("${notImage}");
                        return false;
                    }

                    if($('#drug_image').val()!=""&&$('#drug_image')[0].files[0].size>1.342e+9){
                        $('#mes_img').html("${tooLarge}");
                        return false;
                    }

                    $.ajax({
                        url:'controller',
                        type:'POST',
                        data:formData,
                        async: false,
                        cache: false,
                        contentType:false,
                        processData: false,
                        success:function (data) {
                            if(data.result==true) {
                                $('#create-drug-form').trigger('reset');
                                $('#new-drug-modal').modal('toggle');
                                $('#dos_message').html("");
                                Notify.generate("${newDrugCreated}", "${completed}!", 1);
                            }
                            else {
                                if(data.message.contains("dosages")){
                                    $('#dos_message').html("<span style='color: red'>${checkOneDosage}</span>");
                                }
                                else {
                                    $('#create-drug-form').trigger('reset');
                                    $('#new-drug-modal').modal('toggle');
                                    $('#dos_message').html("");
                                    Notify.generate("${serverResponse}:"+data.message, "${error}", 3);
                                }
                            }
                        }
                    });
                    return false;
                });
            </script>
            <div class="modal fade" id="new-drug-class-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                            <img class="img-circle img-responsive" id="img_logo" src="images/create.png" width="250" height="200" alt="${newDrugClass}"/>
                        </div>
                        <form id="create_class_form">
                            <input type="hidden" name="command" value="CREATE_CLASS">
                            <div class="modal-body">
                                <div id="div-register-msg">
                                    <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                                    <span id="text-register-msg">${newDrugClass}</span>
                                </div>
                                <div class="form_group">
                                    <label for="drug_class_name">${className}: </label>
                                    <input type="text" class="form-control" maxlength="30" id="drug_class_name" name="dr_class" required/>
                                    <span id="create_class_message"></span>
                                </div>
                                <div class="form_group">
                                    <label for="drug_price">${classDescription}: </label>
                                    <textarea class="form-control" id="description" name="class_description" placeholder="${comment}" maxlength="300" required></textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <div>
                                    <button id="submit_drug_class" type="submit" class="btn btn-primary btn-lg btn-block" disabled>${save}</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                $('#drug_class_name').blur(function () {
                    var className = $(this).val();
                    var message = $('#create_class_message');
                    var button = $('#submit_drug_class');
                    button.prop('disabled', true);
                    if(className!=""){
                        $.ajax({
                            url:'controller',
                            type:'POST',
                            dataType:'json',
                            data:{command:'CHECK_CLASS', dr_class:className},
                            success:function (data) {
                                if(data.result==true){
                                    if(data.isExist==true) {
                                        message.html("<span style='color: red'>${classExist}</span>");
                                    }
                                    else {
                                        message.html("<span style='color: green'>${classNotExist}</span>");
                                        button.prop('disabled', false);
                                    }
                                }

                            }
                        });
                    }
                });
                $('#create_class_form').submit(function () {
                    var data = $(this).serialize();
                    var className = $('#drug_class_name').val();
                    var classDescription = $('#description').val();
                    $.ajax({
                        url:'controller',
                        type:'POST',
                        dataType:'json',
                        data:data,
                        success:function (data) {
                            $('#create_class_message').html("");
                            $(this).trigger('reset');
                            $("#new-drug-class-modal").modal('toggle');
                            if(data.result==true){
                                Notify.generate("${newClassCreated}",'${completed}', 1);
                                var drugClasses = $('#drug_classes');
                                drugClasses.html("<li>" +
                                        "<a class='Class' href='/controller?command=GET_DRUGS_BY_CLASS&dr_class="+className+"&page=1' title='"+classDescription+"'>"+className+"</a> " +
                                        "</li>"+drugClasses.html());
                                $('#drug_class_select').html("<option value='"+className+"'>"+className+"</option>"+$('#drug_class_select').html());
                                $('#dr_class_select').html("<option value='"+className+"'>"+className+"</option>"+$('#dr_class_select').html());
                            }
                            else {
                                Notify.generate("${serverResponse}:"+data.message,'${error}', 2);

                            }
                        }
                    });
                    return false;
                });
            </script>
            <div class="modal fade" id="new-drug-manufacturer-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                            <img class="img-circle img-responsive" id="img_logo" src="images/create.png" width="250" height="200" alt="${newManufacturer}">
                        </div>
                        <form id="create_manufacturer">
                            <input type="hidden" name="command" value="CREATE_MANUFACTURER">
                            <div class="modal-body">
                                <div id="div-register-msg">
                                    <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                                    <span id="text-register-msg">${newManufacturer}</span>
                                </div>
                                <div class="form_group">
                                    <label for="drug_manufacturer_name">${manName}: </label>
                                    <input max="50" type="text" class="form-control new_man" maxlength="50" id="drug_manufacturer_name" name="man_name" required/>
                                </div>
                                <div class="form_group">
                                    <label for="drug_manufacturer_country">${manCountry}: </label>
                                    <input max="50" type="text" class="form-control new_man" maxlength="50" id="drug_manufacturer_country" name="man_country" required/>
                                </div>
                                <span id="create_man_message"></span>
                                <div class="form_group">
                                    <label for="drug_price">${description}: </label>
                                    <textarea maxlength="300" class="form-control" id="man_description" name="man_description" placeholder="${comment}" required></textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <div>
                                    <button type="submit" id="submit_man" class="btn btn-primary btn-lg btn-block" disabled>${save}</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                $('.new_man').blur(function () {
                    var manName = $('#drug_manufacturer_name').val();
                    var manCountry = $('#drug_manufacturer_country').val();
                    $('#submit_man').prop('disabled', true);
                    if(manName==""||manCountry==""){
                        return false;
                    }
                    $.ajax({
                        url:'controller',
                        type:'POST',
                        dataType:'json',
                        data:{command:'CHECK_MANUFACTURER', man_name:manName, man_country:manCountry},
                        success: function (data) {

                            if(data.result==true){
                                if(data.isExist==true){
                                    $('#create_man_message').html("<span style='color:red'>${manufacturerExist}</span>");
                                }
                                else {
                                    $('#create_man_message').html("<span style='color:green'>${manufacturerNotExist}</span>");
                                    $('#submit_man').prop('disabled', false);
                                }
                            }
                        }
                    });
                });
                $('#create_manufacturer').submit(function () {
                    var manName = $('#drug_manufacturer_name').val();
                    var manCountry = $('#drug_manufacturer_country').val();
                    var description = $('#man_description').val();
                    var data = $(this).serialize();
                    $.ajax({
                        url:'controller',
                        type:'POST',
                        dataType:'json',
                        data:data,
                        success:function (data) {
                            $(this).trigger('reset');
                            $('#new-drug-manufacturer-modal').modal('toggle');
                            $('#create_man_message').html("");
                            if(data.result==true){
                                Notify.generate("${manufacturerCreated}", "${completed}!", 1);
                                $('#drug_manufacturer_s').html("<option value='"+manName+","+manCountry+"'>"+manName+"("+manCountry+")"+"</option>>"+$('#drug_manufacturer_s').html());
                            }
                            else {
                                Notify.generate("${serverResponse}: "+data.message, '${error}', 3);
                            }
                        }
                    });
                    return false;
                });
            </script>
        </c:if>
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
</html>