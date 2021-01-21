/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function(){
    var running = false;
    $("#rotateButton").click(function(){
        if(!running){
            $(".box").css("animation-play-state","running");
            $("#rotateButton").text("Stop Rotating");
            running = true;
        }
        else{
            $(".box").css("animation-play-state","paused");
            $("#rotateButton").text("Rotate!");
            running = false;
        }
    })
    
  
  }); 


