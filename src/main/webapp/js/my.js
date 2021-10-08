let user = null;

$(document).ready(function () {
    let deferreds = [];
    deferreds.push(getCurrentUser().then(function (data) {
        user = data;
        drawNavMenu(user);
    }));
    displayCustomFilters(deferreds);
    $.when.apply($, deferreds).done(function () {
        findAdvertisements();
    });
});

function displayCustomFilters(deferreds) {
    deferreds.push(displaySortingTypeFilter());
    deferreds.push(displayPeriodFilter());
    deferreds.push(displayStatusFilter());
}

function displaySortingTypeFilter() {
    let select = $("#sortingType");
    select.empty();
    return $.ajax({
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
    return $.ajax({
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

function displayStatusFilter() {
    let select = $("#statusFilter");
    select.empty();
    return $.ajax({
        type: 'GET',
        url: cPath + 'model/statusFilter.do',
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
    let periodFilter = $('#periodFilter').find(':selected').val();
    arrParams.push('periodFilter=' + periodFilter);
    let sortingType = $('#sortingType').find(':selected').val();
    arrParams.push('sortingType=' + sortingType);
    let statusFilter = $('#statusFilter').find(':selected').val();
    arrParams.push('statusFilter=' + statusFilter);
    arrParams.push('author=' + user.id);
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
    let row = `<tr>`
        + `<td><img class="rounded float-left d-block" src="${imgSrc}" width="320px" height="180px"></td>`
        + `<td class="align-middle" style="width: 35%"><strong>${name}</strong><br><br>${bodyType}<br><br>${transmission}</td>`
        + `<td class="align-middle" style="width: 35%"><strong>${price}</strong><br><br>${year}<br><br>${mileage}</td>`
        + `<td class="align-top" style="width: 20%"><br>${formattedDate}<br><br><button type="button" class="btn btn-warning" onclick="goToAdvertisementPage(${id})">Редактировать</button></td>`
        + `</tr>`;
    $('#table tbody').append(row);
}

function goToAdvertisementPage(id) {
    window.location.href = cPath + 'edit.do?id=' + id;
}