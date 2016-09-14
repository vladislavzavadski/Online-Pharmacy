/**
 * Created by vladislav on 11.09.16.
 */
setInterval(function () {
    var count = $('#req_count');
    $.ajax({
        url:'controller',
        type:'GET',
        dataType:'json',
        data:{command:'REQUEST_COUNT'},
        success:function (data) {
            if(data.result==true){
                count.html(data.request_count);
            }
        }
    });
}, 5000);