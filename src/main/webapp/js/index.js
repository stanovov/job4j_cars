let user = null;

$(document).ready(function () {
    getCurrentUser().then(function (data) {
        user = data;
        drawNavMenu(user);
    });
    displayCustomFilters();
});

function displayCustomFilters() {
    displayBrandFilter();
    displayModelFilter();
    displayBodyTypeFilter();
    displayTransmissionFilter();
    displayProductionYear();
    displaySortingTypeFilter();
    displayPeriodFilter();
}

function displayBrandFilter() {
    let select = $("#brand");
    select.empty();
    select.append($('<option>', {
        value: 0,
        text: 'Любая',
        selected: true
    }));
    $.ajax({
        type: 'GET',
        url: cPath + 'model/brand.do',
        dataType: 'json'
    }).done(function (data) {
        for (let filter of data) {
            select.append($('<option>', {
                value: filter.id,
                text: filter.name
            }));
        }
    }).fail(function (err) {
        console.log(err);
    });
}

function displayModelFilter() {
    let select = $("#model");
    select.empty();
    select.attr('disabled', true);
    select.append($('<option>', {
        value: 0,
        text: 'Любая',
        selected: true
    }));
    if ($('#brand').val() > 0) {
        select.attr('disabled', false);
        $.ajax({
            type: 'GET',
            url: cPath + 'model/model.do?brandId=' + $('#brand').val(),
            dataType: 'json'
        }).done(function (data) {
            for (let filter of data) {
                select.append($('<option>', {
                    value: filter.id,
                    text: filter.name
                }));
            }
        }).fail(function (err) {
            console.log(err);
        });
    }
}

function displayBodyTypeFilter() {
    let select = $("#bodyType");
    select.empty();
    select.append($('<option>', {
        value: 0,
        text: 'Любой',
        selected: true
    }));
    $.ajax({
        type: 'GET',
        url: cPath + 'model/bodyType.do',
        dataType: 'json'
    }).done(function (data) {
        for (let filter of data) {
            select.append($('<option>', {
                value: filter.id,
                text: filter.name
            }));
        }
    }).fail(function (err) {
        console.log(err);
    });
}

function displayTransmissionFilter() {
    let select = $("#transmission");
    select.empty();
    select.append($('<option>', {
        value: 0,
        text: 'Любая',
        selected: true
    }));
    $.ajax({
        type: 'GET',
        url: cPath + 'model/transmission.do',
        dataType: 'json'
    }).done(function (data) {
        for (let filter of data) {
            select.append($('<option>', {
                value: filter.id,
                text: filter.name
            }));
        }
    }).fail(function (err) {
        console.log(err);
    });
}

function displayProductionYear() {
    let selectSt = $("#stProdYear");
    selectSt.empty();
    selectSt.append($('<option>', {
        value: 0,
        text: 'Не выбран',
        selected: true
    }));
    let selectEnd = $("#endProdYear");
    selectEnd.empty();
    selectEnd.append($('<option>', {
        value: 0,
        text: 'Не выбран',
        selected: true
    }));
    $.ajax({
        type: 'GET',
        url: cPath + 'model/productionYear.do',
        dataType: 'json'
    }).done(function (data) {
        for (let filter of data) {
            selectSt.append($('<option>', {
                value: filter.year,
                text: filter.year
            }));
            selectEnd.append($('<option>', {
                value: filter.year,
                text: filter.year
            }));
        }
    }).fail(function (err) {
        console.log(err);
    });
}

function displaySortingTypeFilter() {
    let select = $("#sortingType");
    select.empty();
    $.ajax({
        type: 'GET',
        url: cPath + 'model/sortingType.do',
        dataType: 'json'
    }).done(function (data) {
        for (let filter of data) {
            select.append($('<option>', {
                value: filter.id,
                text: filter.name
            }));
        }
    }).fail(function (err) {
        console.log(err);
    });
}

function displayPeriodFilter() {
    let select = $("#periodFilter");
    select.empty();
    $.ajax({
        type: 'GET',
        url: cPath + 'model/periodFilter.do',
        dataType: 'json'
    }).done(function (data) {
        for (let filter of data) {
            select.append($('<option>', {
                value: filter.id,
                text: filter.name
            }));
        }
    }).fail(function (err) {
        console.log(err);
    });
}

