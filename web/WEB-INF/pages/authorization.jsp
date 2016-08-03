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
            <form class="form-signin" method="post">
                <span id="reauth-email" class="reauth-email"></span>
                <input name="command" value="USER_LOGIN" type="hidden">
                <input name="login" type="text" id="inputEmail" class="form-control" placeholder="Login" value="${param.login}" required autofocus>
                <input name="password" type="password" id="inputPassword" class="form-control" placeholder="Password" required>
                <button class="btn btn-lg btn-primary btn-block btn-signin" type="submit">Sign in</button>
            </form><!-- /form -->
            <a href="#" class="forgot-password">
                Forgot the password?
            </a>
            <div class="btn-group" style="margin:auto">
                <a href="https://oauth.vk.com/authorize?client_id=5550894&redirect_uri=http://localhost:8080/controller?command=USER_LOGIN_VK&revoke=1&display=page&scope=4194304&response_type=code" class="btn btn-default"><img class="img-responsive" src="images/vk_enter.jpg" width="50" height="50" alt="Войти через Вконтакте"></a>
                <a href="https://www.facebook.com/dialog/oauth?client_id=1472508072774891&redirect_uri=http://localhost:8080/controller?command=USER_LOGIN_FB" class="btn btn-default"><img src="images/fb-enter.png" class="img-responsive" alt="Войти через Facebook"></a>
                <a href="https://www.linkedin.com/oauth/v2/authorization?client_id=78i3k2ihydq5da&redirect_uri=http://localhost:8080/controller?command=USER_LOGIN_LI&response_type=code" class="btn-default btn"> <img src="images/linkedin_button.jpg" class="img-responsive" alt="Войти через LinkedIn" width="50" height="50"></a>
            </div>
        </div><!-- /card-container -->
    </div><!-- /container -->    
       
   </body>
</html>