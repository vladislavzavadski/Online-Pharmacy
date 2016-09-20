<%--
  Created by IntelliJ IDEA.
  User: vladislav
  Date: 02.08.16
  Time: 10:36
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty sessionScope.language ? sessionScope.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="resource.locale" var="loc"/>
<fmt:message bundle="${loc}" key="locale.present_pharmacy" var="presentPharmacy"/>
<fmt:message bundle="${loc}" key="locale.address" var="address"/>
<fmt:message bundle="${loc}" key="locale.location" var="location"/>
<fmt:message bundle="${loc}" key="locale.share" var="share"/>
<!-- Put this script tag to the <head> of your page -->
<script type="text/javascript" src="http://vk.com/js/api/share.js?94" charset="windows-1251"></script>
<div id="fb-root"></div>
<script>(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/ru_RU/sdk.js#xfbml=1&version=v2.7&appId=1472508072774891";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>
<footer class="footer">
    <div class="container">
        <p class="navbar-text pull-left">
            ${siteBUilder} <a href="mailto:vladislav.zavadski@gmail.com">Vladislav Zavadski</a>, EPAM Systems, 2016
        </p>
        <div class="nav navbar-nav navbar-left" style="line-height:50px">
            <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#about-modal">${about}</button>
            <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#contacts-modal">${contacts}</button>
            <!-- Put this script tag to the place, where the Share button will be -->
            <script type="text/javascript"><!--
            document.write(VK.Share.button({url: "http://pharmacy.mycloud.by"},{type: "round", text: "${share}"}));
            --></script>

            <div class="fb-share-button" data-href="http://pharmacy.mycloud.by" data-layout="button_count" data-size="large" data-mobile-iframe="true"><a class="fb-xfbml-parse-ignore" target="_blank" href="https://www.facebook.com/sharer/sharer.php?u=http%3A%2F%2Fpharmacy.mycloud.by%2F&amp;src=sdkpreparse">${share}</a></div>
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
                    ${presentPharmacy}
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
                        <b>${address}:</b>&nbsp;<span>${location}</span>
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
