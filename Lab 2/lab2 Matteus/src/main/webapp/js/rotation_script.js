

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