/**
 * Created by vladislav on 16.09.16.
 */
$('.switch_locale').click(function () {
    var encodedUrl = encodeURIComponent(window.location.href)
    $(this).attr('href', $(this).attr('href')+'&redirect_url='+encodedUrl);
});