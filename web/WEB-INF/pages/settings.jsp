<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="footer.jsp"%>
<%@include file="header.jsp"%>
<c:if test="${sessionScope.user eq null}">
    <c:redirect url="http://localhost:8080"/>
</c:if>
<jsp:useBean id="user" scope="session" class="by.training.online_pharmacy.domain.user.User"/>
<c:set var="prevRequest" value="<%=request.getRequestURL().toString()%>" scope="session"/>
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
    </head>
    <body>
        <div class="container content">
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
                                <input id="first_name"  name="first_name" type="text" value="${user.firstName}" class="form-control input-md span3" required="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="second_name">Фамилия:</label>  
                            <div class="col-md-4">
                                <input id="second_name"  name="second_name" type="text" value="${user.secondName}" class="form-control input-md span3" required="">
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
                                <input id="old_password"  name="old_password" type="password" class="form-control input-md span3" required="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="new_password">Новый пароль:</label>  
                            <div class="col-md-4">
                                <input id="new_password"  name="new_password" type="password" class="form-control input-md span3" required="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="confirm_password">Подтверждение пароля:</label>  
                            <div class="col-md-4">
                                <input id="confirm_password"  name="confirm_password" type="password" class="form-control input-md span3" required="">
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
                                <input id="e-mail"  name="e-mail" type="email" value="${user.mail}" class="form-control input-md span3" required="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="phone_number">Номер телефона:</label>
                            <div class="col-md-4">
                                <input id="phone_number"  name="phone_number" type="text" value="${user.phone}" class="form-control input-md span3" required="">
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
               <!-- </form>-->
                <script src="js/settings.js"></script>
            </div>
        </div>
    </body>
</html>