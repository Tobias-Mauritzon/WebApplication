var canvas;
var canvas_width = 700;
var canvas_height = 700;
var ctx;
var bird;
var pipe1;
var pipe2;
var background;

document.defaultView.onkeydown = function(e) {
    return e.keyCode !== 32;
};

$(document).ready(function () {
    canvas = document.getElementById("game-canvas");
    bird = new Bird();
    pipe1 = new Pipe("Pipe");
    pipe2 = new Pipe("PipeDown");
    background = new Background();

    $("#start-button").click(function () {
        $("#start-button").addClass("disabled").prop("disabled", true);
        ctx = canvas.getContext("2d");

        document.addEventListener('keydown', event => {
            if (event.which == 32)  // the space key code
            {
                bird.fly();
                bird.colittion();
            }
            return false;
        });

        setInterval(onTimerTick, 33); // ~ 30 fps

        function onTimerTick() {
            bird.update();
            pipe2.updateTop(pipe1);
            pipe1.updateBottom();
            
            background.draw();
            bird.draw(); 
            pipe1.draw();
            pipe2.draw();
           ;
        }   
    });
});




function Bird() {
    this.width = 94;
    this.height = 48;
    this.gravity = 1.7;
    this.flyStrength = 20;
    this.image = new Image(this.width, this.height);
    this.image.src = "Resources/Bird/Birb-fly.png";
    this.vector = new Vector(270, 200, 0, 0);
    this.canJump;
}

Bird.prototype.update = function () {
    if (this.vector.y < 700 - this.height) {
        this.vector.dy += this.gravity;
        this.canJump = false;
    }
    if (this.vector.y > (700 - this.height)) {
        //you lose
    }
    else{
        this.canJump = true;
    }
    this.colittion();
    
    this.vector.update();
};

Bird.prototype.fly = function () {
    if (this.canJump) {
        this.vector.dy = 0;
        this.vector.dy = -this.flyStrength;
    }
};

Bird.prototype.colittion = function () {
    front = this.vector.x+this.width;
    if(front >= pipe1.vector.x && this.vector.x < (pipe1.vector.x + pipe1.width)){
        if(this.vector.y > (pipe2.vector.y + pipe2.height)){ // bellow top pipe
            console.log("bellow top");
            if((this.vector.y + this.height) < pipe1.vector.y){ //above bottom pipe
                console.log("above bottom");
            }    
            //Do nothing, you aren't hit
        }
        else{
            console.log("hit");
        }
        
    }
};

Bird.prototype.draw = function () {
    ctx.drawImage(this.image, this.vector.x, this.vector.y, this.width, this.height);
};

function Vector(x, y, dx, dy) {
    this.x = x;
    this.y = y;
    
    this.dx = dx;
    this.dy = dy;
}

Vector.prototype.update = function () {
    this.x += this.dx;
    this.y += this.dy;
};

function Pipe(pipeSprite) {
    this.width = 75;
    this.height = 600;
    this.image = new Image(this.width, this.height);
    this.image.src = "Resources/Bird/" + pipeSprite + ".png";
    if(pipeSprite === "Pipe"){
        this.vector = new Vector(700, 600 - (Math.random() * 400), -5, 0);    
    }else{
        this.vector = new Vector(700, 0, -5, 0);
    }
    
}

Pipe.prototype.updateTop = function (bottomPipe) {
    this.vector.y = (bottomPipe.vector.y - this.height) - 200;
    this.vector.x = bottomPipe.vector.x;
    this.vector.update();
};


Pipe.prototype.updateBottom = function () {
    if (this.vector.x <= (-this.width)) {
        this.vector.x = 700;
        this.vector.y = 600 - (Math.random() * 400);
    }
    this.vector.update();
};

Pipe.prototype.draw = function () {
    ctx.drawImage(this.image, this.vector.x, this.vector.y, this.width, this.height);
};


function Background() {
    this.width = 700;
    this.height = 700;
    this.image = new Image(this.width, this.height);
    this.image.src = "Resources/Bird/Background.png";
}

Background.prototype.draw = function () {
    ctx.drawImage(this.image, 0, 0);
};

function gameOver(){
    pipe1.vector.dy = 0;
    pipe2.vector.dy = 0;
}