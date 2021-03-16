// Author: Simon

var canvas;
var canvas_width = 700;
var canvas_height = 700;
var ctx;
var bctx;
var bird;
var pipe1;
var pipe2;
var pipe21;
var pipe22;
var background;
var score = [2]; //0 is current score, 1 is previous
var birdDead;


function loaded() {
    canvas = $('iframe[name=game-frame]').contents().find('#game-canvas');
    ctx = canvas[0].getContext("2d");
    bird = new Bird();
    pipe1 = new Pipe("Pipe", 1400);
    pipe2 = new Pipe("PipeDown", 1400);
    pipe21 = new Pipe("Pipe", 1050);
    pipe22 = new Pipe("PipeDown", 1050);
    background = new Background();
    score[0] = 0;
    birdDead = false;

    $('iframe[name=game-frame]').contents().find("body").on('keypress', (e) => {
        var key = e.which;
        if (bird.canFly && key == 32)  // the space key code
        {
            bird.fly();
            bird.canFly = false;
        }
    })

    $('iframe[name=game-frame]').contents().find("body").on('keyup', (e) => {
        bird.canFly = true;
    })

    //Start
    $('iframe[name=game-frame]').contents().find("#start-button").click(function () {
        $('iframe[name=game-frame]').contents().find("#start-button").addClass("disabled").prop("disabled", true);


        //Restart
        $('iframe[name=game-frame]').contents().find("#restart-button").click(function () {
            resetGame();
        });

        setInterval(onTimerTick, 33); // ~ 30 fps

        function onTimerTick() {
            bird.update();
            pipe2.updateTop(pipe1);
            pipe1.updateBottom();
            pipe22.updateTop(pipe21);
            pipe21.updateBottom();

            background.draw();
            bird.draw();
            pipe1.draw();
            pipe2.draw();
            pipe21.draw();
            pipe22.draw();
            printScore();
        }
    });
}
;

function resetGame() {

    $('iframe[name=game-frame]').contents().find("#restart-button").addClass("disabled").prop("disabled", true);
    bird = new Bird();
    pipe1 = new Pipe("Pipe", 1400);
    pipe2 = new Pipe("PipeDown", 1400);
    pipe21 = new Pipe("Pipe", 1050);
    pipe22 = new Pipe("PipeDown", 1050);
    background = new Background();
    score[0] = 0;
    birdDead = false;
    console.log("Reset button pressed");
}


function Bird() {
    this.width = 94;
    this.height = 48;
    this.gravity = 1.5;
    this.flyStrength = 20;
    this.canFly = true;
    this.image = new Image(this.width, this.height);
    this.image.src = "Resources/Bird/Birb-fly.png";
    this.vector = new Vector(270, 200, 0, 0);
    this.angel = 180;
}

Bird.prototype.update = function () {
    this.vector.dy += this.gravity;
    if (!birdDead) {
        this.colittion();
    }
    this.vector.update();
};

Bird.prototype.fly = function () {
    this.vector.dy = 0;
    this.vector.dy = -this.flyStrength;
};

Bird.prototype.colittion = function () {
    front = this.vector.x + this.width;
    if (front >= pipe1.vector.x && this.vector.x < (pipe1.vector.x + pipe1.width)) {
        if (((this.vector.y >= pipe2.vector.y + pipe2.height)) && ((this.vector.y + this.height) <= pipe1.vector.y)) { //True if Bellow top pipe
            //score
            if (score[0] === score[1]) {
                score[0] = score[0] + 1;
            }
        } else {
            gameOver();
        }
    } else if (front >= pipe21.vector.x && this.vector.x < (pipe21.vector.x + pipe21.width)) { //True if Bellow top pipe(same as above for the second instance of pipe)
        if (((this.vector.y >= pipe22.vector.y + pipe22.height)) && ((this.vector.y + this.height) <= pipe21.vector.y)) { //True if Bellow top pipe
            //score
            if (score[0] === score[1]) {
                score[0] = score[0] + 1;
            }
        } else {
            gameOver();
        }
    } else {
        score[1] = score[0];
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

function Pipe(pipeSprite, startXPos) {
    this.width = 75;
    this.height = 600;
    this.image = new Image(this.width, this.height);
    this.image.src = "Resources/Bird/" + pipeSprite + ".png";
    if (pipeSprite === "Pipe") {
        this.vector = new Vector(startXPos, 600 - (Math.random() * 400), -9, 0);
    } else {
        this.vector = new Vector(startXPos, 0, -9, 0);
    }

}

Pipe.prototype.updateTop = function (bottomPipe) {
    this.vector.y = (bottomPipe.vector.y - this.height) - 180;
    this.vector.x = bottomPipe.vector.x;
    this.vector.update();
};


Pipe.prototype.updateBottom = function () {
    if (this.vector.x <= (-this.width)) {
        this.vector.x = 700;
        this.vector.y = 650 - (Math.random() * 350);
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

function gameOver() {
    if (birdDead === false) {
        bird.image.src = "Resources/Bird/Birb-sad.png";
        $('iframe[name=game-frame]').contents().find("#restart-button").removeClass("disabled").prop("disabled", false);
        birdDead = true;
        pipe1.vector.dx = 0;
        pipe2.vector.dx = 0;
        pipe21.vector.dx = 0;
        pipe22.vector.dx = 0;
        bird.flyStrength = 0;
        setHighScore([{name: "highscore", value: score[0]}]);
        console.log("GAME OVER");
    }
}

function printScore() {
    ctx.font = "20px Comic Sans MS";
    ctx.fillStyle = "white";
    ctx.textAlign = "center";
    ctx.fillText("Points: " + (score[0]), 80, 25);
}