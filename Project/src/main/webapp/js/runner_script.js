// //@ts-check
var runner;
var canvas;
var canvas_width = 700;
var canvas_height = 700;
var ctx;
var player;
var obstacle;
var background;

function loaded() {
    canvas = $('iframe[name=game-frame]').contents().find('#game-canvas');
    player = new Player();
    obstacle = new Obstacle();
    background = new Background();
    $('iframe[name=game-frame]').contents().find("#start-button").click(function(){

      ctx = canvas[0].getContext("2d");
      $('iframe[name=game-frame]').contents().find("body").on('keydown', (e) => {
        var key = e.which;
        if(key == 32)  // the space key code
         {
          player.jump();
         }
      })

      setInterval(onTimerTick, 33); // 33 milliseconds = ~ 30 frames per sec

      function onTimerTick() {
          player.update();
          obstacle.update();

          background.draw();
          player.draw();
          obstacle.draw();
      }
    });
  
    $('iframe[name=game-frame]').contents().find('#submit-score').click(function () {
        setHighScore([{name: "highscore", value: score}]);
    });
};

function Vector(x, y, dx, dy) {
    // position
    this.x = x || 0;
    this.y = y || 0;
    // direction
    this.dx = dx || 0;
    this.dy = dy || 0;
}

/**
 * Advance the vectors position by dx,dy
 */
Vector.prototype.advance = function () {
    this.x += this.dx;
    this.y += this.dy;
};


function Player() {
    this.width = 100;
    this.height = 100;
    this.gravity = 1;
    this.jumpVelocity = 20;
    this.image = new Image(this.width, this.height);
    this.image.src = "Resources/squirrel.png";
    this.vector = new Vector(200, 600, 0, 0);
}

Player.prototype.update = function () {
    if (this.vector.y < 700 - this.height) {
        this.vector.dy += this.gravity;
    }
    if (this.vector.y > (700 - this.height)) {
        this.vector.dy = 0;
        this.vector.y = 700 - this.height;
    }

    this.vector.advance();

}

Player.prototype.jump = function () {
    if (this.vector.dy == 0) {
        this.vector.dy = -this.jumpVelocity;
    }
}

Player.prototype.draw = function () {
    ctx.drawImage(this.image, this.vector.x, this.vector.y, this.width, this.height);
}


function Obstacle() {
    this.width = 80;
    this.height = 80;
    this.image = new Image(this.width, this.height);
    this.image.src = "Resources/matchstick/table.png";
    this.vector = new Vector(700, 700 - this.height, -10, 0);
}

Obstacle.prototype.update = function () {
    if (this.vector.x <= 0) {
        this.vector.x = 700
    }
    this.vector.advance();
}

Obstacle.prototype.draw = function () {
    ctx.drawImage(this.image, this.vector.x, this.vector.y, this.width, this.height);
}


function Background() {
    this.image = new Image(this.width, this.height);
    this.image.src = "Resources/windows.jpg";
}

Background.prototype.draw = function () {
    ctx.drawImage(this.image, 0, 0);
}