/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var canvas;
var ctx;
var my_image;

;$(document).ready(function(){
       
    $("#start_button").click(function(){
        $("p").css("color", "red");
        canvas = document.getElementById('background_canvas');
        ctx = canvas.getContext('2d');
        my_image = new Image(1000, 700);
        my_image.src = "Resources/glider_res/terrang6.png";
        spawn_background();
    });
    
  
  }); 

function spawn_background(){
    my_image.onload = function() {
        ctx.drawImage(my_image, 0, 0);
    } 
}


//"../Resources/glider_res/terrang6.png"