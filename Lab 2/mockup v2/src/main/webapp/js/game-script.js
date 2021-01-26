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
    
    draw(ctx){
        for(let i = 0; i < this.body.length; i++){
            ctx.fillRect(this.body[i].x, this.body[i].y, 10, 10);
            ctx.stroke();
        }
    }
    
    setDirection(direction){
        switch(direction){
            case "up":
                if(this.yDirection == 0){
                    this.tempXDirection = 0;
                    this.tempYDirection = -1;
                }
                break;
            case "left":
                if(this.xDirection == 0){
                    this.tempXDirection = -1;
                    this.tempYDirection = 0;
                }
                break;
            case "down":
                if(this.yDirection == 0){
                    this.tempXDirection = 0;
                    this.tempYDirection = 1;
                }
                break;
            case "right":
                if(this.xDirection == 0){
                    this.tempXDirection = 1;
                    this.tempYDirection = 0;
                }
                break;
        }
    }
    
    eatFruit(){
        this.body.push({x:this.body[0].x, y:this.body[0].y});
    }
}

let s;
let keys = new Array();
let fruitX = 10;
let fruitY = 10;
let score = 0;
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
        s.setDirection("up");
    }
    
    //A
    if(keys[65]){
        s.setDirection("left");
    }
    
    //S
    if(keys[83]){
        s.setDirection("down");
    }
    
    //D
    if(keys[68]){
        s.setDirection("right");
    }
    
}

function draw(){
    var ctx;
    try {
        ctx = $('#game-canvas').get(0).getContext('2d');
    } catch (e) {
        console.log('We have encountered an error: ' + e);
    }
    ctx.clearRect(0,0,1000,1000);
    
    ctx.fillStyle = "#FF0000";
    ctx.fillRect(fruitX, fruitY, 10,10);
    
    
    if($("#game-canvas").hasClass("score-blink")){
            $("#game-canvas").removeClass("score-blink");
        }
    if(fruitX == s.body[0].x && fruitY == s.body[0].y){
        
        eatFruit();
        s.eatFruit();
        
        $("#score-board").html(++score);
        $("#game-canvas").addClass("score-blink");
        
    }
    ctx.fillStyle = "#00FF00";
    s.move();
    s.draw(ctx);
    ctx.stroke();
}