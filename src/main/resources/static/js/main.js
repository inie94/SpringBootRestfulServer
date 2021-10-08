'use strict';
var authorizedUser = null;
var userTopics = null;
var chatsList = document.querySelector('#chats-list');
var scrollChatBody = document.querySelector('#scroll-content-body');
var logoutForm = document.querySelector('#logout-form');

let messages = [];
var newMessages = []

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
var currentTopic = null;

var searchReturned;
var searchInput = document.querySelector('#searchInput');

var csrfToken = document.querySelector('#csrf').getAttribute('content');
var csrfHeader = document.querySelector('#csrf_header').getAttribute('content');

var chatsListViews;

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
    stompClient.send("/app/chat.connect",
        {},
        JSON.stringify({
            type: 'JOIN',
            topic: null,
            sender: authorizedUser,
            content: authorizedUser.id
        })
    )

    if (authorizedUser.relationships) {
        authorizedUser.relationships.forEach(relationship => {
            messages[relationship.topic.id] = '';
            newMessages[relationship.topic.id] = 0;
            subscribeOnTopic(relationship.topic.id);
            fetch('/user/topic/id:'+ relationship.topic.id + '/messages')
                .then(response => response.json())
                .then(data => {
                    data.forEach(message => {
                        getMessage(relationship, message);
                    });
                    viewUserTopics();
                });
        });
    }
}

function getMessage(relationship, message) {
    chekOnNewDate(relationship.topic.id, message.createdBy);
    messages[relationship.topic.id] += createMessageContainer(message);
    if (authorizedUser.id !== message.sender.id && relationship.updatedBy < message.createdBy){
        if (newMessages[message.topic.id] == 99) {
            newMessages[message.topic.id] = '99+';
        } else {
            newMessages[message.topic.id]++;
        }
    }
}

function viewUserTopics() {
    var element = '';
    authorizedUser.relationships.sort(
                function(a, b) {
                    return b.topic.updatedBy - a.topic.updatedBy;
                }
            );
    authorizedUser.relationships.forEach(relationship => {
        if (relationship.status === 'SUBSCRIBE') {
            if (relationship.topic.mode === 'PRIVATE') {
                element += generateTopicCard(relationship.topic);
            } else {
                /* View PUBLIC topics */
            }
        }
    });
    chatsListViews = element;
    chatsList.innerHTML = chatsListViews;
}

function generateTopicCard(topic) {
    var companion = topic.relationships.filter(relationship => isNotAuthorisedUser(relationship.user))[0].user;
    var element = '<button onclick="choseTopic(' + topic.id + ')" id="container-user-' + companion.id + '" type="button" class="btn btn-dark p-3 container-fluid position-relative chat-button">' +
                   '<div class="row align-items-center">' +
                       '<div class="col-3 p-0 d-flex justify-content-center">' +
                           '<div class="col-">';
    if (companion.status === 'ONLINE') {
       element += '<span id="user-' + companion.id + '" class="position-absolute p-2 bg-success border border-light rounded-circle"></span>';
    } else {
       element += '<span id="user-' + companion.id + '" class="position-absolute p-2 bg-success border border-light rounded-circle invisible"></span>';
    }
    element += '</div>' +
               '<img src="https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8cG9ydHJhaXR8ZW58MHx8MHx8&ixlib=rb-1.2.1&w=1000&q=80"' +
                    'class="img-fluid rounded-circle border border-light"' +
                    'alt="https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8cG9ydHJhaXR8ZW58MHx8MHx8&ixlib=rb-1.2.1&w=1000&q=80"' +
                    'style="width: 60px; height: 60px;">' +
           '</div>' +
           '<div class="col-9 text-light">' +
               '<h6 align="left" class="card-title text-white">' +
                   companion.firstname + ' ' + companion.lastname +
               '</h6>' +
               '<div class="row">' +
                   '<div class="col-9">' +
                       '<p align="left" class="card-text text-white text-muted text-truncate">' +
                           companion.email +
                       '</p>' +
                   '</div>';
    if (newMessages[topic.id] > 0) {
        element += '<div class="col-3" id="span-container-user-' + companion.id + '">';
    } else {
        element += '<div class="col-3 invisible" id="span-container-user-' + companion.id + '">';
    }
    return element + '<span class="position-absolute m-2 end-0 badge rounded-pill bg-danger" id="span-context-user-' + companion.id + '">' +
                           newMessages[topic.id] +
                       '</span>' +
                   '</div>' +
               '</div>' +
               '</div>' +
           '</div>' +
       '</button>';
}

