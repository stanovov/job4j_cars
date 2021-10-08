let needDeletePhoto = false;
let lUser = null;
let lAdvertisement = null;

$(document).ready(function () {
    getCurrentUser().then(function (user) {
        lUser = user;
        drawNavMenu(user);
    });
    initPage();
});

function initPage() {
    $.when(
        ajaxBrands(),
        ajaxAdvertisement()
    ).then(function (rslBrands, rslAdvertisement) {
        lAdvertisement = rslAdvertisement[0];
        displaySpecifications('brand', rslBrands[0]);
        $.when(
            ajaxModels(false),
            ajaxBodyTypes(),
            ajaxTransmissions(),
            ajaxProductionYears()
        ).then(function (rslModels, rslBodyTypes, rslTransmissions, rslProductionYears) {
            displaySpecifications('model', rslModels[0]);
            displaySpecifications('bodyType', rslBodyTypes[0]);
            displaySpecifications('transmission', rslTransmissions[0]);
            displayProductionYears(rslProductionYears[0]);
            displayAdvertisement();
        });
    });
}

function ajaxBrands() {
    return ajax('model/brand.do');
}

function ajaxModels(changeBrand) {
    let brandId;
    if (changeBrand) {
        brandId = $('#brand').val();
    } else {
        brandId = lAdvertisement === null
            ? $('#brand').val()
            : lAdvertisement.model.brand.id;
    }
    return ajax('model/model.do?brandId=' + brandId);
}

function ajaxBodyTypes() {
    return ajax('model/bodyType.do');
}

function ajaxTransmissions() {
    return ajax('model/transmission.do');
}

function ajaxProductionYears() {
    return ajax('model/productionYear.do');
}

function ajaxAdvertisement() {
    let params = new URLSearchParams(document.location.search.substring(1));
    let id = +params.get("id");
    return ajax('model/advertisement.do?id=' + id);
}

function ajax(path) {
    return $.ajax({
        type: 'GET',
        url: cPath + path,
        dataType: 'json'
    }).fail(function (err) {
        console.log(err);
    });
}

function displaySpecifications(name, data) {
    let select = $("#" + name);
    select.empty();
    for (let d of data) {
        select.append($('<option>', {
            value: d.id,
            text: d.name
        }));
    }
}

function displayProductionYears(prodYears) {
    let select = $("#prodYear");
    select.empty();
    for (let prodYear of prodYears) {
        select.append($('<option>', {
            value: prodYear.year,
            text: prodYear.year
        }));
    }
}

function displayAdvertisement() {
    if (lAdvertisement === null) {
        $('#photo').prop('src', cPath + "model/advertisement/downloadPhoto.do");
        return;
    }
    $('#title').text('Изменение объявления');
    $('#heading').text('Изменение объявления');
    $('#createdFormGroup').removeAttr('hidden');
    $('#soldFormGroup').removeAttr('hidden');
    $('#btnDelete').removeAttr('hidden');
    $('#created').text(getFormattedDate(new Date(lAdvertisement.created)));
    $('#sold').prop('checked', lAdvertisement.sold);
    $('#brand').val(lAdvertisement.model.brand.id);
    $('#model').val(lAdvertisement.model.id);
    $('#bodyType').val(lAdvertisement.bodyType.id);
    $('#transmission').val(lAdvertisement.transmission.id);
    $('#prodYear').val(lAdvertisement.productionYear);
    $('#mileage').val(lAdvertisement.mileage);
    $('#price').val(lAdvertisement.price);
    $('#photo').prop('src', cPath + "model/advertisement/downloadPhoto.do?id=" + lAdvertisement.id);
    $('#description').text(lAdvertisement.description);
}

function changePhoto(inputFile) {
    $('#photo').prop('src', window.URL.createObjectURL(inputFile.files[0]));
    if (lAdvertisement !== null) {
        needDeletePhoto = false;
    }
}

function clearPhoto() {
    $('#photo').prop('src', cPath + "model/advertisement/downloadPhoto.do");
    $('#selectPhoto').val(null);
    if (lAdvertisement !== null) {
        needDeletePhoto = true;
    }
}

function saveAdvertisement() {
    let notification = $('#notification');
    notification.text('');
    notification.prop("style", "color: red");
    $.ajax({
        type: 'POST',
        url: cPath + "model/advertisement.do",
        data: getJSONAdvertisement(),
        dataType: 'json'
    }).done(function(data) {
        if (data === null || data.id === 0) {
            notification.text('Не удалось сохранить объявление!');
            console.log(data);
            return;
        }
        $('#btnSave').prop('disabled', true);
        $('#btnDelete').prop('disabled', true);
        updatePhoto(data.id);
        notification.prop("style", "color: green");
        notification.text('Объявление сохранено успешно.'
            + ' Через несколько секунд вас перенаправит на страницу ваших объявлений');
        setTimeout(() => {window.location.href = cPath + 'my.do'}, 3000);
    }).fail(function(err) {
        notification.text('Не удалось сохранить объявление!');
        console.log(err);
    });
}

function deleteAdvertisement() {
    let notification = $('#notification');
    notification.text('');
    notification.prop("style", "color: red");
    $.ajax({
        type: 'POST',
        url: cPath + "model/advertisement/delete.do",
        data: JSON.stringify({
            id: lAdvertisement.id
        }),
        dataType: 'text'
    }).done(function(data) {
        if (data !== '200 OK') {
            notification.text('Не удалось удалить объявление!');
            console.log(data);
            return;
        }
        $('#btnSave').prop('disabled', true);
        $('#btnDelete').prop('disabled', true);
        needDeletePhoto = true;
        deletePhoto(lAdvertisement.id);
        notification.prop("style", "color: green");
        notification.text('Объявление удалено успешно.'
            + ' Через несколько секунд вас перенаправит на страницу ваших объявлений');
        setTimeout(() => {window.location.href = cPath + 'my.do'}, 3000);
    }).fail(function(err) {
        notification.text('Не удалось удалить объявление!');
        console.log(err);
    });
}

function getJSONAdvertisement() {
    return JSON.stringify({
        id: lAdvertisement === null ? 0 : lAdvertisement.id,
        description: $('#description').val(),
        created: lAdvertisement === null ? new Date().toISOString() : lAdvertisement.created,
        sold: $('#sold').prop('checked'),
        photo: $('#selectPhoto').prop('files').length > 0 && !needDeletePhoto,
        mileage: $('#mileage').val(),
        price: $('#price').val(),
        productionYear: $('#prodYear').val(),
        model: {
            id: $('#model').val(),
            brand: {
                id: $('#brand').val()
            }
        },
        bodyType: {
            id: $('#bodyType').val()
        },
        transmission: {
            id: $('#transmission').val()
        },
        author: {
            id: lUser.id
        }
    });
}

function updatePhoto(id) {
    if (deletePhoto(id)) {
        return;
    }
    let files = $('#selectPhoto').prop('files');
    if (files.length > 0) {
        let formData = new FormData();
        formData.append("file", files[0]);
        fetch(cPath + 'model/advertisement/uploadPhoto.do?id=' + id, {
            method: "POST",
            body: formData
        });
    }
}

function deletePhoto(id) {
    if (needDeletePhoto) {
        fetch(cPath + 'model/advertisement/deletePhoto.do?id=' + id);
        return true;
    }
    return false;
}

function goToMainPage() {
    window.location.href = cPath + 'index.html';
}

function changeBrand() {
    ajaxModels(true).then(function (result) {
        displaySpecifications('model', result);
    });
}
