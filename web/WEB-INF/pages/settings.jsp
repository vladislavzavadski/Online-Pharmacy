<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="header.jsp"%>
<c:if test="${sessionScope.user eq null}">
    <c:redirect url="http://localhost:8080"/>
</c:if>
<jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
<fmt:message bundle="${loc}" key="locale.personal_info" var="personalInfo"/>
<fmt:message bundle="${loc}" key="locale.security" var="security"/>
<fmt:message bundle="${loc}" key="locale.old_password" var="oldPassword"/>
<fmt:message bundle="${loc}" key="locale.new_password" var="newPassword"/>
<fmt:message bundle="${loc}" key="locale.confirmed_password" var="confirmPassword"/>
<fmt:message bundle="${loc}" key="locale.reestablish_password" var="reestablishPassword"/>
<fmt:message bundle="${loc}" key="locale.question" var="question"/>
<fmt:message bundle="${loc}" key="locale.secret_word_saved" var="secretSaved"/>
<fmt:message bundle="${loc}" key="locale.information_success_update" var="infoSuccessUpdate"/>
<fmt:message bundle="${loc}" key="locale.error_information_update" var="errorInfoUpdate"/>
<fmt:message bundle="${loc}" key="locale.personal_info_saved" var="personalInfoSaved"/>
<fmt:message bundle="${loc}" key="locale.all_fields_must" var="allFieldsMust"/>
<fmt:message bundle="${loc}" key="locale.pass_not_same" var="passNotSame"/>
<fmt:message bundle="${loc}" key="locale.password_saved" var="passwordSaved"/>
<fmt:message bundle="${loc}" key="locale.check_password" var="checkPassord"/>
<fmt:message bundle="${loc}" key="locale.contacts_saved" var="contactsSaved"/>
<fmt:message bundle="${loc}" key="locale.new_photo_saved" var="newPhotoSaved"/>
<fmt:message bundle="${loc}" key="locale.photo_not_selected" var="photoNotSelected"/>
<fmt:message bundle="${loc}" key="locale.stuff_description" var="stuffDescription"/>