function isNotAuthorisedUser(user) {
     return user.id !== authorizedUser.id;
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

function generateUserCard(user) {
    var element =   '<button onclick="getTopicFromUserID(' + user.id + ')" type="button" class="btn btn-dark p-3 container-fluid">' +
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
                '<div class="row">' +
                    '<div class="col-9">' +
                        '<p align="left" class="card-text text-white text-muted text-truncate">' +
                            user.email +
                        '</p>' +
                    '</div>' +
                    '<div class="col-3 invisible" id="span-container-user-' + user.id + '">' +
                        '<span class="badge rounded-pill bg-danger" id="span-context-user-' + user.id + '">' +
                            '99+' +
                        '<span class="visually-hidden">unread messages</span>' +
                        '</span>' +
                    '</div>' +
                '</div>' +
                '</div>' +
            '</div>' +
        '</button>';
}

function getTopicFromUserID(id) {
    fetch('/user/id:' + id + '/get-topic')
        .then(response => response.json())
        .then(data => {
            preparingToConnect(data);
            choseTopic(data.topic.id);
        });
}

function preparingToConnect(someRelationship) {
    if (authorizedUser.relationships.filter(relationship => relationship.id == someRelationship.id)[0] == null) {
        authorizedUser.relationships.push(someRelationship);
    } else {
        var item = authorizedUser.relationships.filter(relationship => relationship.id == someRelationship.id)[0];
        var index = authorizedUser.relationships.indexOf(item);
        authorizedUser.relationships[index] = someRelationship;
    }
    messages[someRelationship.topic.id] = '';
    newMessages[someRelationship.topic.id] = 0;
    subscribeOnTopic(someRelationship.topic.id);
    fetch('/user/topic/id:'+ someRelationship.topic.id + '/messages')
        .then(response => response.json())
        .then(data => {
            data.forEach(message => {
                getMessage(someRelationship, message);
            });
            viewUserTopics();
        });
}

function choseTopic(topicId) {
    clearSearchInput();
    chat.classList.remove('invisible');

    currentTopic = authorizedUser.relationships.filter(relationship => relationship.topic.id === topicId)[0].topic;

    var companion = currentTopic.relationships.filter(relationship => isNotAuthorisedUser(relationship.user))[0].user;

    var newMessageBullet = document.querySelector('#span-container-user-' + companion.id);
    if(newMessageBullet != null && !newMessageBullet.classList.contains('invisible')){
        newMessageBullet.classList.add('invisible');
    }

    chatHeader.innerHTML = companion.firstname + ' ' + companion.lastname;

    fetch('/user/topic/id:' + topicId + '/messages/received')
        .then(response => response.json())
        .then(data => {
            authorizedUser.relationships.forEach(relationship => {
                if(relationship.id === data.id) {
                    relationship = data;
                }
            })
            newMessages[topicId] = 0;
            reloadChatBody(topicId);
        });

}

function clearSearchInput() {
    searchInput.value = '';
    searchInput.blur();
    viewUserTopics();
}

function subscribeOnTopic(topicId) {
    // Subscribe to the Topic and add to subscriptions
    subscriptions[topicId] = stompClient.subscribe(('/topic/id:' + topicId), onMessageReceived);
}

//function sendSubscribeMessage() {
//    if(stompClient) {
//        var subscribeMessage = {
//            topic: currentTopic,
//            sender: authorizedUser,
//            content: messageInput.value,
//            type: 'CHAT',
//            createdBy: Date.now()
//        };
//        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(subscribeMessage));
//        messageInput.value = '';
//    }
//}

function onError(error) {
    connectingElement.innerHTML = 'Could not connect to WebSocket server. Please refresh this page to try again!';
//    connectingElement.style.color = 'red';
}

function sendNotification(recipient, recipientRelationship) {
    stompClient.send("/app/chat.notification",
        {},
        JSON.stringify({
            type: 'AWAIT_CONNECTION',
            topic: recipientRelationship.topic,
            sender: authorizedUser,
            content:
                JSON.stringify({
                    relationship: recipientRelationship,
                    user: recipient
                })
        })
    );
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            topic: currentTopic,
            sender: authorizedUser,
            content: messageInput.value,
            type: 'CHAT'
        };
        if (currentTopic.mode == 'PRIVATE') {
            var relationship = currentTopic.relationships.filter(relationship => isNotAuthorisedUser(relationship.user))[0];
            if(relationship.status === 'UNSUBSCRIBE'){
                var recipient = relationship.user;
                fetch('/user/id:' + recipient.id + '/topic:' + currentTopic.id + '/subscribe')
                    .then(response => response.json())
                    .then(data => {
                        sendNotification(recipient, data);
                        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
                    });
            } else {
                stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            }
        } else {
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        }

        messageInput.value = '';


    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    if(message.type === 'AWAIT_CONNECTION') {
        var awaitConnectionMessageContent = JSON.parse(message.content);
        var recipient = awaitConnectionMessageContent.user;
        if (recipient.id === authorizedUser.id) {
            var rlsp = awaitConnectionMessageContent.relationship;
            if(authorizedUser.relationships.filter(relationship => relationship.topic.id === rlsp.topic.id)[0] != null) {
                var item = authorizedUser.relationships.filter(relationship => relationship.topic.id === rlsp.topic.id)[0];
                var index = authorizedUser.relationships.indexOf(item);
                authorizedUser.relationships[index] = rlsp;
            } else {
                authorizedUser.relationships.push(rlsp);
            }
            messages[rlsp.topic.id] = '';
            if (!newMessages[rlsp.topic.id]) {
                newMessages[rlsp.topic.id] = 0;
            }
            subscribeOnTopic(rlsp.topic.id);
            fetch('/user/topic/id:'+ rlsp.topic.id + '/messages')
               .then(response => response.json())
               .then(data => {
                   data.forEach(message => {
                       getMessage(rlsp, message);
                   });
                   viewUserTopics();
               });

        } else if (message.sender.id === authorizedUser.id && recipient.status === 'OFFLINE') {
            fetch('/user/id:' + recipient.id + '/topic:' + message.topic.id + '/subscribe')
                .then(response => response.json())
                .then(data => {});
        }
    } else if (message.type === 'CHAT') {
        chekOnNewDate(message.topic.id, message.createdBy);
        messages[message.topic.id] += createMessageContainer(message);
        authorizedUser.relationships.filter(relationship => relationship.topic.id === message.topic.id)[0].topic.updatedBy = message.topic.updatedBy;
        if (!currentTopic || (currentTopic.id !== message.topic.id)) {
            if (newMessages[message.topic.id] == 99) {
                newMessages[message.topic.id] = '99+';
            } else {
                newMessages[message.topic.id]++;
            }
        } else {
            reloadChatBody(message.topic.id);
        }
        viewUserTopics();
    } else {
        authorizedUser.relationships.forEach(relationship => {
            if (relationship.topic.mode === 'PRIVATE' && relationship.status === 'SUBSCRIBE') {
                var companion = relationship.topic.relationships.filter(relationship => isNotAuthorisedUser(relationship.user))[0].user;
                if (companion != null && companion.id === message.sender.id) {
                    if(message.type === 'JOIN') {
                        if(message.sender.id !== authorizedUser.id) {
                            document.querySelector('#user-' + message.sender.id).classList.remove('invisible');
                        }
                    } else if (message.type === 'LEAVE') {
                        if(message.sender.id !== authorizedUser.id) {
                            document.querySelector('#user-' + message.sender.id).classList.add('invisible');
                        }
                    }
                }
            }
        });
    }
}

