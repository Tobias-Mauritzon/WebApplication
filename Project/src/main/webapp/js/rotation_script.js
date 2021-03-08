

function loaded() {
    var running = false;
    console.log("loaded");
    $('iframe[name=game-frame]').contents().find('#rotate-button').click(function(){
        if(!running){
            $('iframe[name=game-frame]').contents().find('.box').css('animation-play-state','running');
            $('iframe[name=game-frame]').contents().find('#rotate-button').text("Stop Rotating");
            running = true;
            console.log("running");
        }
        else{
            $('iframe[name=game-frame]').contents().find('.box').css('animation-play-state','paused');
            $('iframe[name=game-frame]').contents().find('#rotate-button').text('Rotate!');
            running = false;
            console.log("stopped");
        }
    })
  }; 