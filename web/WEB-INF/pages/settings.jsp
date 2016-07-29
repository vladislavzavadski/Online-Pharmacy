<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    </head>
    <body>
        
        <nav class="navbar navbar-default navbar-fixed-top" style="background:#507ecf">
            <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <img class="brand-logo image-logo" src="images/logo.jpg" alt="Green cross" width="60" height="50"/>
                <p class="name navbar-brand" style="color:azure">Online Pharmacy</p>
                <button type="button" class="navbar-toggle" style="float:right; background:white;" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <form class="navbar-form navbar-left" role="search" style="padding-top:20px;">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-info">Найти</button>
                </form>
                <div class="btn-group" style="padding-top:20px;" role="group">
                    <c:if test="${user.userRole eq 'CLIENT'}">
                        <a href="#" class="btn btn-lg btn-primary">Мои заказы</a>
                        <a href="#" class="btn btn-lg btn-primary">Мои рецепты</a>
                    </c:if>
                    <a href="#" class="btn btn-lg btn-primary">Мои настройки</a>
                    <a href="#" class="btn btn-lg btn-primary">Лекарства</a>
                    <a href="#" class="btn btn-lg btn-primary">Врачи</a>
                    <a href="#" class="btn btn-lg btn-primary">Выход</a>
                </div>
                <!-- /.navbar-collapse -->
            </div>
                <!-- /.container-fluid -->
        </nav>
        <div id="wrapper">
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
                <script>
                    $("#save_personal_info").click(function () {
                        var firstName = $("#first_name").val();
                        var secondName = $("#second_name").val();
                        var gender = $("#gender_select").val();
                        if(firstName==""){
                            $("#personal_inf").html("<span style=\"color:red\">Поле ИМЯ должно быть заполнено</span>");
                            return;
                        }
                        if(secondName==""){
                            $("#personal_inf").html("<span style=\"color:red\">Поле ФАМИЛИЯ должно быть заполнено</span>");
                            return;
                        }
                        $.ajax({
                            url: "controller",
                            type: "POST",
                            dataType:"json",
                            data:{command:"UPDATE_PERSONAL_INFORMATION", first_name:firstName, second_name:secondName, gender:gender},
                            success: function (data) {
                                if(data.result=="success"){
                                    $("#personal_inf").html("<span style=\"color:green\">Сохранено</span>");
                                }
                            }
                        });
                    });
                </script>
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
                <script>
                    $("#save_password").click(function () {
                        var oldPassword = $("#old_password").val();
                        var newPassword = $("#new_password").val();
                        var confirmedPassword = $("#confirm_password").val();
                        if(newPassword!=confirmedPassword){
                            $("#security_inf").html("<span style=\"color:red\">Введенные пароли не совпадают</span>");
                            return;
                        }
                        $.ajax({
                            url: "controller",
                            type: "POST",
                            dataType:"json",
                            data:{command:"UPDATE_PASSWORD", new_password:newPassword, old_password:oldPassword},
                            success:function (data) {
                                if(data.result==true){
                                    $("#security_inf").html("<span style=\"color:green\">Сохранено</span>");
                                }else {
                                    $("#security_inf").html("<span style=\"color:red\">Старый пароль введен неверно</span>");
                                }
                            }
                        });
                    });
                </script>
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
                <script>
                    $("#save_contacts").click(function () {
                        var mail = $("#e-mail").val();
                        var phone = $("#phone_number").val();
                        if(mail==""){
                            $("#contacts_inf").html("<span style=\"color:red\">Поле E-MAIL не должно быть пустым</span>");
                            return;
                        }
                        if(mail==""){
                            $("#contacts_inf").html("<span style=\"color:red\">Поле НОМЕР ТЕЛЕФОНА не должно быть пустым</span>");
                            return;
                        }
                        $.ajax({
                            url: "controller",
                            type: "POST",
                            dataType:"json",
                            data:{command:"UPDATE_CONTACTS", mail:mail, phone:phone},
                            success: function (data) {
                                if(data.result==true){
                                    $("#contacts_inf").html("<span style=\"color:green\">Сохранено</span>");
                                }
                            }
                        });
                    });
                </script>
                <form class="form-horizontal">
                    <fieldset>
                        <legend>Фото профиля</legend>
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="profile_image">Фото:</label>  
                            <div class="col-md-4">
                                <input id="profile_image"  name="profile_image" type="file" class="form-control input-md span3" required="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"></label>
                            <div class="col-md-4">
                                <button  class="btn btn-primary">Сохранить</button>
                            </div>
                        </div>
                    </fieldset>
                </form>                     
                   

            </div>
        </div>
        <div class="modal fade" id="about-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" align="center">
                        <img class="image-circle img-responsive" src="images/descr.jpg" alt="О проекте"/>  
                        <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть"/>
                            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                    </div>
                    <div class="modal-body" style="height:200; overflow:auto;">
                    <p align="justify">
                    Представляем вашему вниманию онлайн-аптеку.
                    Здесь вы можете заказывать и покупать лекарста. Так же возможно получение рецепта на то или иное лекарство.
                    </p>    
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="contacts-modal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" align="center">
                        <img class="img-circle img-responsive" src="images/contacts.jpg" alt="Контакты"/>    
                        <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть"/>
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                    </div>
                    <div class="modal-body">
                        <div class="modal-body">
                            <div class="form_group">
                                <b>Адрес:</b>&nbsp;<span>Минск, ул. Купревича 1/2</span>
                                <br/>
                                <b>Телефон:</b>&nbsp;<span>+375447350720</span>
                                <br/>
                                <b>email:</b>&nbsp;<span><a href="mailto:vladislav.zavadski@gmail.com">vladislav.zavadski@gmail.com</a></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </div>      
            <footer class="footer">
                <div class="container">
                    <p class="navbar-text pull-left"> 
                        Site Built By <a href="mailto:vladislav.zavadski@gmail.com">Vladislav Zavadski</a>, EPAM Systems, 2016
                    </p>
                    <div class="nav navbar-nav navbar-left" style="line-height:50px">
                        <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#about-modal">О проекте</button>
                        <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#contacts-modal">Контакты</button>
                    </div>
                </div>
            </footer>
    </body>
</html>