function reloadChatBody(topicId) {
    chatBody.innerHTML = messages[topicId];
    scrollChatBody.scrollTop = scrollChatBody.scrollHeight;
}

function search(text) {
    if (text === '' || text === ' ') {
        viewUserTopics();
    } else {
        var element = '';
//        element += searchIntoUserTopics(text);
        fetch('/search?value=' + text)
            .then(response => response.json())
            .then(data => {
                element += searchHeader('Global search result: ');
                data.forEach(item => {
                    element += generateUserCard(item);
                });
                chatsList.innerHTML = element;
            });
    }
}
/* deprecated */
function searchIntoUserTopics(text) {
    var element = ''
    authorizedUser.channels.forEach(topic => {
        if (topic.mode === 'PRIVATE') {
            var companion = topic.subscribers.filter(isNotAuthorisedUser)[0];
            if (companion != null && (companion.email.startsWith(text) ||
                companion.firstname.startsWith(text)  ||
                companion.lastname.startsWith(text)) ) {

                element += generateTopicCard(topic);
            }
        } else {
            /* View PUBLIC topics */
        }
    });
    return element;
}

function searchHeader(headerText) {
    return '<div class="container-fluid bg-primary" style="--bs-bg-opacity: .08;">' +
              '<div class="row flex-column align-items-center p-1">' +
                  '<div class="text-muted">' + headerText + '</div>' +
              '</div>' +
           '</div>';
}

function logout(event) {
    fetch('/logout', {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        headers: {
            [csrfHeader] : csrfToken,
            'charset': 'UTF-8',
            'Content-Type': 'application/json',
        },
        redirect: 'follow', // manual, *follow, error
    })
    .then(response => {
            // HTTP 301 response
            // HOW CAN I FOLLOW THE HTTP REDIRECT RESPONSE?
            if (response.redirected) {
                window.location.href = response.url;
            }
        })
    .catch(function(err) {
        console.info(err + " url: " + url);
    });
}

function unsubscribeAtCurrentTopic() {
    fetch('/user/topic:' + currentTopic.id + '/unsubscribe')
        .then(response => response.json())
        .then(data => {
            console.log(data);
            var item = authorizedUser.relationships.filter(relationship => relationship.id == data.id)[0];
            var index = authorizedUser.relationships.indexOf(item);
            authorizedUser.relationships[index] = data;
            subscriptions[currentTopic.id].unsubscribe();
//            sendUnsubscribeMessage(currentTopic);
            currentTopic = null;
            messages[data.topic.id] = '';
            newMessages[data.topic.id] = 0;
            chat.classList.add('invisible');
            viewUserTopics();
        });
}

function sendUnsubscribeMessage(topic) {
    if(stompClient) {
        var unsubscribeMessage = {
            topic: topic,
            sender: authorizedUser,
            type: 'UNSUBSCRIBE',
            createdBy: Date.now()
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(unsubscribeMessage));
    }
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
