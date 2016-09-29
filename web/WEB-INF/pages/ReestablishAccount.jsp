<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="language" value="${not empty sessionScope.language ? sessionScope.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="resource.locale" var="loc"/>
<fmt:message bundle="${loc}" key="locale.online_pharmacy" var="onlinePharmacy"/>
<fmt:message bundle="${loc}" key="locale.reestablish" var="reestablish"/>
<fmt:message bundle="${loc}" key="locale.login" var="login"/>
<fmt:message bundle="${loc}" key="locale.secret_word" var="secretWord"/>
<fmt:message bundle="${loc}" key="locale.question" var="question"/>
<fmt:message bundle="${loc}" key="locale.completed" var="completed"/>
<fmt:message bundle="${loc}" key="locale.error" var="error"/>
<fmt:message bundle="${loc}" key="locale.user_not_found_question" var="userNotFoundQuestion"/>
<fmt:message bundle="${loc}" key="locale.success_reestablish" var="successReestablish"/>
<fmt:message bundle="${loc}" key="locale.invalid_response" var="invalidResponse"/>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${onlinePharmacy}</title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/auth-style.css" rel="stylesheet">
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
<div id="notifies"></div>

<div class="container">
    <div class="card card-container">
        <img id="profile-img" class="profile-img-card" src="//ssl.gstatic.com/accounts/ui/avatar_2x.png" />
        <p class="error-message">${requestScope.message}</p>
        <form id="reestablish-form" class="form-signin" method="post">
            <span id="reauth-email" class="reauth-email"></span>
            <input name="command" value="REESTABLISH_ACCOUNT" type="hidden">
            <input name="login" type="text" id="input_login" class="form-control" placeholder="${login}" required autofocus>
            <span id="secret_message"></span>
            <input name="secret_word" type="text" id="inputPassword" class="form-control" placeholder="${secretWord}" required>
            <button class="btn btn-lg btn-primary btn-block btn-signin" id="submit_button" type="submit" disabled>${reestablish}</button>
        </form><!-- /form -->
    </div><!-- /card-container -->
</div><!-- /container -->
<script>
    $('#input_login').blur(function () {
        var secretMessage = $('#secret_message');
        var login = $(this).val();
        $('#submit_button').prop('disabled', true);
        if(login==""){
            secretMessage.html("");
            return;
        }
        $.ajax({
            url:'controller',
            type:'POST',
            dataType:'json',
            data:{command:'GET_SECRET', login:login},
            success:function (data) {
                if(data.result==true&&data.question!=""){
                    secretMessage.html("<span style=\"color:green\">${question}: "+data.question+"</span>");
                    $('#submit_button').prop('disabled', false);
                }
                else {
                    secretMessage.html("<span style=\"color:red\">${userNotFoundQuestion}: </span>");
                }
            }
        });
    });
    $('#reestablish-form').submit(function () {
        var data = $(this).serialize();
        $.ajax({
            url:'controller',
            type:'POST',
            dataType:'json',
            data:data,
            success:function (data) {
                if(data.result==true){
                    $(this).trigger('reset');
                    Notify.generate('${successReestablish}' , '${completed}!', 1);
                }
                else {
                    Notify.generate('${invalidResponse}', '${error}!', 3);
                }
            }
        });
        return false;
    });
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
</body>
</html>