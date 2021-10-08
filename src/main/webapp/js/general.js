const cPath = 'http://localhost:8080/cars/';

function drawNavMenu(user) {
    let navigationMenu = $('#nav');
    navigationMenu.append(`<li class="nav-item"><a class="nav-link" href="${cPath + 'index.html'}">Главная</a></li>`);
    navigationMenu.append(`<li class="nav-item"><a class="nav-link" href="${cPath + 'edit.do'}">Добавить объявление</a></li>`);
    navigationMenu.append(`<li class="nav-item"><a class="nav-link" href="${cPath + 'my.do'}">Мои объявления</a></li>`);
    if (user === null) {
        navigationMenu.append(`<li class="nav-item"><a class="nav-link" href="${cPath + 'login.html'}">Войти</a></li>`);
    } else {
        navigationMenu.append(`<li class="nav-item"><a class="nav-link" href="#">${user.firstName + ' ' + user.lastName}</a></li>`);
        navigationMenu.append(`<li class="nav-item"><a class="nav-link" href="${cPath + 'auth/logout.do'}">Выход</a></li>`);
    }
}

function getCurrentUser() {
    return $.ajax({
        type: 'GET',
        url: cPath + 'auth/user.do',
        dataType: 'json'
    }).fail(function (err) {
        console.log(err);
    });
}

function getFormattedDate(date) {
    return date.getDate() + ' '
        + getMonth(date.getMonth()) + ' '
        + date.getFullYear() + ' г. '
        + ((date.getHours() < 10) ? '0' : '') + date.getHours() + ':'
        + ((date.getMinutes() < 10) ? '0' : '') + date.getMinutes();
}

function getShortFormattedDate(date) {
    return date.getDate() + ' '
        + getMonth(date.getMonth()) + ' '
        + date.getFullYear() + ' г.';
}

function getMonth(num) {
    switch (num) {
        case 0 :
            return 'января';
        case 1 :
            return 'февраля';
        case 2 :
            return 'марта';
        case 3 :
            return 'апреля';
        case 4 :
            return 'мая';
        case 5 :
            return 'июня';
        case 6 :
            return 'июля';
        case 7 :
            return 'августа';
        case 8 :
            return 'сентября';
        case 9 :
            return 'октября';
        case 10 :
            return 'ноября';
        case 11 :
            return 'декабря';
        default :
            return ''
    }
}

function negativeInputNumberControl(inputItem) {
    if (inputItem.value < 0) {
        inputItem.value = 0;
    }
}
