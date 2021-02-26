
var canvas;
var ctx;
var background_image;
var pawn_canvas;
var pawn_ctx;
var pawn;
var pawn_pos_x;
var pawn_pos_y;
var speed = 5;
var map = {};

$(document).ready(function(){
       
    $("#start_button").click(function(){
        
        // Initializing canvas
        canvas = document.getElementById('background_canvas');
        ctx = canvas.getContext('2d');
        background_image = new Image(1000, 700);
        background_image.src = "Resources/glider_res/terrang4.png";
        spawn_background();
        
        // Add keyboard events for movement
        document.addEventListener('keydown', (event) => {
            map[event.which] = true;
        });
        
        document.addEventListener('keyup', (event) => {
            map[event.which] = false;
        });
        
        
        document.addEventListener('keypress', (event) => {
            key_press();
        });
         
        
        
        //Craeting and spawning the pawn in the canvas
        pawn_ctx = canvas.getContext('2d');
        
        pawn_image = new Image(64, 64);
        pawn_image.src = "Resources/glider_res/gubbe2.png";
        spawn_pawn();        
         
    });    
  
  }); 

function spawn_background(){
    background_image.onload = function() {
        ctx.drawImage(background_image, 0, 0);
    } 
}
function spawn_pawn(){
    pawn_image.onload = function() {
        pawn_pos_x = 0;
        pawn_pos_y = 0;
        pawn_ctx.drawImage(pawn_image, pawn_pos_x, pawn_pos_y);
        
    } 
}
function key_press(){

    //A
    if(map[65]){
        pawn_pos_x += -speed;
        change_backround();
    }

    //W
    if(map[87]){
        pawn_pos_y += -speed;
        change_backround();
    }

    //D
    if(map[68]){
        pawn_pos_x += speed;
        change_backround();
    }

    //S
    if(map[83]){
        pawn_pos_y += speed;

        change_backround();
    }

    //R
    if(map[82]){
        pawn_pos_x = 0;
        pawn_pos_y = 0;
    }

    // I
    if(map[73]){
        speed += 5;
    }

    // U
    if(map[85]){
        speed += -5;
    }

    ctx.drawImage(background_image, 0, 0);
    pawn_ctx.drawImage(pawn_image, pawn_pos_x, pawn_pos_y); 
}

function re_draw_background(){
    var map = Math.floor(Math.random() * 4) + 1;
    background_image.src = "Resources/glider_res/terrang"+map+".png";
    ctx.drawImage(background_image, 0, 0);
}

function change_backround(){
    if(pawn_pos_x + 64 > 1000){
        re_draw_background();
        pawn_pos_x += -1000;
    }
    
    if(pawn_pos_x + 64 < 0){
        re_draw_background();
        pawn_pos_x += 1000;
    }
    
    if(pawn_pos_y + 64 > 700){
        re_draw_background();
        pawn_pos_y += -700;
    }
    
    if(pawn_pos_y + 64 < 0){
        re_draw_background();
        pawn_pos_y += 700;
    }
}
