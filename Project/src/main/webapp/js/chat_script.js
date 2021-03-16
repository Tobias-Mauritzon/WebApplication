// Author: Matteus

var score;

/**
 * First time setup
 */
function loaded() {
    score = 0;

    /**
     * Key Press listner
     */
    $('iframe[name=game-frame]').contents().find('#chatInput').keypress(function (e) {
        var key = e.which;
        console.log(key);

        /**
         * If enter is pressed add comment
         */
        if (key === 13)  // the enter key code
        {
            score++;
            $('iframe[name=game-frame]').contents().find('#score-label').text("Score: " + score);

            var txt1 = $("<b></b>").text("you: ");
            var txt2 = $("<p></p>").text($('iframe[name=game-frame]').contents().find('#chatInput').val()).css({"margin-bottom": "0", "display": "inline"});

            var div1 = $("<div></div>");

            div1.append(txt1, txt2);
            $('iframe[name=game-frame]').contents().find('#chat').append(div1);
            $('iframe[name=game-frame]').contents().find('#chatInput').val("");
            return true;
        }
    });

    /**
     * Submit Score Button
     */
    $('iframe[name=game-frame]').contents().find('#submit-score').click(function () {
        setHighScore([{name: "highscore", value: score}]);
    });
}