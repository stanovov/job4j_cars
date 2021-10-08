$(document).ready(function () {
    initMasks();
    $.ajax({
        type: 'GET',
        url: cPath + 'auth/login.do',
        dataType: 'json'
    }).done(function (data) {
        $('#email').val(data.email);
        $('#password').val(data.password);
    }).fail(function (err) {
        console.log(err);
    });
});

function initMasks() {
    $(":input").inputmask();
}

function login() {
    $('#notification').text('');
    $.ajax({
        type: 'POST',
        url: cPath + 'auth/login.do',
        data: JSON.stringify({
            email: $('#email').val(),
            password: $('#password').val()
        }),
        dataType: 'text'
    }).done(function(data) {
        if (data === '200 OK') {
            window.location.href = cPath + 'index.html';
        } else {
            $('#notification').text('Неверный email или пароль');
            console.log(data);
        }
    }).fail(function(err) {
        console.log(err);
    });
}

function goToRegistrationPage() {
    window.location.href = cPath + 'reg.html';
}