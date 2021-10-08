$(document).ready(function () {
    initMasks();
});

function initMasks() {
    $(":input").inputmask();
    $("#phone").inputmask({
        mask: '+7 (999) 999-99-99',
        placeholder: ' ',
        showMaskOnHover: false,
        showMaskOnFocus: false,
        onBeforePaste: function (pastedValue, opts) {
            var processedValue = pastedValue;
            return processedValue;
        }
    });
}

function signUp() {
    $('#notification').text('');
    if ($('#password').val() !== $('#repeatPassword').val()) {
        $('#notification').text('Пароли должны совпадать');
        return;
    }
    $.ajax({
        type: 'POST',
        url: cPath + 'auth/reg.do',
        data: JSON.stringify({
            email: $('#email').val(),
            password: $('#password').val(),
            firstName: $('#firstName').val(),
            lastName: $('#lastName').val(),
            phone: $('#phone').val()
        }),
        dataType: 'text'
    }).done(function(data) {
        if (data === '200 OK') {
            window.location.href = cPath + 'login.html';
        } else {
            $('#notification').text('Пользователь с такой почтой уже существует');
            console.log(data);
        }
    }).fail(function(err) {
        console.log(err);
    });
}

function Cancel() {
    window.location.href = cPath + 'login.html';
}

