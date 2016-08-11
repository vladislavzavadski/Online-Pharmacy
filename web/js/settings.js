/**
 * Created by vladislav on 03.08.16.
 */
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
            if(data.result==true){
                $("#personal_inf").html("<span style=\"color:green\">Сохранено</span>");
            }
            else {
                $("#contacts_inf").html("<span style=\"color:red\">Ошибка. Сообщение сервера: "+data.message+"</span>");
            }
        }
    });
});

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
            }else if(data.message==null) {
                $("#security_inf").html("<span style=\"color:red\">Старый пароль введен неверно</span>");
            }else {
                $("#security_inf").html("<span style=\"color:red\">Ошибкаytyt. Сообщение сервера: "+data.message+"</span>");
            }
        }
    });
});

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
        data:{command:"UPDATE_CONTACTS", email:mail, phone:phone},
        success: function (data) {
            if(data.result==true){
                $("#contacts_inf").html("<span style=\"color:green\">Сохранено</span>");
            }
            else {
                $("#contacts_inf").html("<span style=\"color:red\">Ошибка. Сообщение сервера: "+data.message+"</span>");
            }
        }
    });
});

$('#save_image').click(function(){
    if($("#profile_image1").val()==""){
        alert("Не выбрано фото для загрузки.");
        return;
    }
    var formData = new FormData();
    formData.append("command", "UPLOAD_PROFILE_IMAGE");
    formData.append("profile_image", $("#profile_image1")[0].files[0]);
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
                $("#photo_inf").html("<span style=\"color:green\">Сохранено</span>");
            }
            else {
                $("#photo_inf").html("<span style=\"color:red\">Ошибка. Ответ сервера: "+data.message+"</span>");
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