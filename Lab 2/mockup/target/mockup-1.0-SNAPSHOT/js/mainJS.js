/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var dir = 0;
var speed = 10;
var left;
var right;
var snakeHeadX = 0;
var snakeHeadY = 0;

document.addEventListener('keydown', function(event) {
    if(event.keyCode == 37) {
        //alert('Left was pressed');
        left = true;
        snakeHeadX--;
        document.getElementById("id2").style.left = snakeHeadX + "px";
    }
    else if(event.keyCode == 39) {
        //alert('Right was pressed');
        right = true;
        snakeHeadX++;
        document.getElementById("id2").style.left = snakeHeadX + "px";
    }
    
});

window.onload = function () {
    var div = document.getElementById("id2");
    var div1 = document.getElementById("id1");
    var top = 10;
    div.onmouseover = function () {
        over = true;
        div.style.left = top + "px";
        top += speed;
    };
};