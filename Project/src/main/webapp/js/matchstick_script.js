var count;
var canvas;
var ctx;
var background_image;
var match;
var matches;
var turn;

/**
 * The matchstick game!
 * @author Joachim Antfolk
 */

function loaded(){
    
    // Initializing
    canvas = $("iframe[name=game-frame]").contents().find("#gameCanvas");
    ctx = canvas[0].getContext("2d");
    turn = true;
    background_image = new Image(1000, 600);
    background_image.src = "Resources/matchstick/table.png";
    ctx.drawImage(background_image, 0, 0);
    
    $("iframe[name=game-frame]").contents().find(".btn").css({"margin": "10px", "width": "200px", "border-radius": "0px", "font-family": "Arial"});
    
    reset();
    spawnBackground();
    printMid("Press 'RESET' to Start Game");
    
    $("iframe[name=game-frame]").contents().find("#removeOne").click(function(){
        if(turn){
            playerMove(1);
            setTimeout(() => {computerMove();}, 1000);
        }
    });
    
    $("iframe[name=game-frame]").contents().find("#removeTwo").click(function(){
        if(turn){
            playerMove(2);
            setTimeout(() => {computerMove();}, 1000);
        }
    });
    
    $("iframe[name=game-frame]").contents().find("#reset").click(function(){
        reset();
        redraw();
    });
}

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
    ctx.fillText("MATCHES LEFT: " + count, 30, 50);   
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
    ctx.fillText(str, canvas[0].width/2, canvas[0].height/2);
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
    if(turn === false){
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
                //remove(parseInt(1 + Math.round(Math.random())));
            }
            turn = true;
            redraw();
            checkWin();
        }
    } 
}

