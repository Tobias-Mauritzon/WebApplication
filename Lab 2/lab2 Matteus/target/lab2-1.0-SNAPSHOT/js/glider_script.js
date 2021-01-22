/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var canvas;
var ctx;
var my_image;
var pawn_canvas;
var pawn_ctx;
var pawn;
var pawn_pos_x;
var pawn_pos_y;

$(document).ready(function(){
       
    $("#start_button").click(function(){
        
        //test if the button click worked
        $("p").css("color", "red");
        
        // Initializing canvas
        canvas = document.getElementById('background_canvas');
        ctx = canvas.getContext('2d');
        my_image = new Image(1000, 700);
        my_image.src = "Resources/glider_res/terrang6.png";
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
    my_image.onload = function() {
        ctx.drawImage(my_image, 0, 0);
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
          pawn_pos_x += -5;
          pawn_ctx.drawImage(pawn_image, pawn_pos_x, pawn_pos_y);
      }
      
      //W
      if(event.which === 87){
          pawn_pos_y += -5;
          pawn_ctx.drawImage(pawn_image, pawn_pos_x, pawn_pos_y);
      }
      
      //D
      if(event.which === 68){
          pawn_pos_x += 5;
          pawn_ctx.drawImage(pawn_image, pawn_pos_x, pawn_pos_y);
      }
      
      //S
      if(event.which === 83){
          pawn_pos_y += 5;
          pawn_ctx.drawImage(pawn_image, pawn_pos_x, pawn_pos_y);
      }
      
      //R
      if(event.which === 82){
        pawn_pos_x = 0;
        pawn_pos_y = 0;
        pawn_ctx.drawImage(pawn_image, pawn_pos_x, pawn_pos_y);
      }
}

//"../Resources/glider_res/terrang6.png"