// Author: Matteus
var score = 0;

/**
 * First time setup
 */
function loaded() {
    var running = false;

    /**
     * Rotation Button
     */
    $('iframe[name=game-frame]').contents().find('#rotate-button').click(function () {
        if (!running) {
            $('iframe[name=game-frame]').contents().find('.box').css('animation-play-state', 'running');
            $('iframe[name=game-frame]').contents().find('#rotate-button').text("Stop Rotating");
            score++;
            $('iframe[name=game-frame]').contents().find('#score-label').text("Score: " + score);
            running = true;
        } else {
            $('iframe[name=game-frame]').contents().find('.box').css('animation-play-state', 'paused');
            $('iframe[name=game-frame]').contents().find('#rotate-button').text('Rotate!');
            running = false;
        }
    });

    /**
     * Submit Score Button
     */
    $('iframe[name=game-frame]').contents().find('#submit-score').click(function () {
        setHighScore([{name: "highscore", value: score}]);
    });
}
; 