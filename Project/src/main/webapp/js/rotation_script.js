
var score = 0;

function loaded() {
    var running = false;
    $('iframe[name=game-frame]').contents().find('#rotate-button').click(function(){
        if(!running){
            $('iframe[name=game-frame]').contents().find('.box').css('animation-play-state','running');
            $('iframe[name=game-frame]').contents().find('#rotate-button').text("Stop Rotating");
            running = true;
        }
        else{
            $('iframe[name=game-frame]').contents().find('.box').css('animation-play-state','paused');
            $('iframe[name=game-frame]').contents().find('#rotate-button').text('Rotate!');
            running = false;
        }
    })
    
    $('iframe[name=game-frame]').contents().find('#submit-score').click(function(){
        setHighScore([{ name: "highscore", value: score}]);
    });
  }; 