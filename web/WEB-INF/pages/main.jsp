<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 22.07.16
  Time: 18:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="ru">
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
</head>
<body>

<nav class="navbar navbar-default navbar-fixed-top" style="background:#507ecf">
    <div class="container-fluid">
        <a href="#menu-toggle" type="button" class="navbar-toggle" style="display:block; background:white;" id="menu-toggle">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </a>
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
                <a href="#" class="btn btn-lg btn-primary">Мои заказы</a>
                <a href="#" class="btn btn-lg btn-primary">Мои рецепты</a>
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
        <div id="sidebar-wrapper">
            <ul class="sidebar-nav">
                <li class="sidebar-brand">
                    <a href="#">
                        Классы лекарств:
                    </a>
                </li>
                <li>
                    <a href="#">Анальгетик</a>
                </li>
                <li>
                    <a href="#">Витамины</a>
                </li>
                <li>
                    <a href="#">Успокоительное</a>
                </li>
                <li>
                    <a href="#">Снотворное</a>
                </li>
                <li>
                    <a href="#">Отхаркивающее</a>
                </li>
                <li>
                    <a href="#">Все классы</a>
                </li>
            </ul>
        </div>
        <div class="container" style="background: white">
            <div class="row">
                <div class="col-lg-6">
                    <img src="/controller?command=GET_PROFILE_IMAGE" class="img-responsive" alt="<jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/>" width="300" height="400"/>
                    <!--<h1><jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/></h1>-->
                </div>
                <div class="col-lg-6">
                    <h1><jsp:getProperty name="user" property="firstName"/> <jsp:getProperty name="user" property="secondName"/></h1>
                    <b>Номер телефона: </b>&nbsp
                    <span><jsp:getProperty name="user" property="phone"/></span>
                    <br/>
                    <b>e-mail: </b>&nbsp
                    <span><a href="mailto:<jsp:getProperty name="user" property="mail"/>"><jsp:getProperty name="user" property="mail"/></a> </span>
                </div>
            </div>
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
    <script>
        $("#menu-toggle").click(function(e) {
            e.preventDefault();
            $("#wrapper").toggleClass("toggled");
        });
    </script>
</body>
</html>
