var count;
var canvas;
var ctx;
var background_image;
var match;
var matches;
var turn;
var score;
var gameRunning;

/**
 * The matchstick game!
 * @author Joachim Antfolk
 */

/**
 * First time setup
 */
function loaded(){
    setUp();
    
    // Initializing
    canvas = $("iframe[name=game-frame]").contents().find("#gameCanvas");
    ctx = canvas[0].getContext("2d");
    turn = true;
    background_image = new Image(1100, 650);
    background_image.src = "Resources/matchstick/table.png";
    
    //Style buttons
    $("iframe[name=game-frame]").contents().find(".btn").css({"margin": "10px", "width": "200px", "border-radius": "0px", "font-family": "Arial"});
    
    //Initiate game and print start text
    reset();
    printMidMain("Press 'START' to Start Game");
    score = 0;
    
    //Add actions to remove one stick button
    $("iframe[name=game-frame]").contents().find("#removeOne").click(function(){
        if(turn){
            playerMove(1);
            setTimeout(() => {computerMove();}, 1000);
        }
    });
    
    //Add actions to remove two sticks button
    $("iframe[name=game-frame]").contents().find("#removeTwo").click(function(){
        if(turn){
            playerMove(2);
            setTimeout(() => {computerMove();}, 1000);
        }
    });
    
    //Add actions to reset button
    $("iframe[name=game-frame]").contents().find("#reset").click(function(){
        $("iframe[name=game-frame]").contents().find("#removeOne, #removeTwo").css({"display": "inline"});
        $("iframe[name=game-frame]").contents().find("#exit").css({"display": "none"});
        if(gameRunning){
            score--;
        }
        reset();
        redraw();
    });
    
    //Add actions to start button
    $("iframe[name=game-frame]").contents().find("#start").click(function(){
        $("iframe[name=game-frame]").contents().find("#start").css({"display": "none"});
        $("iframe[name=game-frame]").contents().find("#removeOne, #removeTwo, #reset").css({"display": "inline"});
        score = 0;
        reset();
        redraw();
    });
    
    //Add actions to exit button
    $("iframe[name=game-frame]").contents().find("#exit").click(function(){
        $("iframe[name=game-frame]").contents().find("#start").css({"display": "inline"});
        $("iframe[name=game-frame]").contents().find("#removeOne, #removeTwo, #reset, #exit").css({"display": "none"});
        setHighScore([{name: "highscore", value: score}]); //Submit score
        ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
        printMidMain("Press 'START' to Start Game");
    });
}

/**
 * Redraws game
 */
function redraw(){
    ctx.drawImage(background_image, 0, 0);    
    for(i = 0; i < count; i++){
        ctx.drawImage(matches[i].image, matches[i].x, matches[i].y);
    }
    ctx.font = "30px Arial";
    ctx.fillStyle = "black";
    ctx.textAlign = "left";
    ctx.fillText("MATCHES LEFT: " + count, 30, 50);   
    ctx.fillText("SCORE: " + score, 30, 90);   
    
}

/**
 * Removes matches from the pile
 * @param number: Number of matches to remove
 */
function remove(number){
    if(count > 1){
        count -= number;
    } else {
        count -= 1;
    }
}

/**
 * Checks if there is a winner prints if win/loss, updates score accordingly, and prepares for exit or next round
 */
function checkWin(){
    if(count <= 0){
        gameRunning = false;
        if(turn){
            score++;
            redraw();
            printMidMain("YOU WIN!");   
        } else {
            score--;
            redraw();
            printMidMain("YOU LOSE!");
        }
        printMidSubText("Press 'EXIT' to return to start page and submit score");
        $("iframe[name=game-frame]").contents().find("#exit").css({"display": "inline"});
        $("iframe[name=game-frame]").contents().find("#removeOne, #removeTwo").css({"display": "none"});
    }
}

/**
 * Prints main middle text
 */
function printMidMain(str){
    ctx.font = "50px Arial";
    ctx.fillStyle = "black";
    ctx.textAlign = "center";
    ctx.fillText(str, canvas[0].width/2, canvas[0].height/2);
}

/**
 * Prints smaller text in the middle under main middle text
 */
function printMidSubText(str){
    ctx.font = "30px Arial";
    ctx.fillStyle = "black";
    ctx.textAlign = "center";
    ctx.fillText(str, canvas[0].width/2, (canvas[0].height/2) + 40);
}

/**
 * Resets game state such as matches and next turn
 */
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
    gameRunning = true;
    count = 21;
}

/**
 * Hadles logic for player moves
 */
function playerMove(number){
    if(count > 0){
        remove(number);
        turn = false;
    }
    redraw();
    checkWin();
}

/**
 * Hadles logic for computer player
 */
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
                remove(parseInt(1 + Math.round(Math.random())));
            }
            turn = true;
            redraw();
            checkWin();
        }
    } 
}

/**
 * Setup style and position for elements
 */
function setUp(){
    $("iframe[name=game-frame]").contents().find("#container").css({"position": "relative"});
    $("iframe[name=game-frame]").contents().find("#input").css({"bottom": "0", "left": "0", "position": "absolute", "width": "100%"});
    $("iframe[name=game-frame]").contents().find("#removeOne, #removeTwo, #reset, #exit").css({"display": "none"});
    $("iframe[name=game-frame]").contents().find("#gameCanvas").css({"background-color": "white"});
}

