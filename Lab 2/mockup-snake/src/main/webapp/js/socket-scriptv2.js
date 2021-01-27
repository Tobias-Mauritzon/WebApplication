/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var wsUri = "ws://localhost:8080/mockup-snake/endpoint";//"ws://" + document.location.host + document.location.pathname + "endpoint";
console.log(wsUri);
var websocket = new WebSocket(wsUri);

var snakeBody;

websocket.onmessage = function(evt) { onMessage(evt); };

function sendText(json) {
    console.log("sending text: " + json);
    websocket.send(json);
}
                
function onMessage(evt) {
    //var jsonTest = JSON.parse(evt.data);
    //console.log("received: " + evt.data);
    //{"playerName":"Lerbyn","x":8,"y":16,"xDir":1,"yDir":0,"hasEaten":false,"dead":false,"body":[{"x":8,"y":16}]}
    //var body = JSON.parse(evt.data).body;
    snakeBody = JSON.parse(evt.data).body;
    //console.log(test);
    
}

websocket.onerror = function (evt) {
    onError(evt);
};

function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

// For testing purposes

websocket.onopen = function(evt) { onOpen(evt); };

function writeToScreen(message) {
    console.log(message);
}

function onOpen() {
    writeToScreen("Connected to " + wsUri);
}
// End test functions
