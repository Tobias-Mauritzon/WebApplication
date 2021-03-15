// Author: Matteus
var runner;
var canvas;
var canvas_width = 700;
var canvas_height = 700;
var ctx;
var player;
var obstacle;
var background;
var score;

/**
 * First time setup
 */
function loaded() {
    canvas = $('iframe[name=game-frame]').contents().find('#game-canvas');
    player = new Player();
    obstacle = new Obstacle();
    background = new Background();
    score = 0;

    /**
     * Start Button
     * Init Game
     */
    $('iframe[name=game-frame]').contents().find("#start-button").click(function () {
        $('iframe[name=game-frame]').contents().find("#start-button").addClass("disabled").prop("disabled", true);

        ctx = canvas[0].getContext("2d");
        $('iframe[name=game-frame]').contents().find("body").on('keydown', (e) => {
            var key = e.which;
            if (key === 32)  // the space key code
            {
                player.jump();
            }
        });

        setInterval(onTimerTick, 33); // 33 milliseconds = ~ 30 frames per sec

        /**
         * Game Loop
         */
        function onTimerTick() {
            player.update();
            obstacle.update();
            $('iframe[name=game-frame]').contents().find('#score-label').text("Score: " + score);
            background.draw();
            player.draw();
            obstacle.draw();
        }
    });

    /**
     * Submit Score Button
     */
    $('iframe[name=game-frame]').contents().find('#submit-score').click(function () {
        setHighScore([{name: "highscore", value: score}]);
    });
}
;

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

/**
 * Player character
 */
function Player() {
    this.width = 100;
    this.height = 100;
    this.gravity = 1;
    this.jumpVelocity = 20;
    this.image = new Image(this.width, this.height);
    this.image.src = "Resources/squirrel.png";
    this.vector = new Vector(200, 600, 0, 0);
    this.canJump = true;
}

/**
 * Player update function
 */
Player.prototype.update = function () {
    if (this.vector.y < 700 - this.height) {
        this.vector.dy += this.gravity;
    }
    if (this.vector.y > (700 - this.height)) {
        this.vector.dy = 0;
        this.vector.y = 700 - this.height;
        this.canJump = true;
    }

    this.vector.advance();
};

/**
 * Player jump function
 */
Player.prototype.jump = function () {
    if (this.vector.dy === 0 && this.canJump) {
        this.vector.dy = -this.jumpVelocity;
        this.canJump = false;
    }
};

/**
 * Function to draw player on canvas
 */
Player.prototype.draw = function () {
    ctx.drawImage(this.image, this.vector.x, this.vector.y, this.width, this.height);
};

/**
 * Obstacle
 */
function Obstacle() {
    this.width = 80;
    this.height = 80;
    this.image = new Image(this.width, this.height);
    this.image.src = "Resources/matchstick/table.png";
    this.vector = new Vector(700, 700 - this.height, -10, 0);
}

/**
 * Obstacle update function
 */
Obstacle.prototype.update = function () {
    if (this.vector.x <= 0) {
        this.vector.x = 700;
        score++;
    }
    this.vector.advance();
};

/**
 * Function to draw Obstacle on canvas
 */
Obstacle.prototype.draw = function () {
    ctx.drawImage(this.image, this.vector.x, this.vector.y, this.width, this.height);
};

/**
 * Background
 */
function Background() {
    this.image = new Image(this.width, this.height);
    this.image.src = "Resources/windows.jpg";
}

/**
 * Function to draw Background on canvas
 */
Background.prototype.draw = function () {
    ctx.drawImage(this.image, 0, 0);
};