function findAdvertisements() {
    $("#tbody").empty();
    let arrParams = [];
    let brand = $('#brand').find(':selected').val();
    if (brand !== '0') {
        arrParams.push('brand=' + brand);
    }
    let model = $('#model').find(':selected').val();
    if (model !== '0') {
        arrParams.push('model=' + model);
    }
    let bodyType = $('#bodyType').find(':selected').val();
    if (bodyType !== '0') {
        arrParams.push('bodyType=' + bodyType);
    }
    let transmission = $('#transmission').find(':selected').val();
    if (transmission !== '0') {
        arrParams.push('transmission=' + transmission);
    }
    let stProdYear = $('#stProdYear').find(':selected').val();
    if (stProdYear !== '0') {
        arrParams.push('stProdYear=' + stProdYear);
    }
    let endProdYear = $('#endProdYear').find(':selected').val();
    if (endProdYear !== '0') {
        arrParams.push('endProdYear=' + endProdYear);
    }
    let stMileage = $('#stMileage').val();
    if (stMileage !== '') {
        arrParams.push('stMileage=' + stMileage);
    }
    let endMileage = $('#endMileage').val();
    if (endMileage !== '') {
        arrParams.push('endMileage=' + endMileage);
    }
    let stPrice = $('#stPrice').val();
    if (stPrice !== '') {
        arrParams.push('stPrice=' + stPrice);
    }
    let endPrice = $('#endPrice').val();
    if (endPrice !== '') {
        arrParams.push('endPrice=' + endPrice);
    }
    let photo = $('#photo').is(":checked");
    if (photo === true) {
        arrParams.push('photo=' + photo);
    }
    let periodFilter = $('#periodFilter').find(':selected').val();
    arrParams.push('periodFilter=' + periodFilter);
    let sortingType = $('#sortingType').find(':selected').val();
    arrParams.push('sortingType=' + sortingType);
    arrParams.push('sold=false');
    let params = '?' + arrParams.join('&');
    $.ajax({
        type: 'GET',
        url: cPath + 'model/advertisement.do' + params,
        dataType: 'json'
    }).done(function (data) {
        for (let advertisement of data) {
            displayAdvertisement(advertisement);
        }
    }).fail(function (err) {
        console.log(err);
    });
}

function displayAdvertisement(advertisement) {
    const id = advertisement.id;
    const formattedDate = getShortFormattedDate(new Date(advertisement.created));
    const imgSrc = cPath + "model/advertisement/downloadPhoto.do?id=" + advertisement.id;
    const name = advertisement.model.brand.name + ', ' + advertisement.model.name;
    const bodyType = advertisement.bodyType.name;
    const transmission = advertisement.transmission.name;
    const price = advertisement.price.toLocaleString() + ' р.';
    const year = advertisement.productionYear + ' г.';
    const mileage = advertisement.mileage.toLocaleString() + ' км';
    const authorName = advertisement.author.firstName;
    const authorPhone = advertisement.author.phone;
    const authorEmail = advertisement.author.email;
    let btnName = 'Перейти';
    let btnColor = 'btn-success';
    if (user !== null && user.id === advertisement.author.id) {
        btnName = 'Редактировать';
        btnColor = 'btn-warning';
    }
    let row = `<tr>`
        + `<td><img class="rounded float-left d-block" src="${imgSrc}" width="320px" height="180px"></td>`
        + `<td class="align-middle" style="width: 20%"><strong>${name}</strong><br><br>${bodyType}<br><br>${transmission}</td>`
        + `<td class="align-middle" style="width: 15%"><strong>${price}</strong><br><br>${year}<br><br>${mileage}</td>`
        + `<td class="align-middle" style="width: 20%">${authorName}<br><br>${authorPhone}<br><br>${authorEmail}</td>`
        + `<td class="align-top" style="width: 18%"><br>${formattedDate}<br><br><button type="button" class="btn ${btnColor}" onclick="goToAdvertisementPage(${id})">${btnName}</button></td>`
        + `</tr>`;
    $('#table tbody').append(row);
}

function goToAdvertisementPage(id) {
    window.location.href = cPath + 'readonly.do?id=' + id;
}
