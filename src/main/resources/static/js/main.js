'use strict';
var authorizedUser = null;
var userTopics = null;
var chatsList = document.querySelector('#chats-list');
var scrollChatBody = document.querySelector('#scroll-content-body');

let messages = [];

var subscriptions = [];
var loadedTopics = [];

var months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

var chat = document.querySelector('#content-block');
var chatHeader = document.querySelector('#content-header');
var chatBody = document.querySelector('#content-body');
var messageForm = document.querySelector('#message-form');
var messageInput = document.querySelector('#message');

var connectingElement = document.querySelector('#content-body');
var stompClient = null;
var dateBreakpoint = [];
var currentTopic;

var searchReturned;
var searchInput = document.querySelector('#searchInput');

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function initialization() {
    fetch('/user')
        .then(response => response.json())
        .then(data => {
            authorizedUser = data;
            connect();
        });
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    // Subscribe to the Notification Topic
    stompClient.subscribe('/topic/notification', onMessageReceived);
    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({
            type: 'JOIN',
            topic: null,
            sender: authorizedUser,
            content: authorizedUser.id
        })
    )

    fetch('/user/topics')
        .then(response => response.json())
        .then(data => {
            data.forEach(item => {
                loadedTopics[item.id] = item;
                messages[item.id] = '';
                fetch('/user/topic/id:'+ item.id + '/messages')
                        .then(response => response.json())
                        .then(data => {
                            data.forEach(message => {
                                chekOnNewDate(item.id, message.createdBy);
                                messages[item.id] += createMessageContainer(message);
                            });
                        });
                subscribeOnTopic(item.id);
            });
            viewUserTopics(data);
        });
}

function createMessageContainer(message) {
    var element = '';

    if (message.sender.id === authorizedUser.id) {
        element += '<div class="d-flex justify-content-end mb-3">';
    } else {
        element += '<div class="d-flex justify-content-start mb-3">';
    }
    var date = new Date(message.createdBy);
    return element +  '<div class="card col-8 ">' +
                          '<div class="card-body py-2">' +
                              '<div>' + message.content + '</div>' +
                              '<div class="text-end text-muted">' +
                                date.getHours() + ':' + date.getMinutes() +
                              '</div>' +
                          '</div>' +
                      '</div>' +
                  '</div>';
}

function createDateBreakpoint(date) {
    return '<div class="d-flex justify-content-center mb-3">' +
               '<div class="card border-light bg-transparent">' +
                   '<div class="card-body py-2">' +
                       '<div class="text-light">' +
                           date.getDate() + ' ' + months[date.getMonth()] + ' ' + date.getFullYear() +
                       '</div>' +
                   '</div>' +
               '</div>' +
           '</div>';
}

function chekOnNewDate(topicId, message_date) {
    var date = new Date(message_date);
    if (dateBreakpoint[topicId] === undefined) {
        messages[topicId] += createDateBreakpoint(date);
    } else {
        if ((date.getFullYear() != dateBreakpoint[topicId].getFullYear()) ||
            (date.getMonth() != dateBreakpoint[topicId].getMonth()) ||
            (date.getDate() != dateBreakpoint[topicId].getDate()))  {

            messages[topicId] += createDateBreakpoint(date);
        }
    }
    dateBreakpoint[topicId] = date;
}

function viewUserTopics(topics) {
    var element = '';
    topics.forEach(topic => {
        if (topic.status === 'PRIVATE') {
            topic.subscriber.forEach( subscriber => {
                if (subscriber.id !== authorizedUser.id) {
                    element += generateUserCard(subscriber, topic);
                }
            });
        } else {
            /* View PUBLIC topics */
        }
    });
    chatsList.innerHTML = element;
}

