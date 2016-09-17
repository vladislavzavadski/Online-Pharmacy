/**
 * Created by vladislav on 17.09.16.
 */
$('#orders').on('click', '.complete_order', function () {
   var orderId = $(this).attr('data-order');
    var parent = $(this).parent();

    $.ajax({
        url:'controller',
        dataType:'json',
        type:'POST',
        data:{command:'COMPLETE_ORDER', order_id:orderId},
        success:function (data) {
            if(data.result==true){
                parent.html("<span style=\"color: green\">Завершено!</span>");
            }
        }
    });

    return false;
});

$('#search_by_id').submit(function () {
    var data = $(this).serialize();

    $.get("/controller?"+data, function (data) {
       $('#orders').html(data);
    });

    paginationUrl=null;
    var pushed = {foo:"bar"};
    window.history.pushState(pushed, "page", "/controller?"+data+"&overload=true");
    return false;
});