<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>${mySettings}</title>
        <!-- Bootstrap -->
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/simple-sidebar.css" rel="stylesheet">
        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
        <script src="js/bootstrap.js"></script>
        <style>
            #notifies {
                position:fixed;
                width:auto;
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
            <!-- Sidebar -->
            <h1 class="display_1">${mySettings}</h1>
            <div class="container" style="background:white">
                <form id="profile_info_form" class="form-horizontal">
                    <fieldset>
                        <legend>${personalInfo}</legend>
                        <input type="hidden" name="command" value="UPDATE_PERSONAL_INFORMATION">
                        <span id="personal_inf"></span>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="first_name">${firstName}:</label>
                            <div class="col-md-4">
                                <input id="first_name"  name="first_name" maxlength="30" type="text" value="${user.firstName}" class="form-control input-md span3" required="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="second_name">${lastName}:</label>
                            <div class="col-md-4">
                                <input id="second_name" maxlength="30"  name="second_name" type="text" value="${user.secondName}" class="form-control input-md span3" required="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="gender_select">${gender}:</label>
                            <div class="col-md-4">
                                <select name="gender" id="gender_select" class="selectpicker form-control input-md span3">
                                    <option value="MALE" ${user.gender eq 'MALE'?'selected':''}>${male}</option>
                                    <option value="FEMALE" ${user.gender eq 'FEMALE'?'selected':''}>${female}</option>
                                    <option value="UNKNOWN" ${user.gender eq 'UNKNOWN'?'selected':''}>${unknown}</option>
                                </select>
                            </div>

                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"></label>
                            <div class="col-md-4">
                                <button id="save_personal_info" class="btn btn-primary">${save}</button>
                            </div>
                        </div>

                    </fieldset>
                </form>
                <c:if test="${user.registrationType eq 'NATIVE'}">
                <div class="form-horizontal">
                    <fieldset>
                        <legend>${security}</legend>
                        <span id="security_inf"></span>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="old_password">${oldPassword}:</label>
                            <div class="col-md-4">
                                <input id="old_password" maxlength="60"  name="old_password" type="password" class="form-control input-md span3" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="new_password">${newPassword}:</label>
                            <div class="col-md-4">
                                <input id="new_password" maxlength="60"  name="new_password" type="password" class="form-control input-md span3" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="confirm_password">${confirmPassword}:</label>
                            <div class="col-md-4">
                                <input id="confirm_password" maxlength="60"  name="confirm_password" type="password" class="form-control input-md span3" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"></label>
                            <div class="col-md-4">
                                <button id="save_password" class="btn btn-primary">${save}</button>
                            </div>
                        </div>

                    </fieldset>
                </div>
                </c:if>
                <form id="cont_form" class="form-horizontal">
                    <input type="hidden" name="command" value="UPDATE_CONTACTS">
                    <fieldset>
                        <legend>${contacts}</legend>
                        <span id="contacts_inf"></span>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="e-mail">E-mail:</label>  
                            <div class="col-md-4">
                                <input id="e-mail" maxlength="45"  name="email" type="email" value="${user.mail}" class="form-control input-md span3" required="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="phone_number">${phoneNumber}:</label>
                            <div class="col-md-4">
                                <input pattern="^\+(?:[0-9]●?){6,14}[0-9]$" id="phone_number" maxlength="15"  name="phone" type="text" value="${user.phone}" class="form-control input-md span3" required="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"></label>
                            <div class="col-md-4">
                                <button id="save_contacts" class="btn btn-primary">${save}</button>
                            </div>
                        </div>

                    </fieldset>
                </form>
               <!-- <form id="image_form" class="form-horizontal" action="/controller" method="post" enctype="multipart/form-data" name="contact">-->
                <div class="form-horizontal">
                <fieldset>
                        <legend>${photo}</legend>
                        <span id="photo_inf"></span>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="profile_image1">${photo}:</label>
                            <div class="col-md-4">
                                <input id="profile_image1"  name="profile_image" type="file" class="form-control input-md span3" accept="image/*" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"></label>
                            <div class="col-md-4">
                                <button type="submit" id="save_image"  class="btn btn-primary">${save}</button>
                                <img id="loading" src="images/Loading_icon.gif" width="150" height="100" style="display: none"/>
                            </div>
                        </div>
                    </fieldset>
                </div>
                <c:if test="${user.registrationType eq 'NATIVE'}">
                    <jsp:useBean id="secretQuestions" scope="request" class="java.util.ArrayList"/>
                    <form class="form-horizontal" id="secret_form">
                        <input type="hidden" name="command" value="CREATE_SECRET">
                        <fieldset>
                            <legend>${reestablishPassword}</legend>
                            <div class="form-group">
                                <label class="col-md-4 control-label" for="questions">${question}:</label>
                                <div class="col-md-4">
                                    <select name="question_id" id="questions" class="selectpicker form-control input-md span3" required>
                                        <c:forEach items="${secretQuestions}" var="question">
                                            <option value="${question.id}">${question.question}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label" for="secret_word">${answer}:</label>
                                <div class="col-md-4">
                                    <input type="text" maxlength="50" class="form-control input-md span3" name="secret_word" id="secret_word" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label"></label>
                                <div class="col-md-4">
                                    <button type="submit" id="save_secret"  class="btn btn-primary">${save}</button>
                                </div>
                            </div>
                        </fieldset>

                    </form>
                    <script>
                        $('#secret_form').submit(function () {
                            var data = $(this).serialize();
                            $.ajax({
                                url:'controller',
                                type:'POST',
                                dataType:'json',
                                data:data,
                                success: function (data) {
                                    if(data.result==true){
                                        Notify.generate('${secretSaved}','${completed}',  1);
                                    }
                                    else {
                                        Notify.generate('${serverResponse}: '+data.message, '${error}', 2);
                                    }

                                }
                            });
                            return false;
                        });
                    </script>

                </c:if>
                <c:if test="${user.userRole eq 'DOCTOR'}">
                    <form class="form-horizontal" id="staff_description">
                        <input type="hidden" name="command" value="UPDATE_DESCRIPTION">
                        <legeng>${stuffDescription}</legeng>
                        <div class="form-group">
                            <label for="specialization" class="col-md-4 control-label">${specialization}</label>
                            <div class="col-md-4">
                                <input type="text" value="${user.userDescription.specialization}" class="form-control input-md span3" name="specialization" id="specialization" placeholder="${specialization}" required maxlength="40">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="description" class="col-md-4 control-label">${description}:</label>
                            <div class="col-md-4">
                                <textarea id="description" name="description" maxlength="300" placeholder="${description}" required>${user.userDescription.description}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"></label>
                            <div class="col-md-4">
                                <button type="submit"  class="btn btn-primary">${save}</button>
                            </div>
                        </div>
                    </form>
                    <script>
                        $('#staff_description').submit(function () {
                            var data = $(this).serialize();
                            $.ajax({
                                url:'controller',
                                type:'POST',
                                dataType:'json',
                                data:data,
                                success:function (data) {

                                    if(data.result==true){
                                        Notify.generate('${infoSuccessUpdate}', '${completed}', 1);
                                    }

                                    else {
                                        Notify.generate("${errorInfoUpdate}", "${error}", 3);
                                    }

                                },
                                error:function () {
                                    Notify.generate("${errorInfoUpdate}", "${error}", 3);
                                }
                            });
                            return false;
                        });
                    </script>
                </c:if>
                <script>

                    $('#profile_info_form').submit(function () {
                        var data = $(this).serialize();

                        $.ajax({
                            url: "controller",
                            type: "POST",
                            dataType:"json",
                            data:data,
                            success: function (data) {
                                if(data.result==true){
                                    Notify.generate('${personalInfoSaved}', '${completed}', 1)
                                }
                                else {
                                    Notify.generate('${serverResponse} '+data.message, '${error}', 2);
                                }

                            }
                        });
                        return false;
                    });

                    $("#save_password").click(function () {
                        var oldPassword = $("#old_password").val();
                        var newPassword = $("#new_password").val();
                        var confirmedPassword = $("#confirm_password").val();

                        if(oldPassword==""||newPassword==""||confirmedPassword==""){
                            Notify.generate('${allFieldsMust}', '${error}', 2);
                            return;
                        }

                        if(newPassword!=confirmedPassword){
                            Notify.generate('${passNotSame}', '${error}', 2);
                            return;
                        }

                        $.ajax({
                            url: "controller",
                            type: "POST",
                            dataType:"json",
                            data:{command:"UPDATE_PASSWORD", new_password:newPassword, old_password:oldPassword},
                            success:function (data) {
                                if(data.result==true){
                                    Notify.generate('${passwordSaved}', '${completed}', 1);
                                } else {
                                    Notify.generate('${checkPassord}', '${error}', 2);
                                }
                            }
                        });
                    });

                    $("#cont_form").submit(function () {

                        var data = $(this).serialize();
                        $.ajax({
                            url: "controller",
                            type: "POST",
                            dataType:"json",
                            data:data,
                            success: function (data) {
                                if(data.result==true){
                                    Notify.generate('${contactsSaved}', '${completed}', 1);
                                }
                                else {
                                    Notify.generate("${serverResponse} "+data.message, '${error}', 2);
                                }

                            }
                        });
                        return false;
                    });

                    $('#save_image').click(function(){

                        if($("#profile_image1").val()==""){
                            Notify.generate("${photoNotSelected}", '${error}', 2);
                            return;
                        }

                        var formData = new FormData();
                        formData.append("command", "UPLOAD_PROFILE_IMAGE");
                        formData.append("webcam", $("#profile_image1")[0].files[0]);

                        if($('#profile_image1')[0].files[0].size>1.342e+9){
                            Notify.generate("${tooLarge}", "${error}", 2);
                            return;
                        }

                        if($('#profile_image1')[0].files[0].type!="image/jpeg"){
                            Notify.generate("${notImage}", "${error}", 2);
                            return;
                        }

                        $("#loading").show();
                        $.ajax({
                            type: 'POST',
                            url:'controller',
                            data:formData,
                            cache: false,
                            contentType: false,
                            processData: false,
                            success:function (data) {
                                $("#loading").hide();
                                if(data.result==true) {
                                    Notify.generate("${newPhotoSaved}", '${completed}', 1);
                                }
                                else {
                                    Notify.generate("${serverResponse}: "+data.message, '${error}', 2);
                                }
                            }

                        });
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
    <c:if test="${user.userRole eq 'CLIENT' or user.userRole eq 'DOCTOR'}">
        <script src="js/sendRequest.js"></script>
    </c:if>
    <c:if test="${user.userRole eq 'DOCTOR'}">
        <script src="js/requestsForPrescription.js"></script>
    </c:if>
    <script src="js/switchLocale.js"></script>
</html>