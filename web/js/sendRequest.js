/**
 * Created by vladislav on 11.09.16.
 */
setInterval(function () {
    var messageCount = $('#mes_count');
    $.ajax({
        url:'controller',
        type:'GET',
        dataType:'json',
        data:{command:'GET_MESSAGE_COUNT', message_status:'NEW'},
        success: function (data) {
            if(data.result==true){
                messageCount.html(data.count);
            }
        }
    });
}, 5000);
