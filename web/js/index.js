/**
 * Created by vladislav on 03.08.16.
 */
$("#register_username").blur(function () {
    if($(this).val()==""){
        return false;
    }

    $.ajax({
        url:"controller",
        type:"POST",
        dataType:'json',
        success:function (data) {
            if(data.isExist==false){
                $("#login_message").html("<span style=\"color:green\">Данный логин свободен</span>");
                $("#reg_button").prop("disabled", false);
            }
            else {
                $("#login_message").html("<span style=\"color:red\">Данный логин занят</span>");
                $("#reg_button").prop("disabled", true);
            }

        },
        data: {login: $("#register_username").val(), command:"CHECK_LOGIN"}
    });
});