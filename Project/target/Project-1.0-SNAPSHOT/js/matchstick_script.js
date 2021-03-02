var count;
var canvas;
var ctx;
var background_image;
var match;
var matches;


function loaded(){
    
    // Initializing
    
    canvas = $('iframe[name=game-frame]').contents().find('#gameCanvas');
    ctx = canvas[0].getContext("2d");
    background_image = new Image(1000, 600);
    background_image.src = "Resources/matchstick/table.png";
    ctx.drawImage(background_image, 0, 0);

    reset();
    spawnBackground();
    

    $('iframe[name=game-frame]').contents().find('#removeOne').click(function(){
       removeOne();
       Redraw();
    });
    
    $('iframe[name=game-frame]').contents().find('#removeTwo').click(function(){
        removeTwo();
        Redraw();
    });
    
    $('iframe[name=game-frame]').contents().find('#reset').click(function(){
        reset();
        Redraw();
    });
}

function spawnBackground(){
    background_image.onload = function() {
        ctx.drawImage(background_image, 0, 0);    
        for(i = 0; i < count; i++){
            ctx.drawImage(matches[i].image, matches[i].x, matches[i].y);
        }
    };
}

function Redraw(){
    ctx.drawImage(background_image, 0, 0);    
    for(i = 0; i < count; i++){
        ctx.drawImage(matches[i].image, matches[i].x, matches[i].y);
    }
}

function removeOne(){
    count -= 1;
}

function removeTwo(){
    count -= 2;
}

function reset(){
    count = 0;
    matches = [];
    for(i = 0; i < 21; i++){
        match = new Image(40, 140);
        match.src = "Resources/matchstick/matchstick.png";
        var xPos = Math.random() * 900;
        var yPos = Math.random() * 450;
        matches.push({image:match, x: xPos, y:yPos});
    }
    count = 21;
}