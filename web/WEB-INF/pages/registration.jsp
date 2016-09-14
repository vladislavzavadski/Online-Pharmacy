<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Онлайн аптека</title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/auth-style.css" rel="stylesheet">
    <script src="js/bootstrap.js"></script>
</head>
<body>


<div class="container">
    <div class="card card-container">
        <img id="profile-img" class="profile-img-card" src="//ssl.gstatic.com/accounts/ui/avatar_2x.png" />
        <p class="error-message">${requestScope.message}</p>
        <form id="register-form" method="post" action="/controller">
        <input name="command" type="hidden" value="USER_REGISTRATION">
        <div class="modal-body">
            <div id="div-register-msg">
                <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                <span id="text-register-msg">Регистрация аккаунта</span>
            </div>
            <div class="form_group">
                <label for="register_username">Логин: </label>
                <input id="register_username" class="form-control" type="text" placeholder="Логин" maxlength="30" required name="login" autofocus>
                <span id="login_message"></span>
            </div>
            <div class="form_group">
                <label for="register_first_name">Имя: </label>
                <input id="register_first_name" class="form-control" type="text" placeholder="Имя" maxlength="30" required name="first_name">
            </div>
            <div class="form_group">
                <label for="register_second_name">Фамилия: </label>
                <input id="register_second_name" class="form-control" type="text" placeholder="Фамилия" maxlength="30" required name="second_name">
            </div>
            <div class="form_group">
                <label for="register_email">E-mail: </label>
                <input id="register_email" class="form-control" type="email" placeholder="E-Mail" maxlength="45" required name="email">
            </div>
        </div>
        <div class="modal-footer">
            <div>
                <button type="submit" id="reg_button" class="btn btn-primary btn-lg btn-block" disabled>Зарегистрироваться</button>
            </div>
        </div>
    </form>
        <div class="btn-group" style="margin:auto">
            <a href="https://oauth.vk.com/authorize?client_id=5550894&redirect_uri=http://localhost:8080/controller?command=USER_LOGIN_VK&revoke=1&display=page&scope=4194304&response_type=code" class="btn btn-default"><img class="img-responsive" src="images/vk_enter.jpg" width="50" height="50" alt="Войти через Вконтакте"></a>
            <a href="https://www.facebook.com/dialog/oauth?client_id=1472508072774891&redirect_uri=http://localhost:8080/controller?command=USER_LOGIN_FB" class="btn btn-default"><img src="images/fb-enter.png" class="img-responsive" alt="Войти через Facebook"></a>
            <a href="https://www.linkedin.com/oauth/v2/authorization?client_id=78i3k2ihydq5da&redirect_uri=http://localhost:8080/controller?command=USER_LOGIN_LI&response_type=code" class="btn-default btn"> <img src="images/linkedin_button.jpg" class="img-responsive" alt="Войти через LinkedIn" width="50" height="50"></a>
        </div>
    </div><!-- /card-container -->
</div><!-- /container -->

</body>
</html>