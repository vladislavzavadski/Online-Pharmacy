<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="header.jsp"%>
<%@include file="footer.jsp"%>
<jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
<jsp:useBean id="drug" scope="request" class="by.training.online_pharmacy.domain.drug.Drug"/>
<jsp:useBean id="prescriptionExist" scope="request" type="java.lang.Boolean"/>
<c:if test="${user.userRole eq 'CLIENT' and drug.prescriptionEnable and prescriptionExist}">
    <jsp:useBean id="prescription" scope="request" type="by.training.online_pharmacy.domain.prescription.Prescription"/>
</c:if>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>${drug.name}</title>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/simple-sidebar.css" rel="stylesheet">
        <link href="css/sticky-footer-navbar.css" rel="stylesheet">
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
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
        <c:if test="${drug.prescriptionEnable}">
            <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/js/bootstrap-datepicker.min.js"></script>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/css/bootstrap-datepicker3.css"/>
            <script>
                $(document).ready(function(){
                    var date_input=$('input[name="prolong_date"]'); //our date input has the name "date"
                    var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
                    date_input.datepicker({
                        format: 'yyyy-mm-dd',
                        container: container,
                        todayHighlight: true,
                        autoclose: true,
                        disableEntry: true,
                    })
                })
            </script>
        </c:if>
    </head>
    <body>

        <div class="container content">
            <div id="notifies"></div>
            <!-- Sidebar -->
            <div class="container" style="background:white;" align="justify">
                <div class="row">
                    <div class="col-xs-12 col-lg-4">
                        <h1 id="drug_name_desc" class="display_1">${drug.name}
                            <span class="label label-success">$${drug.price}</span>
                        </h1>
                        <div id="img_div">
                        <img id="drug_img" src="/controller?command=GET_DRUG_IMAGE&dr_id=${drug.id}" alt="Drug image" class="img-responsive"/>
                        </div>
                        <p id="drug_description_desc">
                            ${drug.description}
                        </p>
                        <b>
                            ${manufacturer}:
                        </b>&nbsp;
                        <span title="${drug.drugManufacturer.description}" id="drug_man_desc">
                            ${drug.drugManufacturer.name}
                        </span>
                        <br/>
                        <b>
                            ${country}:
                        </b>&nbsp;
                        <span id="drug_country_desc">
                            ${drug.drugManufacturer.country}
                        </span>
                        <br/>
                        <b>
                            ${byPresc}:
                        </b>&nbsp;
                        <span id="drug_prescription_desc">
                    <c:choose>
                        <c:when test="${drug.prescriptionEnable}">
                            ${yes}
                        </c:when>
                        <c:otherwise>
                            ${no}
                        </c:otherwise>
                    </c:choose>
                </span>
                        <br/>
                        <b>
                            ${isInStock}:
                        </b>&nbsp;
                        <span id="in_stock_desc">
                            ${drug.drugsInStock}
                        </span>
                        <br/>
                        <b>
                            ${drugClass}:
                        </b>&nbsp;
                        <span title="${drug.drugClass.description}" id="drug_class_desc">
                            ${drug.drugClass.name}
                        </span>
                        <br/>
                        <b>
                            ${drugType}:
                        </b>&nbsp;
                        <span id="drug_type_desc">
                            ${drug.type}
                        </span>
                        <br/>
                        <b>
                            ${actSubs}:
                        </b>&nbsp;
                        <span id="drug_substance_desc">
                            ${drug.activeSubstance}
                        </span>
                        <br/>
                        <c:if test="${not drug.inStock}">
                            <span style="color: red">${drNotInStock}</span>
                        </c:if>
                    </div>
                    <div class="col-xs-12 col-lg-4" style="padding-top:40px">
                        <c:if test="${((not drug.prescriptionEnable)or(drug.prescriptionEnable and prescriptionExist)) and drug.inStock and user.userRole eq 'CLIENT'}">
                            <button class="btn btn-lg btn-success" style="float:right" data-toggle="modal" data-target="#order-modal" >${orderDrug}</button>
                        </c:if>
                        <c:if test="${drug.prescriptionEnable and user.userRole eq 'CLIENT'}">
                            <button data-toggle="modal" data-target="#request-prescription-modal" class="btn btn-primary btn-primary">${orderPrescription}</button>
                        </c:if>
                        <c:if test="${user.userRole eq 'PHARMACIST'}">
                            <button style="float:right" data-toggle="modal" data-target="#edit-modal" class="btn btn-primary"><span class="glyphicon glyphicon-edit"></span> ${edit}</button>
                            <button style="float:right" data-toggle="modal" data-target="#delete-modal" class="btn btn-danger">${delete}</button>
                        </c:if>
                    </div>
                </div>

            </div>
        </div>
    <c:if test="${user.userRole eq 'CLIENT'}">
        <div class="modal fade" id="order-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
         <div class="modal-dialog">
            <div class="modal-content">
               <div class="modal-header" align="center">
                  <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                  <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                  </button>
                  <img class="img-circle img-responsive" id="img_logo" src="images/order.jpg" alt="Заказ">
               </div>
               <form id="create_order_form">
                   <input type="hidden" name="command" value="CREATE_ORDER">
                   <input type="hidden" name="dr_id" value="${drug.id}">
                  <div class="modal-body">
                     <div id="div-register-msg">
                        <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                        <span id="text-register-msg">${drugOrder}</span>
                     </div>
                     <div class="form_group">    
                        <label for="dosage">${dosage}: </label>
                         <select name="drug_dosage" id="dosage" class="form-control">
                         <c:choose>
                             <c:when test="${drug.prescriptionEnable and prescriptionExist}">
                                 <option value="${prescription.drugDosage}">${prescription.drugDosage}</option>
                             </c:when>
                             <c:otherwise>
                                 <c:forEach items="${drug.dosages}" var="dosage">
                                     <option value="${dosage}">${dosage}</option>
                                 </c:forEach>
                             </c:otherwise>
                         </c:choose>
                        </select>
                     </div>
                     <div class="form_group">
                        <label for="drug_count">${drugCount}: </label>
                        <input id="drug_count" name="drug_count"  class="form-control" type="number" placeholder="${drugCount}" step="1" min="1" max="${prescriptionExist ? (prescription.drugCount<10 ? prescription.drugCount : 10) : (drug.drugsInStock <10 ? drug.drugsInStock : 10)}" required>
                     </div>
                  </div>
                  <div class="modal-footer">
                     <div>
                        <button id="create_order" type="submit" class="btn btn-primary btn-lg btn-block" >${orderDrug}</button>
                     </div>
                  </div>
               </form>
            </div>
         </div>
        </div>
    </c:if>
        <c:if test="${drug.prescriptionEnable and user.userRole eq 'CLIENT'}">
            <div class="modal fade" id="request-prescription-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                        </div>
                        <form id="create_request_form">
                            <input type="hidden" name="command" value="CREATE_REQUEST">
                            <input type="hidden" name="dr_id" value="${drug.id}">
                                <div class="form-group">
                                    <label class="control-label" for="date">
                                        ${prolongTo}:
                                        <span class="asteriskField">
                                        *
                                    </span>
                                    </label>
                                    <input class="form-control input-md" id="date" name="prolong_date" placeholder="yyyy-mm-dd" type="text" required/>
                                    <span id="date_comment"></span>
                                </div>
                                <div class="form-group">
                                    <label class="control-label" for="comment">${problemDescription}</label>
                                    <textarea class="form-control" id="comment" name="client_comment" maxlength="400" placeholder="${comment}" required></textarea>
                                    <span id="comment_comment"></span>
                                </div>
                                <div class="form-group">
                                    <label class="control-label"></label>
                                    <button class="btn btn-primary">${send}</button>
                                </div>
                        </form>
                    </div>
                </div>
            </div>
        </c:if>
        <script>
            $('#create_order_form').submit(function () {
                var data = $(this).serialize();

                var count=parseInt($('#drug_count').val());

                $.ajax({
                    url: 'controller',
                    dataType: 'json',
                    type: 'POST',
                    data: data,
                    success:function (data) {
                        $("#order-modal").modal('toggle');

                        if(data.result){
                            Notify.generate('${orderSuccess}','${completed}', 1);
                            var inStock = parseInt($("#in_stock").html())-count;
                            $("#in_stock").html(inStock);
                            $("#drug_count").attr("max", parseInt($("#drug_count").attr('max'))-count);
                            $("#drug_count").val(1);

                        }
                        else {
                            Notify.generate('${orderError}', '${error}', 2);
                        }

                    }
                });

                return false;
            });
            
            $('#create_request_form').submit(function () {
                var data=$(this).serialize();

                $.ajax({
                    url:'controller',
                    type:'POST',
                    dataType:'json',
                    data:data,
                    success:function (data) {
                        $("#request-prescription-modal").modal('hide');

                        if(data.result){
                            Notify.generate('${requestSuccess}', '${completed}', 1);
                        }else {
                            Notify.generate('${nonProcessedRequest}', '${error}', 2);
                        }

                    }
                });
                return false;
            });
        </script>
        <c:if test="${user.userRole eq 'PHARMACIST'}">
            <jsp:useBean id="drugManufacturers" scope="request" class="java.util.ArrayList"/>
            <jsp:useBean id="drugClasses" scope="request" class="java.util.ArrayList"/>
            <jsp:useBean id="specializations" scope="request" class="java.util.ArrayList"/>
            <div class="modal fade" id="edit-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" align="center">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                            <img class="img-circle img-responsive" id="img_logo" src="images/edit.png" width="150" height="150" alt="${edit}">
                        </div>
                        <form id="update_drug_form">
                            <input type="hidden" name="command" value="UPDATE_DRUG">
                            <input type="hidden" name="dr_id" value="${drug.id}">
                            <div class="modal-body">
                                <div id="div-register-msg">
                                    <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                                    <span id="text-register-msg">${editDrug}</span>
                                </div>
                                <div class="form_group">
                                    <label for="drug_name">${drugName}: </label>
                                    <input type="text" maxlength="20" class="form-control" id="drug_name" value="${drug.name}" name="drug_name" required/>
                                </div>
                                <div class="form_group">
                                    <label for="drug_image">${photo}: </label>
                                    <input class="form-control" type="file" id="drug_image" name="drug_image" accept="image/*"/>
                                    <span id="mes_img"></span>
                                </div>
                                <div class="form_group">
                                    <label for="manufacturer_name">${manufacturer}: </label>
                                    <select id="manufacturer_name" class="form-control" name="dr_manufacturer">
                                        <c:forEach items="${drugManufacturers}" var="drugMan">
                                            <option value="${drugMan.name},${drugMan.country}" <c:if test="${drugMan.name eq drug.drugManufacturer.name and drugMan.country eq drug.drugManufacturer.country}">selected</c:if>>${drugMan.name}(${drugMan.country})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label for="drug_class">${drugClass}:</label>
                                    <select id="drug_class" class="form-control" name="dr_class">
                                        <c:forEach items="${drugClasses}" var="drugClass">
                                            <option value="${drugClass.name}" <c:if test="${drug.drugClass.name eq drugClass.name}">selected</c:if>>${drugClass.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label for="prescription_enable">${prescriptionEnable}:</label>
                                    <select id="prescription_enable" class="form-control" name="pr_status">
                                        <option value="true" <c:if test="${drug.prescriptionEnable}">selected</c:if>>${yes}</option>
                                        <option value="false" <c:if test="${ not drug.prescriptionEnable}">selected</c:if>>${no}</option>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label for="in_stock">${inStock}:</label>
                                    <input id="in_stock" type="number" max="1000" class="form-control" step="1" min="0" name="drugs_in_stock" value="${drug.drugsInStock}" required>
                                </div>
                                <div class="form_group">
                                    <label for="drug_type">${drugType}</label>
                                    <select id="drug_type" class="form-control" name="drug_type">
                                        <option value="tablet" <c:if test="${drug.type eq 'TABLET'}">selected</c:if>>${tablet}</option>
                                        <option value="capsule" <c:if test="${drug.type eq 'CAPSULE'}">selected</c:if>>${capsule}</option>
                                        <option value="salve" <c:if test="${drug.type eq 'SALVE'}">selected</c:if>>${salve}</option>
                                        <option value="syrop" <c:if test="${drug.type eq 'SYROP'}">selected</c:if>>${syrop}</option>
                                        <option value="injection" <c:if test="${drug.type eq 'INJECTION'}">selected</c:if>>${injection}</option>
                                        <option value="candle" <c:if test="${drug.type eq 'CANDLE'}">selected</c:if>>${candle}</option>
                                        <option value="drops" <c:if test="${drug.type eq 'DROPS'}">selected</c:if>>${drops}</option>
                                        <option value="unknown" <c:if test="${drug.type eq 'UNKNOWN'}">selected</c:if>>${unknown}</option>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label id="drug_type">${specialization}</label>
                                    <select id="drug_type" class="form-control" name="specialization">
                                        <c:forEach items="${specializations}" var="spec">
                                            <option value="${spec.specialization}" <c:if test="${drug.doctorSpecialization eq spec.specialization}">selected</c:if>>${spec.specialization}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_group">
                                    <label for="drug_active_substance">${actSubs}: </label>
                                    <input class="form-control" maxlength="45" type="text" id="drug_active_substance" name="active_substance" value="${drug.activeSubstance}" required/>
                                </div>
                                <div class="form_group">
                                    <label for="drug_price">${drugPrice}: </label>
                                    <input class="form-control" max="999.99" type="number" min="0" id="drug_price" step="0.1" name="drug_price" value="${drug.price}" required/>
                                </div>
                                <div class="form_group">
                                    <label >${dosage}: </label>
                                    <span id="dosage_message" style="color: red;"></span>
                                    <div class="row">
                                        <c:forEach var="i" begin="50" end="1000" step="50">
                                            <div class="col-lg-3">
                                                <div class="checkbox">
                                                    <label><input type="checkbox" value="${i}" name="drug_dosage"<c:if test="${drug.dosages.contains(i)}">checked</c:if>>${i}</label>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div class="form_group">
                                    <label for="drug_price">${description}: </label>
                                    <textarea class="form-control" maxlength="300" id="description" name="drug_description" placeholder="${comment}">${drug.description}</textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <div>
                                    <button type="submit" id="save_edit" class="btn btn-primary btn-lg btn-block">${save}</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                var checkboxes = $("input[type='checkbox']");
                checkboxes.click(function () {
                    $('#save_edit').prop('disabled', !checkboxes.is(':checked'));

                    if(!checkboxes.is(':checked')){
                        $('#dosage_message').html('${checkOneDosage}!');
                    }
                    else {
                        $('#dosage_message').html('');
                    }
                });

                $('#update_drug_form').submit(function () {
                    var data = new FormData($(this)[0]);

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
                        async: false,
                        cache: false,
                        contentType:false,
                        processData: false,
                        data:data,
                        success: function (data) {
                            if(data.result==true){
                                Notify.generate("${changesSaved}", '${completed}!', 1);
                                $('#edit-modal').modal('toggle');
                                $('#drug_img').attr('src', '/controller?command=GET_DRUG_IMAGE&dr_id=${drug.id}&atr='+new Date().getTime());
                                $('#drug_name_desc').html($('#drug_name').val()+"<span class=\"label label-success\">$"+$('#drug_price').val()+"</span>");
                                $('#drug_description_desc').html($('#description').val());

                                var strings = $('#manufacturer_name').val().toString().split(",");
                                $('#drug_man_desc').html(strings[0]);
                                $('#drug_country_desc').html(strings[1]);

                                if($('#prescription_enable').val()=="true"){
                                    $('#drug_prescription_desc').html("${yes}");
                                }
                                else {
                                    $('#drug_prescription_desc').html("${no}");
                                }

                                $('#in_stock_desc').html($('#in_stock').val());
                                $('#drug_substance_desc').html($('#drug_active_substance').val());
                                $('#drug_type_desc').html($( "#drug_type option:selected" ).text());
                            }
                            else {
                                Notify.generate('${serverResponse}'+data.message, '${error}!', 3);
                            }
                        }
                    });
                    return false;
                });
            </script>
            <div class="modal fade" id="delete-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">

                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel">${deleting}</h4>
                        </div>

                        <div class="modal-body">
                            <p>${sureDelete}?</p>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">${cancel}</button>
                            <a  class="btn btn-danger btn-ok" href="/controller?command=DELETE_DRUG&dr_id=${drug.id}">${delete}</a>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

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