'use strict';
var chats = document.getElementsByClassName("chat-button");

//var menu = document.querySelector("#context-menu");
var menuState = 0;
var active = "context-menu--active";

function addEventListenerToChatButtons() {
  for ( var i = 0, len = chats.length; i < len; i++ ) {
    var chat = chats[i];
    contextMenuListener(chat);
    toggleMenuOn();
  }
}

function contextMenuListener(el) {
    el.addEventListener( "contextmenu", function(e) {
        console.log(e, el);
    });
}

function toggleMenuOn() {
  if ( menuState !== 1 ) {
    menuState = 1;
//    menu.classList.add(active);
  }
}