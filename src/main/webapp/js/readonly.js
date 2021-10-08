$(document).ready(function () {
    getCurrentUser().then(function (user) {
        drawNavMenu(user);
    })
    initPage();
});

function initPage() {
    let params = new URLSearchParams(document.location.search.substring(1));
    let id = +params.get("id");
    if (id === 0) {
        return;
    }
    $.ajax({
        type: 'GET',
        url: cPath + 'model/advertisement.do?id=' + id,
        dataType: 'json'
    }).done(function (advertisement) {
        $('#created').text(getFormattedDate(new Date(advertisement.created)));
        $('#sold').text(advertisement.sold ? 'Да' : 'Нет');
        $('#brand').text(advertisement.model.brand.name);
        $('#model').text(advertisement.model.name);
        $('#bodyType').text(advertisement.bodyType.name);
        $('#transmission').text(advertisement.transmission.name);
        $('#productionYear').text(advertisement.productionYear);
        $('#mileage').text(advertisement.mileage.toLocaleString() + ' км')
        $('#price').text(advertisement.price.toLocaleString() + ' р.');
        $('#firstName').text(advertisement.author.firstName);
        $('#phone').text(advertisement.author.phone);
        $('#email').text(advertisement.author.email);
        $('#photo').prop('src', cPath + "model/advertisement/downloadPhoto.do?id=" + advertisement.id);
        $('#description').text(advertisement.description);
    }).fail(function (err) {
        console.log(err);
    });
}
