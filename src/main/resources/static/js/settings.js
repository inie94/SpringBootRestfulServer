'use strict';
//
var csrfToken = document.querySelector('#csrf').getAttribute('content');
var csrfHeader = document.querySelector('#csrf_header').getAttribute('content');

function updateProfile(event) {
    var profile = {
        firstname: document.querySelector('#firstname').value,
        lastname: document.querySelector('#lastname').value,
        gender: document.querySelector('#gender').value,
        dateOfBirth: document.querySelector('#date-of-birth').value
    };

    fetch('/edit-profile', {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        headers: {
            [csrfHeader] : csrfToken,
            'charset': 'UTF-8',
            'Content-Type': 'application/json'
        },
        redirect: 'follow', // manual, *follow, error
        body: JSON.stringify(profile) // body data type must match "Content-Type" header
    }).then(res => console.log(res)).catch(err => console.log(err));

}

function updateEmailOrPassword(event) {
    if (document.querySelector('#password').value !== document.querySelector('#repeat-password').value) {
        document.querySelector('#repeat-password-container').innerHTML = invalidPasswords();
    } else {
        var profile = {
                    email: document.querySelector('#email').value,
                    password: document.querySelector('#password').value
                };
        document.querySelector('#repeat-password-container').innerHTML = validPasswords();
        fetch('/edit-profile', {
            method: 'POST', // *GET, POST, PUT, DELETE, etc.
            headers: {
                [csrfHeader] : csrfToken,
                'charset': 'UTF-8',
                'Content-Type': 'application/json'
            },
            redirect: 'follow', // manual, *follow, error
            body: JSON.stringify(profile) // body data type must match "Content-Type" header
        }).then(res => console.log(res)).catch(err => console.log(err));
    }
}

function invalidPasswords() {
    return   '<div class="input-group mb-2">' +
                 '<input type="text" class="form-control is-invalid" id="password"' +
                        'aria-describedby="basic-addon3" placeholder="Password">' +
             '</div>' +
             '<div class="input-group mb-2">' +
                 '<input type="text" class="form-control is-invalid" id="repeat-password"' +
                        'aria-describedby="basic-addon3" placeholder="Repeat password">' +
                 '<div class="invalid-feedback">Passwords will not match!</div>' +
             '</div>';
}

function validPasswords() {
    return   '<div class="input-group mb-2">' +
                 '<input type="text" class="form-control" id="password"' +
                        'aria-describedby="basic-addon3" placeholder="Password">' +
             '</div>' +
             '<div class="input-group mb-2">' +
                 '<input type="text" class="form-control" id="repeat-password"' +
                        'aria-describedby="basic-addon3" placeholder="Repeat password">' +
             '</div>';
}

//profileForm.addEventListener('submit', updateProfile, true);
//emailPasswordForm.addEventListener('submit', updateEmailOrPassword, true);