/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var canvas;
var ctx;
var background_image;
var pawn_canvas;
var pawn_ctx;
var pawn;
var pawn_pos_x;
var pawn_pos_y;
var speed = 5;

$(document).ready(function(){
       
    $("#start_button").click(function(){
        
        //test if the button click worked
        $("p").css("color", "red");
        
        // Initializing canvas
        canvas = document.getElementById('background_canvas');
        ctx = canvas.getContext('2d');
        background_image = new Image(1000, 700);
        background_image.src = "Resources/glider_res/terrang4.png";
        spawn_background();
        
        // Add keyboard events for movement
        document.addEventListener('keydown', (event) => {
            key_press(event);
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
function key_press(event){
    $('#h1_g').html('keydown() is triggered!, keyCode = ' 
              + event.keyCode + ' which = ' + event.which);
    //A
    if(event.which === 65){
        pawn_pos_x += -speed;
        change_backround();
    }

    //W
    if(event.which === 87){
        pawn_pos_y += -speed;
        change_backround();
    }

    //D
    if(event.which === 68){
        pawn_pos_x += speed;
        change_backround();
    }

    //S
    if(event.which === 83){
        pawn_pos_y += speed;
        
        change_backround();
    }
      
      //R
    if(event.which === 82){
        pawn_pos_x = 0;
        pawn_pos_y = 0;
    }
    
    // I
    if(event.which === 73){
        speed += 5;
    }
    
    // U
    if(event.which === 85){
        speed += -5;
    }
    
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
