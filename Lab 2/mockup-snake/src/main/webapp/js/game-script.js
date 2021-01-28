/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


class Snake{
    constructor(){
        this.x = 0;
        this.y = 0;
        this.body = new Array();
        this.body.unshift({x:0, y:0});
        this.body.push({x:0, y:0});
        this.body.push({x:0, y:0});
        this.speed = 10;
        this.tempXDirection = 1;
        this.tempYDirection = 0;
    }
    
    move(){
        this.xDirection = this.tempXDirection;
        this.yDirection = this.tempYDirection;
        this.body.unshift({x: this.body[0].x + this.xDirection * this.speed, y: this.body[0].y + this.yDirection * this.speed});
        this.body.pop();
    }
    
//    draw(ctx){
//        for(let i = 0; i < this.body.length; i++){
//            console.log(this.body[i].x + "  " + this.body[i].y)
//            ctx.fillRect(this.body[i].x, this.body[i].y, 10, 10);
//            ctx.stroke();
//        }
//    }
    
    eatFruit(){
        this.body.push({x:this.body[0].x, y:this.body[0].y});
    }
}


function setDirection(direction){
    switch(direction){
        case "up":
            //if(this.yDirection == 0){
                xDir = 0;
                yDir = -1;
            //}
            break;
        case "left":
            //if(this.xDirection == 0){
                xDir = -1;
                yDir = 0;
            //}
            break;
        case "down":
            //if(this.yDirection == 0){
                xDir = 0;
                yDir = 1;
            //}
            break;
        case "right":
            //if(this.xDirection == 0){
                xDir = 1;
                yDir = 0;
            //}
            break;
    }
    sendJson();
}

function sendJson(){
    var jsonString = JSON.stringify({
            "playername": playerName,
            "dirX": xDir,
            "dirY": yDir
        });
    sendText(jsonString);
}


let s;
let keys = new Array();
let fruitX = 10;
let fruitY = 10;
let score = 0;
let playerName = Math.floor(Math.random()*10000).toString();
$(document).ready(function(){
    
    $(document).keydown(function(event){
        keys[event.which] = true;
        console.log(event.which);
    });
    
    $(document).keyup(function(event){
        keys[event.which] = false;
    });
    
    
    s = new Snake();
    setInterval(keyEvents, 50);
    setInterval(draw, 100);
});

function eatFruit(){
    fruitX = Math.floor(Math.random() * 21)*10;
    fruitY = Math.floor(Math.random() * 21)*10;
}

function keyEvents(){
    
    //W
    if(keys[87]){
        setDirection("up");
    }
    
    //A
    if(keys[65]){
        setDirection("left");
    }
    
    //S
    if(keys[83]){
        setDirection("down");
    }
    
    //D
    if(keys[68]){
        setDirection("right");
    }
    
    
    
}

function draw(){
    var ctx;
    //console.log(snakes);
    try {
        ctx = $('#game-canvas').get(0).getContext('2d');
    } catch (e) {
        console.log('We have encountered an error: ' + e);
    }
    ctx.clearRect(0,0,1000,1000);
    for(var i = 0; i < snakes.length; i++) {
        let fruit = JSON.parse(snakes[i]).fruit;
        let snakeBody = JSON.parse(snakes[i]).body;
        ctx.fillStyle = "#FF0000";
        ctx.fillRect(fruit.x*10,fruit.y*10,10,10);
        ctx.fillStyle = "#00FF00";
        for(var j = 0; j < snakeBody.length; j++)
            
        //console.log(snakeBody[i].body[j].x + "  " +  snakeBody[i].body[j].y)
        ctx.fillRect(snakeBody[j].x*10,snakeBody[j].y*10,10,10);
    
    
    }
    ctx.stroke();
}