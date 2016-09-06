<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<c:if test="${sessionScope.user ne null}">
   <jsp:forward page="/main.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="ru">
   <head>
      <meta charset="utf-8">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <title>Онлайн аптека</title>
      <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
      <script type="text/javascript" src="http://vk.com/js/api/share.js?90" charset="windows-1251"></script>
      <link href="css/bootstrap.css" rel="stylesheet">
      <link href="css/style.css" rel="stylesheet">
      <script src="js/bootstrap.js"></script>
      <style>
         #notifies {
            position:fixed;
            width:400px;
            height:auto;
            top:100px;
            right:20px;
         }
      </style>
   </head>
   <body>
   <div id="fb-root"></div>
   <script>(function(d, s, id) {
      var js, fjs = d.getElementsByTagName(s)[0];
      if (d.getElementById(id)) return;
      js = d.createElement(s); js.id = id;
      js.src = "//connect.facebook.net/ru_RU/sdk.js#xfbml=1&version=v2.7&appId=1472508072774891";
      fjs.parentNode.insertBefore(js, fjs);
   }(document, 'script', 'facebook-jssdk'));</script>
      <nav class="navbar navbar-default navbar-fixed-top" style="background:#507ecf">
         <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
               <img class="brand-logo image-logo" src="images/logo.jpg" alt="Green cross" width="60" height="50"/>
               <p class="name navbar-brand" style="color:azure">Online Pharmacy</p>
               <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"></button>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
               <ul class="nav navbar-nav navbar-right" style="line-height:60px" id="login-buttons">
                  <button class="btn btn-lg btn-success" data-toggle="modal" data-target="#login-modal"><span class="glyphicon glyphicon-log-in"></span> Вход</button>
                  <button class="btn btn-lg btn-primary" data-toggle="modal" data-target="#registration-modal"><span class="glyphicon glyphicon-plus"></span> Регистрация</button>
               </ul>
            </div>
            <!-- /.navbar-collapse -->
         </div>
         <!-- /.container-fluid -->
      </nav>
      <div class="container content">
         <div id="notifies"></div>
          <h1 class="display_1">Онлайн аптека</h1>
         <div id="main-carousel" class="carousel slide">
            <ol class="carousel-indicators">
               <li class="active" data-target="#main-carousel" data-slide-to="0"></li>
               <li data-target="#main-carousel" data-slide-to="1"></li>
               <li data-target="#main-carousel" data-slide-to="2"></li>
               <li data-target="#main-carousel" data-slide-to="3"></li>
            </ol>
            <div class="carousel-inner">
               <div class="item active">
                  <img src="images/slide1.jpg" alt="Слайд 1" />
                  <div class="carousel-caption">
                     <h3>Заказывайте лекарства не выходя из дома</h3>
                  </div>
               </div>
               <div class="item">
                  <img src="images/slide2.jpg" alt="Слайд 2" />
                  <div class="carousel-caption">
                     <h3>Получайте рецепты онлайн</h3>
                  </div>
               </div>
               <div class="item">
                  <img src="images/slide3.jpg" alt="Слайд 3"/>
                  <div class="carousel-caption">
                     <h3>Широкий выбор лекарств</h3>
                  </div>
               </div>
               <div class="item" style="float:none">
                  <img src="images/slide4.jpg" alt="Слайд 4"/>
                  <div class="carousel-caption">
                     <h3>Но всё же лучше не болейте :)</h3>
                  </div>
               </div>
            </div>
            <a href="#main-carousel" class="left carousel-control" data-slide="prev">
            <span class="glyphicon glyphicon-chevron-left"></span>
            </a>
            <a href="#main-carousel" class="right carousel-control" data-slide="next">
            <span class="glyphicon glyphicon-chevron-right"></span>
            </a>              
         </div>
         <div class="container-fluid">
            <div class="row" align="justify" style="overflow:auto">
               <div class="col-lg-4">
                  <h3>Что это?</h3>
                  <p>
                     С другой стороны начало повседневной работы по формированию позиции способствует подготовки и реализации систем массового участия. Повседневная практика показывает, что реализация намеченных плановых заданий играет важную роль в формировании дальнейших направлений развития. Равным образом укрепление и развитие структуры представляет собой интересный эксперимент проверки системы обучения кадров, соответствует насущным потребностям.
                     Товарищи! дальнейшее развитие различных форм деятельности позволяет выполнять важные задания по разработке новых предложений. Повседневная практика показывает, что сложившаяся структура организации обеспечивает широкому кругу (специалистов) участие в формировании модели развития. Таким образом рамки и место обучения кадров играет важную роль в формировании соответствующий условий активизации. Товарищи! укрепление и развитие структуры позволяет выполнять важные задания по разработке позиций, занимаемых участниками в отношении поставленных задач. Идейные соображения высшего порядка, а также постоянный количественный рост и сфера нашей активности позволяет выполнять важные задания по разработке позиций, занимаемых участниками в отношении поставленных задач.
                     Не следует, однако забывать, что консультация с широким активом играет важную роль в формировании модели развития. Таким образом постоянный количественный рост и сфера нашей активности требуют от нас анализа дальнейших направлений развития. Разнообразный и богатый опыт реализация намеченных плановых заданий позволяет выполнять важные задания по разработке форм развития. Не следует, однако забывать, что консультация с широким активом представляет собой интересный эксперимент проверки позиций, занимаемых участниками в отношении поставленных задач. С другой стороны постоянное информационно-пропагандистское обеспечение нашей деятельности позволяет оценить значение новых предложений.
                  </p>
               </div>
               <div class="col-lg-4">
                  <h3>Зачем это?</h3>
                  <p>
                     С другой стороны начало повседневной работы по формированию позиции способствует подготовки и реализации систем массового участия. Повседневная практика показывает, что реализация намеченных плановых заданий играет важную роль в формировании дальнейших направлений развития. Равным образом укрепление и развитие структуры представляет собой интересный эксперимент проверки системы обучения кадров, соответствует насущным потребностям.
                     Товарищи! дальнейшее развитие различных форм деятельности позволяет выполнять важные задания по разработке новых предложений. Повседневная практика показывает, что сложившаяся структура организации обеспечивает широкому кругу (специалистов) участие в формировании модели развития. Таким образом рамки и место обучения кадров играет важную роль в формировании соответствующий условий активизации. Товарищи! укрепление и развитие структуры позволяет выполнять важные задания по разработке позиций, занимаемых участниками в отношении поставленных задач. Идейные соображения высшего порядка, а также постоянный количественный рост и сфера нашей активности позволяет выполнять важные задания по разработке позиций, занимаемых участниками в отношении поставленных задач.
                     Не следует, однако забывать, что консультация с широким активом играет важную роль в формировании модели развития. Таким образом постоянный количественный рост и сфера нашей активности требуют от нас анализа дальнейших направлений развития. Разнообразный и богатый опыт реализация намеченных плановых заданий позволяет выполнять важные задания по разработке форм развития. Не следует, однако забывать, что консультация с широким активом представляет собой интересный эксперимент проверки позиций, занимаемых участниками в отношении поставленных задач. С другой стороны постоянное информационно-пропагандистское обеспечение нашей деятельности позволяет оценить значение новых предложений.
                  </p>
               </div>
               <div class="col-lg-4">
                  <h3>Как этим пользоваться?</h3>
                  <p>
                     С другой стороны начало повседневной работы по формированию позиции способствует подготовки и реализации систем массового участия. Повседневная практика показывает, что реализация намеченных плановых заданий играет важную роль в формировании дальнейших направлений развития. Равным образом укрепление и развитие структуры представляет собой интересный эксперимент проверки системы обучения кадров, соответствует насущным потребностям.
                     Товарищи! дальнейшее развитие различных форм деятельности позволяет выполнять важные задания по разработке новых предложений. Повседневная практика показывает, что сложившаяся структура организации обеспечивает широкому кругу (специалистов) участие в формировании модели развития. Таким образом рамки и место обучения кадров играет важную роль в формировании соответствующий условий активизации. Товарищи! укрепление и развитие структуры позволяет выполнять важные задания по разработке позиций, занимаемых участниками в отношении поставленных задач. Идейные соображения высшего порядка, а также постоянный количественный рост и сфера нашей активности позволяет выполнять важные задания по разработке позиций, занимаемых участниками в отношении поставленных задач.
                     Не следует, однако забывать, что консультация с широким активом играет важную роль в формировании модели развития. Таким образом постоянный количественный рост и сфера нашей активности требуют от нас анализа дальнейших направлений развития. Разнообразный и богатый опыт реализация намеченных плановых заданий позволяет выполнять важные задания по разработке форм развития. Не следует, однако забывать, что консультация с широким активом представляет собой интересный эксперимент проверки позиций, занимаемых участниками в отношении поставленных задач. С другой стороны постоянное информационно-пропагандистское обеспечение нашей деятельности позволяет оценить значение новых предложений.
                  </p>
               </div>
            </div>
         </div>
      </div>
      <div class="modal fade" id="login-modal" tabindex="-1" role="dialog"  aria-hidden="true" style="display: none;">
         <div class="modal-dialog">
            <div class="modal-content">
               <div class="modal-header" align="center">
                  <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                  <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                  </button>
                  <div class="btn-group">
                  <a href="https://oauth.vk.com/authorize?client_id=5550894&redirect_uri=http://localhost:8080/controller?command=USER_LOGIN_VK&revoke=1&display=page&scope=4194304&response_type=code" class="btn btn-default"><img class="img-responsive" src="images/vk_enter.jpg" width="50" height="50" alt="Войти через Вконтакте"></a>
                  <a href="https://www.facebook.com/dialog/oauth?client_id=1472508072774891&redirect_uri=http://localhost:8080/controller?command=USER_LOGIN_FB" class="btn btn-default"><img src="images/fb-enter.png" class="img-responsive" alt="Войти через Facebook"></a>
                  <a href="https://www.linkedin.com/oauth/v2/authorization?client_id=78i3k2ihydq5da&redirect_uri=http://localhost:8080/controller?command=USER_LOGIN_LI&response_type=code" class="btn-default btn"> <img src="images/linkedin_button.jpg" class="img-responsive" alt="Войти через LinkedIn" width="50" height="50"></a>
                  </div>
               </div>

               <form id="login-form" action="/controller" method="post">
                  <input name="command" type="hidden" value="USER_LOGIN">
                  <div class="modal-body">
                     <div id="div-login-msg">
                        <div id="icon-login-msg" class="glyphicon glyphicon-chevron-right"></div>
                        <span id="text-login-msg">Введите логин и пароль</span>
                     </div>
                     <div class="form-group">
                        <label for="sign-login">Логин: </label>
                        <input id="sign-login" class="form-control" type="text" placeholder="Логин" name="login" required>
                     </div>
                     <div class="form-group">
                        <label for="sign-password">Пароль:</label>
                        <input id="sign-password" class="form-control" type="password" placeholder="Пароль" name="password" required>
                     </div>
                     <div class="form-group">
                        <a href="/reestablish">Забыли пароль?</a>
                     </div>
                  </div>
                  <div class="modal-footer">
                     <div>
                        <button id="submitButton" type="submit" class="btn btn-primary btn-lg btn-block">Войти</button>
                     </div>
                  </div>
               </form>
            </div>
         </div>
      </div>
      <div class="modal fade" id="registration-modal" tabindex="-1" role="dialog"  aria-hidden="true" style="display: none;">
         <div class="modal-dialog">
            <div class="modal-content">
               <div class="modal-header" align="center">
                  <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                  <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                  </button>
                  <img class="img-circle img-responsive" id="img_logo" src="images/reg.png" alt="Регистрация">
               </div>
               <form id="register-form" method="post" action="/controller" accept-charset="utf-8">
                  <input name="command" type="hidden" value="USER_REGISTRATION">
                  <div class="modal-body">
                     <div id="div-register-msg">
                        <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                        <span id="text-register-msg">Регистрация аккаунта</span>
                     </div>
                     <div class="form_group">    
                        <label for="register_username">Логин: </label>
                        <input id="register_username" class="form-control" type="text" placeholder="Логин" required name="login" autofocus>
                        <span id="login_message"></span>
                     </div>
                     <div class="form_group">    
                        <label for="register_first_name">Имя: </label>
                        <input id="register_first_name" class="form-control" type="text" placeholder="Имя" required name="first_name">
                     </div>
                     <div class="form_group">    
                        <label for="register_second_name">Фамилия: </label>
                        <input id="register_second_name" class="form-control" type="text" placeholder="Фамилия" required name="second_name">
                     </div>
                     <div class="form_group">
                        <label for="register_email">E-mail: </label>     
                        <input id="register_email" class="form-control" type="email" placeholder="E-Mail" required name="email">
                     </div>
                  </div>
                  <div class="modal-footer">
                     <div>
                        <button type="submit" id="reg_button" class="btn btn-primary btn-lg btn-block" disabled>Зарегистрироваться</button>
                     </div>
                  </div>
               </form>
            </div>
         </div>
      </div>
      <script>
         $('#register-form').submit(function () {
            var data = $(this).serialize();
            $.ajax({
               type:'POST',
               url:'controller',
               dataType:'json',
               data:data,
               success:function (data) {
                  $('#login_message').html("");
                  $('#registration-modal').modal('toggle');
                  $(this).trigger('reset');
                  if(data.result==true){
                     Notify.generate('Вы успешно зарегестрированы. Проверьте ваш почтовый ящик.', 'Готово', 1);

                  }
                  else {
                     Notify.generate('Ответ сервера '+data.message, 'Ошибка', 3);
                  }
               }
            });
            return false;
         });
      </script>
      <script src="js/index.js"></script>
      <div class="modal fade" id="about-modal" tabindex="-1" role="dialog"   aria-hidden="true" style="display: none;">
         <div class="modal-dialog">
            <div class="modal-content">
               <div class="modal-header" align="center">
                  <img class="image-circle img-responsive" src="images/descr.jpg" alt="Description image"/>  
                  <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть"/>
                     <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
               </div>
               <div class="modal-body" style="height:200"; overflow:auto;">
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
       <footer class="footer" style="background:white">
           <div class="container">
               <p class="navbar-text pull-left"> 
                   Site Built By <a href="mailto:vladislav.zavadski@gmail.com">Vladislav Zavadski</a>, EPAM Systems, 2016
               </p>
               <div class="nav navbar-nav navbar-left" style="line-height:50px">
                  <nobr>
                   <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#about-modal">О проекте</button>
                   <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#contacts-modal">Контакты</button>
                  <script type="text/javascript">

                     <!--

                     document.write(VK.Share.button());

                     -->
                  </script>

                  <div class="fb-share-button" data-href="https://www.onliner.by/" data-layout="button_count" data-size="large" data-mobile-iframe="true"><a class="fb-xfbml-parse-ignore" target="_blank" href="https://www.facebook.com/sharer/sharer.php?u=https%3A%2F%2Fwww.onliner.by%2F&amp;src=sdkpreparse">Поделиться</a></div>
                  </nobr>
               </div>

           </div>
  </footer>
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
</html>