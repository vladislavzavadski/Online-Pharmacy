<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 02.08.16
  Time: 10:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<footer class="footer">
    <div class="container">
        <p class="navbar-text pull-left">
            ${siteBUilder} <a href="mailto:vladislav.zavadski@gmail.com">Vladislav Zavadski</a>, EPAM Systems, 2016
        </p>
        <div class="nav navbar-nav navbar-left" style="line-height:50px">
            <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#about-modal">${about}</button>
            <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#contacts-modal">${contacts}</button>
        </div>
    </div>
</footer>
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
                        <b>${phoneNumber}:</b>&nbsp;<span>+375447350720</span>
                        <br/>
                        <b>email:</b>&nbsp;<span><a href="mailto:vladislav.zavadski@gmail.com">vladislav.zavadski@gmail.com</a></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
