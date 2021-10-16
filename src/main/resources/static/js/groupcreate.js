'use strict';
var userSearchInput = document.querySelector('#user-search-input');
var userListOptions = document.querySelector('#user-list-options');

var searchResult = [];
var users = [];

function userSearch(value) {
    fetch('/search?value=' + value)
        .then(response => response.json())
        .then(data => {
            data.forEach(item => {
                if(searchResult.filter(user => user.id === item.id).length === 0){
                    searchResult.push(item);
                }
            });
            generateCards();
        });
}

function generateCards() {
    var element = '';
    searchResult.forEach(user => {
        element += '<option onclick="addUserToList()" value="' + user.firstname + ' ' + user.lastname + '"></option>';
    });
    userListOptions.innerHTML = element;
}

function addUserToList() {
    var user = searchResult.filter(item => (item.firstname + ' ' + item.lastname) == searchResultearchInput.value)[0];
    if(users.filter(item => user.id === item.id).length === 0){
        users.push(item);
    } else {
        var index = users.indexOf(user);
        users[index] = user;
    }
}

function createGroup() {
    // продолжить
}

userSearchInput.oninput = function(event) {
            userListOptions.innerHTML = '';
            userSearch(userSearchInput.value);
        };