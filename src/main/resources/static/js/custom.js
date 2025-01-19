// to get current year
function getYear() {
    var currentDate = new Date();
    var currentYear = currentDate.getFullYear();
    document.querySelector("#displayYear").innerHTML = currentYear;
}

getYear();


/** google_map js **/
function myMap() {
    var mapProp = {
        center: new google.maps.LatLng(40.712775, -74.005973),
        zoom: 18,
    };
    var map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
}

$(document).ready(function() {
    $("#sendOrderBtn").on("click", function(event) {
        event.preventDefault();
        $.ajax({
            url: '/order/send-to-telegram',
            method: 'POST',
            success: function(response) {
                alert('Заказ успешно отправлен в Telegram!');
                window.location.href = '/';
                // Можно добавить код для очистки корзины на клиенте, если нужно
            },
            error: function(xhr, status, error) {
                console.error(xhr.responseText);
                alert('Ошибка отправки заказа в Telegram');
                // Можно отобразить сообщение об ошибке
            }
        });
    });
});
