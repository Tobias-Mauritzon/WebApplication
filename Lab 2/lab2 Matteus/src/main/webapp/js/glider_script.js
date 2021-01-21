/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var canvas;
var ctx;

$(document).ready(function(){
       
    $("#start_button").click(function(){
        $("p").css("color", "red");
        canvas = document.getElementById('background_canvas');
        ctx = canvas.getContext("2d");
        spawn_background();
    })
    
  
  }); 

function spawn_background(){
    ctx.drawImage("../Resources/glider_res/terrang6.png", 0, 0);
}

