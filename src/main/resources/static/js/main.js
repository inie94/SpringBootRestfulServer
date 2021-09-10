'use strict';
var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#contentBlock');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#content');
var connectingElement = document.querySelector('.connecting');
var stompClient = null;
var username = 'josh';
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

fetch('/user')
  .then(
    function(response) {
      if (response.status !== 200) {
        console.log('Looks like there was a problem. Status Code: ' +
          response.status);
        return;
      }

      // Examine the text in the response
      response.json().then(function(data) {
        username = data.firstname;
      });
    }
  )
  .catch(function(err) {
    console.log('Fetch Error :-S', err);
  });

function connect(/*event*/) {
    if(username) {
//        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }
//    event.preventDefault();
}
function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);
    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )
    connectingElement.classList.add('hidden');
}
function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}
function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    if(message.type === 'JOIN') {
    var joinMessage = '<div class="row- py-2">' +
                        '<div class="card text-white border-white bg-transparent ">' +
                            '<div class="card-body d-flex justify-content-center">' +
                                '<p class="card-text">' +
                                    message.sender + ' joined!' +
                                '</p>' +
                            '</div>' +
                        '</div>' +
                    '</div>' ;

        messageArea.innerHTML = messageArea.innerHTML + joinMessage;
    } else if (message.type === 'LEAVE') {
        var joinMessage = '<div class="row- py-2">' +
                            '<div class="card text-danger border-danger bg-transparent ">' +
                                '<div class="card-body d-flex justify-content-center">' +
                                    '<p class="card-text">' +
                                        message.sender + ' left!' +
                                    '</p>' +
                                '</div>' +
                            '</div>' +
                        '</div>' ;

            messageArea.innerHTML = messageArea.innerHTML + joinMessage;
    } else {
        var messageTextContent = '<div class="row- py-2">' +
                                             '<div class="card text-dark bg-light" >' +
                                                 '<div class="card-header">' + message.sender + '</div>' +
                                                 '<div class="card-body">' +
                                                     '<p class="card-text">' +
                                                         message.content +
                                                     '</p>' +
                                                 '</div>' +
                                             '</div>' +
                                         '</div>';

        messageArea.innerHTML = messageArea.innerHTML + messageTextContent;
    }
    messageArea.scrollTop = messageArea.scrollHeight;
}
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

connect.call()
messageForm.addEventListener('submit', sendMessage, true)