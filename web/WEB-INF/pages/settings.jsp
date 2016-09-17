<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="footer.jsp"%>
<%@include file="header.jsp"%>
<c:if test="${sessionScope.user eq null}">
    <c:redirect url="http://localhost:8080"/>
</c:if>
<jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>Настройки</title>
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
            <h1 class="display_1">Настройки</h1>
            <div class="container" style="background:white">
                <div class="form-horizontal">
                    <fieldset>
                        <legend>Личная информация</legend>
                        <span id="personal_inf"></span>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="first_name">Имя:</label>  
                            <div class="col-md-4">
                                <input id="first_name"  name="first_name" maxlength="30" type="text" value="${user.firstName}" class="form-control input-md span3" required="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="second_name">Фамилия:</label>  
                            <div class="col-md-4">
                                <input id="second_name" maxlength="30"  name="second_name" type="text" value="${user.secondName}" class="form-control input-md span3" required="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="gender_select">Пол:</label>
                            <div class="col-md-4">
                                <select name="gender" id="gender_select" class="selectpicker form-control input-md span3">
                                    <option value="MALE" ${user.gender eq 'MALE'?'selected':''}>Мужчина</option>
                                    <option value="FEMALE" ${user.gender eq 'FEMALE'?'selected':''}>Женщина</option>
                                    <option value="UNKNOWN" ${user.gender eq 'UNKNOWN'?'selected':''}>Неизвестно</option>
                                </select>
                            </div>

                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"></label>
                            <div class="col-md-4">
                                <button id="save_personal_info" class="btn btn-primary">Сохранить</button>
                            </div>
                        </div>

                    </fieldset>
                </div>
                <c:if test="${user.registrationType eq 'NATIVE'}">
                <div class="form-horizontal">
                    <fieldset>
                        <legend>Безопасность</legend>
                        <span id="security_inf"></span>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="old_password">Старый пароль:</label>  
                            <div class="col-md-4">
                                <input id="old_password" maxlength="60"  name="old_password" type="password" class="form-control input-md span3" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="new_password">Новый пароль:</label>  
                            <div class="col-md-4">
                                <input id="new_password" maxlength="60"  name="new_password" type="password" class="form-control input-md span3" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="confirm_password">Подтверждение пароля:</label>  
                            <div class="col-md-4">
                                <input id="confirm_password" maxlength="60"  name="confirm_password" type="password" class="form-control input-md span3" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"></label>
                            <div class="col-md-4">
                                <button id="save_password" class="btn btn-primary">Сохранить</button>
                            </div>
                        </div>

                    </fieldset>
                </div>
                </c:if>
                <div class="form-horizontal">
                    <fieldset>
                        <legend>Контакты</legend>
                        <span id="contacts_inf"></span>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="e-mail">E-mail:</label>  
                            <div class="col-md-4">
                                <input id="e-mail" maxlength="45"  name="e-mail" type="email" value="${user.mail}" class="form-control input-md span3" required="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="phone_number">Номер телефона:</label>
                            <div class="col-md-4">
                                <input id="phone_number" maxlength="15"  name="phone_number" type="text" value="${user.phone}" class="form-control input-md span3" required="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"></label>
                            <div class="col-md-4">
                                <button id="save_contacts" class="btn btn-primary">Сохранить</button>
                            </div>
                        </div>

                    </fieldset>
                </div>
               <!-- <form id="image_form" class="form-horizontal" action="/controller" method="post" enctype="multipart/form-data" name="contact">-->
                <div class="form-horizontal">
                <fieldset>
                        <legend>Фото профиля</legend>
                        <span id="photo_inf"></span>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="profile_image1">Фото:</label>
                            <div class="col-md-4">
                                <input id="profile_image1"  name="profile_image" type="file" class="form-control input-md span3" accept="image/*" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"></label>
                            <div class="col-md-4">
                                <button type="submit" id="save_image"  class="btn btn-primary">Сохранить</button>
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
                            <legend>Восстановление пароля</legend>
                            <div class="form-group">
                                <label class="col-md-4 control-label" for="questions">Вопрос:</label>
                                <div class="col-md-4">
                                    <select name="question_id" id="questions" class="selectpicker form-control input-md span3" required>
                                        <c:forEach items="${secretQuestions}" var="question">
                                            <option value="${question.id}">${question.question}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label" for="secret_word">Ответ:</label>
                                <div class="col-md-4">
                                    <input type="text" maxlength="50" class="form-control input-md span3" name="secret_word" id="secret_word" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label"></label>
                                <div class="col-md-4">
                                    <button type="submit" id="save_secret"  class="btn btn-primary">Сохранить</button>
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
                                        Notify.generate('Секретное слово сохранено','Сохранено',  1)
                                    }
                                    else {
                                        if(data.isCritical){
                                            Notify.generate('Логин под которым вы авторизованы был удален из базы данных.', 'Критическая ошибка', 3);
                                            setTimeout(function () {
                                                window.location.assign("/index.jsp");
                                            }, 5000);
                                        }
                                        else {
                                            Notify.generate('Не сохранить изменения. Ответ сервера: '+data.message, 'Не сохранить изменения', 2);
                                        }
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
                        <legeng>Описание персонала</legeng>
                        <div class="form-group">
                            <label for="specialization" class="col-md-4 control-label">Специализация</label>
                            <div class="col-md-4">
                                <input type="text" value="${user.userDescription.specialization}" class="form-control input-md span3" name="specialization" id="specialization" placeholder="Специализация" required maxlength="40">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="description" class="col-md-4 control-label">Описание:</label>
                            <div class="col-md-4">
                                <textarea id="description" name="description" maxlength="300" placeholder="Описание" required>${user.userDescription.description}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"></label>
                            <div class="col-md-4">
                                <button type="submit"  class="btn btn-primary">Сохранить</button>
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
                                        Notify.generate('Информация успешно обновлена', 'Готово', 1);
                                    }
                                    else {
                                        Notify.generate("Произошла ошибка при попытке обновления информации", "Ошибка", 3);
                                    }
                                },
                                error:function () {
                                    Notify.generate("Произошла ошибка при попытке обновления информации", "Ошибка", 3);
                                }
                            });
                            return false;
                        });
                    </script>
                </c:if>
                <c:if test="${user.registrationType eq 'NATIVE'}">
                <div class="form-horizontal">
                    <fieldset>
                        <legend>Удаление аккаунта</legend>
                        <span id="delete_inf"></span>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="delete_password">Пароль:</label>
                            <div class="col-md-4">
                                <input id="delete_password"  name="profile_image" type="password" class="form-control input-md span3" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"></label>
                            <div class="col-md-4">
                                <button type="submit" id="delete_user"  class="btn btn-danger">Удалить</button>
                            </div>
                        </div>
                    </fieldset>
                </div>
                </c:if>

                <script>

                    $("#save_personal_info").click(function () {
                        var firstName = $("#first_name").val();
                        var secondName = $("#second_name").val();
                        var gender = $("#gender_select").val();
                        if(firstName==""){
                            Notify.generate('Поле имя не должно быть пустым', 'Ошибка', 2);
                            return;
                        }
                        if(secondName==""){
                            Notify.generate('Поле ФАМИЛИЯ должно быть заполнено', 'Ошибка', 2);
                            return;
                        }
                        $.ajax({
                            url: "controller",
                            type: "POST",
                            dataType:"json",
                            data:{command:"UPDATE_PERSONAL_INFORMATION", first_name:firstName, second_name:secondName, gender:gender},
                            success: function (data) {
                                if(data.result==true){
                                    Notify.generate('Сохранено', 'Персональная информация успешно сохранена', 1)
                                }
                                else {
                                    if(data.isCritical){
                                        Notify.generate('Логин под которым вы авторизованы был удален из базы данных.', 'Критическая ошибка', 3);
                                        setTimeout(function () {
                                            window.location.assign("/index.jsp");
                                        }, 5000);
                                    }
                                    else {
                                        Notify.generate('Не сохранить изменения. Ответ сервера: '+data.message, 'Не сохранить изменения', 2);
                                    }
                                }
                            }
                        });
                    });

                    $("#save_password").click(function () {
                        var oldPassword = $("#old_password").val();
                        var newPassword = $("#new_password").val();
                        var confirmedPassword = $("#confirm_password").val();
                        if(oldPassword==""||newPassword==""||confirmedPassword==""){
                            Notify.generate('Все поля должны быть заполнены', 'Ошибка', 2);
                            return;
                        }
                        if(newPassword!=confirmedPassword){
                            Notify.generate('Введенные пароли не совпадают', 'Ошибка', 2);
                            return;
                        }
                        $.ajax({
                            url: "controller",
                            type: "POST",
                            dataType:"json",
                            data:{command:"UPDATE_PASSWORD", new_password:newPassword, old_password:oldPassword},
                            success:function (data) {
                                if(data.result==true){
                                    Notify.generate('Сохранено', 'Новый пароль сохранен', 1);
                                }else if(data.isCritical){
                                    Notify.generate('Логин под которым вы авторизованы был удален из базы данных.', 'Критическая ошибка', 3);
                                    setTimeout(function () {
                                        window.location = "/index.jsp";
                                    }, 3);
                                } else {
                                    Notify.generate('Проверьте введенный пароль', 'Ошибка', 2);
                                }
                            }
                        });
                    });

                    $("#save_contacts").click(function () {
                        var mail = $("#e-mail").val();
                        var phone = $("#phone_number").val();
                        if(mail==""){
                            Notify.generate('Поле E-MAIL не должно быть пустым', 'Ошибка', 2);
                            return;
                        }
                        if(mail==""){
                            $("#contacts_inf").html("<span style=\"color:red\"></span>");
                            Notify.generate('Поле НОМЕР ТЕЛЕФОНА не должно быть пустым', 'Ошибка', 2);
                            return;
                        }
                        $.ajax({
                            url: "controller",
                            type: "POST",
                            dataType:"json",
                            data:{command:"UPDATE_CONTACTS", email:mail, phone:phone},
                            success: function (data) {
                                if(data.result==true){
                                    Notify.generate('Контакты успешно сохранены', 'Сохранено', 1);
                                }
                                else if(!data.isCritical){
                                    Notify.generate("Ошибка. Сообщение сервера: "+data.message, 'Ошибка', 2);
                                }
                                else {
                                    Notify.generate('Логин под которым вы авторизованы был удален из базы данных.', 'Критическая ошибка', 3);
                                    setTimeout(function () {
                                        window.location = "/index.jsp";
                                    }, 3);
                                }
                            }
                        });
                    });

                    $('#save_image').click(function(){
                        if($("#profile_image1").val()==""){
                            Notify.generate("Не выбрано фото для загрузки", 'Ошибка', 2);
                            return;
                        }
                        var formData = new FormData();
                        formData.append("command", "UPLOAD_PROFILE_IMAGE");
                        formData.append("profile_image", $("#profile_image1")[0].files[0]);
                        alert($("#profile_image1")[0].files[0].size+" "+$("#profile_image1")[0].files[0].type);
                        if($('#profile_image1')[0].files[0].size>1.342e+9){
                            Notify.generate("Размер файла слишком велик");
                            return;
                        }
                        if(!$('#profile_image1')[0].files[0].type.contains('image')){
                            Notify.generate("Данный файл не является картинкой");
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
                                    Notify.generate("Новое фото успешно сохранено", 'Сохранено', 1);
                                }
                                else if(!data.isCritical){
                                    Notify.generate("Ошибка. Сообщение сервера: "+data.message, 'Ошибка', 2);
                                }
                                else {
                                    Notify.generate('Логин под которым вы авторизованы был удален из базы данных.', 'Критическая ошибка', 3);
                                    setTimeout(function () {
                                        window.location = "/index.jsp";
                                    }, 3);
                                }
                            }
                        });
                    });

                    $('#delete_user').click(function () {
                        var password = $("#delete_password").val();
                        if(password==""){
                            $("#delete_inf").html("<span style=\"color:red\">Пароль не должен быть пустым</span>");
                            return;
                        }
                        $.ajax({
                            type:'POST',
                            url:'controller',
                            dataType:'json',
                            data:{command:'DELETE_USER', password:password},
                            success: function (data) {
                                if(data.result==true){
                                    window.location = "/index.jsp";
                                }
                                else {
                                    $("#delete_inf").html("<span style=\"color:red\">Введен ошибочный пароль</span>");
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