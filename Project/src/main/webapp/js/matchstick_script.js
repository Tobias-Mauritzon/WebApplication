var count;
var canvas;
var ctx;
var background_image;
var match;
var matches;
var turn;

$(document).ready(function(){
    
    // Initializing
    canvas = document.getElementById("gameCanvas");
    ctx = canvas.getContext("2d");
    turn = true;
    background_image = new Image(1000, 600);
    background_image.src = "Resources/matchstick/table.png";
    ctx.drawImage(background_image, 0, 0);
    reset();
    spawnBackground();
    
    $("#removeOne").click(function(){
        if(turn){
            playerMove(1);
            setTimeout(() => {computerMove();}, 1000);
        }
    });
    
    $("#removeTwo").click(function(){
        if(turn){
            playerMove(2);
            setTimeout(() => {computerMove();}, 1000);
        }
    });
    
    $("#reset").click(function(){
        reset();
        redraw();
    });
});

function spawnBackground(){
    background_image.onload = function() {
        Redraw();
    };
}

function redraw(){
    ctx.drawImage(background_image, 0, 0);    
    for(i = 0; i < count; i++){
        ctx.drawImage(matches[i].image, matches[i].x, matches[i].y);
    }
    ctx.font = "30px Arial";
    ctx.fillStyle = "black";
    ctx.textAlign = "left";
    ctx.fillText("Matches left: " + count, 30, 30);
}

function remove(number){
    if(count > 1){
        count -= number;
    } else {
        count -= 1;
    }
}

function checkWin(){
    if(count <= 0){
        if(turn){
            printMid("YOU WIN!");
        } else {
            printMid("YOU LOSE!");
        }
    }
}

function printMid(str){
    ctx.font = "50px Arial";
    ctx.fillStyle = "black";
    ctx.textAlign = "center";
    ctx.fillText(str, canvas.width/2, canvas.height/2);
}

function reset(){
    count = 0;
    matches = [];
	turn = true;
    for(i = 0; i < 21; i++){
        match = new Image(40, 140);
        match.src = "Resources/matchstick/matchstick (" + parseInt(1 + Math.random() * 7) + ").png";
        var xPos = 100 + Math.random() * 700;
        var yPos = 100 + Math.random() * 300;
        matches.push({image:match, x: xPos, y:yPos});
    }
    count = 21;
}

function playerMove(number){
    if(count > 0){
        remove(number);
        turn = false;
        redraw();
    }
    checkWin();
}

function computerMove(){
    if(count > 0){
        if(((count % 3) === 0) || (((count + 1) % 3) === 0)){ //Check winning positionS
            if((count % 3) === 0){
                remove(2);
            }
            else{
                remove(1);
            }
        } else {
            remove(1);
        }
        turn = true;
        redraw();
        checkWin();
    }
}

