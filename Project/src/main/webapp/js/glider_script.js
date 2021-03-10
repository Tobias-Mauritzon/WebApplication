
var canvas;
var ctx;
var background_image;
var background_image_heigth;
var background_image_width;
var pawn_canvas;
var pawn_ctx;
var pawn;
var pawn_pos_x;
var pawn_pos_y;
var speed = 5;
var map = {};
var score = 0;

function loaded() {
    $('iframe[name=game-frame]').contents().find('#start-button').click(function(){
    
        
        // Initializing canvas
        canvas = $('iframe[name=game-frame]').contents().find('#game-canvas');
        ctx = canvas[0].getContext("2d");
        background_image = new Image(1000, 700);
        background_image.src = "Resources/glider_res/terrang4.png";
        spawn_background();

        // Add keyboard events for movement
        $('iframe[name=game-frame]').contents().find("body").on('keydown', (event) => {
            map[event.which] = true;
        });
        
        $('iframe[name=game-frame]').contents().find("body").on('keyup', (event) => {
            map[event.which] = false;
        });
        
        
        $('iframe[name=game-frame]').contents().find("body").on('keypress', (event) => {
            key_press();
        });



        //Craeting and spawning the pawn in the canvas
        pawn_ctx = canvas.getContext('2d');

        pawn_image = new Image(64, 64);
        pawn_image.src = "Resources/glider_res/gubbe2.png";
        spawn_pawn();

    });

};

function spawn_background() {
    background_image.onload = function () {
        pawn_ctx = canvas[0].getContext('2d');
        
        pawn_image = new Image(64, 64);
        pawn_image.src = "Resources/glider_res/gubbe2.png";
        spawn_pawn();        
         
    };    
    
    
    $('iframe[name=game-frame]').contents().find('#submit-score').click(function(){
        setHighScore([{ name: "highscore", value: score}]);
    });
  }; 

function spawn_background(){
    background_image.onload = function() {
        ctx.drawImage(background_image, 0, 0);
    }
}
function spawn_pawn() {
    pawn_image.onload = function () {
        pawn_pos_x = 0;
        pawn_pos_y = 0;
        pawn_ctx.drawImage(pawn_image, pawn_pos_x, pawn_pos_y);

    }
}
function key_press() {

    //A
    if (map[65]) {
        pawn_pos_x += -speed;
        change_backround();
    }

    //W
    if (map[87]) {
        pawn_pos_y += -speed;
        change_backround();
    }

    //D
    if (map[68]) {
        pawn_pos_x += speed;
        change_backround();
    }

    //S
    if (map[83]) {
        pawn_pos_y += speed;

        change_backround();
    }

    //R
    if (map[82]) {
        pawn_pos_x = 0;
        pawn_pos_y = 0;
    }

    // I
    if (map[73]) {
        speed += 5;
    }

    // U
    if (map[85]) {
        speed += -5;
    }

    ctx.drawImage(background_image, 0, 0);
    pawn_ctx.drawImage(pawn_image, pawn_pos_x, pawn_pos_y);
}

function re_draw_background() {
    var map = Math.floor(Math.random() * 4) + 1;
    background_image.src = "Resources/glider_res/terrang" + map + ".png";
    ctx.drawImage(background_image, 0, 0);
    pawn_ctx.drawImage(pawn_image, pawn_pos_x, pawn_pos_y);
}

function change_backround() {
    if (pawn_pos_x > background_image_width) {
        pawn_pos_x += -background_image_width;
        re_draw_background();    
    }

    if (pawn_pos_x + 64 < 0) {
        pawn_pos_x += background_image_width;
        re_draw_background();      
    }

    if (pawn_pos_y > background_image_heigth) {
        pawn_pos_y += -background_image_heigth;
        re_draw_background();
    }

    if (pawn_pos_y + 64 < 0) {
        pawn_pos_y += background_image_heigth;
        re_draw_background();
    }
}