function generateUserCard(user, topic) {
    var element = '';
    if(topic === null) {
        element = '<button onclick="getTopicFromUserID(' + user.id + ')" type="button" class="btn btn-dark p-3 container-fluid">';
    } else {
        element = '<button onclick="preparingToConnect(' + topic.id + ')" type="button" class="btn btn-dark p-3 container-fluid">';
    }
    element +=
        '<div class="row align-items-center">' +
            '<div class="col-3 p-0 d-flex justify-content-center">' +
                '<div class="col-">';
    if (user.status === 'ONLINE') {
        element += '<span id="user-' + user.id + '" class="position-absolute p-2 bg-success border border-light rounded-circle"></span>';
    } else {
        element += '<span id="user-' + user.id + '" class="position-absolute p-2 bg-success border border-light rounded-circle invisible"></span>';
    }
    return element += '</div>' +
                '<img src="https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8cG9ydHJhaXR8ZW58MHx8MHx8&ixlib=rb-1.2.1&w=1000&q=80"' +
                     'class="img-fluid rounded-circle border border-light"' +
                     'alt="https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8cG9ydHJhaXR8ZW58MHx8MHx8&ixlib=rb-1.2.1&w=1000&q=80"' +
                     'style="width: 60px; height: 60px;">' +
            '</div>' +
            '<div class="col-9 text-light">' +
                '<h6 align="left" class="card-title text-white">' +
                    user.firstname + ' ' + user.lastname +
                '</h6>' +
                '<p align="left" class="card-text text-white text-muted text-truncate">' +
                    user.email +
                '</p>' +
                '</div>' +
            '</div>' +
        '</button>';
}

function getTopicFromUserID(id) {
    fetch('/user/id:' + id + '/get-topic')
        .then(response => response.json())
        .then(data => {
            loadedTopics[data.id] = data;
            preparingToConnect(data.id);
        });
}

function preparingToConnect(topicId) {
    clearSearchInput();
    chat.classList.remove('invisible');

    currentTopic = loadedTopics[topicId];

    if (subscriptions[topicId] !== true) {
        messages[topicId] = '';
        subscribeOnTopic(topicId);
    }

    currentTopic.subscriber.forEach(subscriber => {
        if(subscriber.id !== authorizedUser.id) {
            chatHeader.innerHTML = subscriber.firstname + ' ' + subscriber.lastname;
        }
    });

    reloadChatBody(messages[topicId], currentTopic);
}

function clearSearchInput() {
    searchInput.value = '';
    searchInput.blur();
    fetch('/user/topics')
        .then(response => response.json())
        .then(data => {
            viewUserTopics(data);
        });
}

function subscribeOnTopic(topicId) {
    // Subscribe to the Topic
    stompClient.subscribe(('/topic/id:' + topicId), onMessageReceived);
    // Add subscribe on topic to subscriptions
    subscriptions[topicId] = true;
}

function onError(error) {
    connectingElement.innerHTML = 'Could not connect to WebSocket server. Please refresh this page to try again!';
//    connectingElement.style.color = 'red';
}
function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            topic: currentTopic,
            sender: authorizedUser,
            content: messageInput.value,
            type: 'CHAT',
            createdBy: new Date()
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    if(message.type === 'JOIN') {
        if(message.sender.id !== authorizedUser.id) {
            document.querySelector('#user-' + message.sender.id).classList.remove('invisible');
        }
    } else if (message.type === 'LEAVE') {
        if(message.sender.id !== authorizedUser.id) {
            document.querySelector('#user-' + message.sender.id).classList.add('invisible');
        }
    } else {
        chekOnNewDate(message.topic.id, message.createdBy);
        messages[message.topic.id] += createMessageContainer(message);
        reloadChatBody(messages[message.topic.id], message.topic);
    }
}

function reloadChatBody(messagesCurr, topic) {
    if (topic.id === currentTopic.id) {
        chatBody.innerHTML = messagesCurr;
        scrollChatBody.scrollTop = scrollChatBody.scrollHeight;
    }
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

function search(text) {
    if (text === '' || text === ' ') {
        fetch('/user/topics')
            .then(response => response.json())
            .then(data => {
                viewUserTopics(data);
            });
    } else {
        fetch('/search?value=' + text)
            .then(response => response.json())
            .then(data => {
                var element = searchHeader('Global search result: ');
                data.forEach( item => {
                    element += generateUserCard(item, null);
                });
                chatsList.innerHTML = element;
            });
    }
}

function searchHeader(headerText) {
    return '<div class="container-fluid bg-primary" style="--bs-bg-opacity: .08;">' +
              '<div class="row flex-column align-items-center p-1">' +
                  '<div class="text-muted">' + headerText + '</div>' +
              '</div>' +
           '</div>';
}

initialization.call();

searchInput.onkeydown = function(event) {
            if (event.keyCode == 27 || event.key == 27) {
                clearSearchInput();
            }
        };
searchInput.oninput = function(event) {
            chatsList.innerHTML = '';
            search(searchInput.value);
        };

messageForm.addEventListener('submit', sendMessage, true);
