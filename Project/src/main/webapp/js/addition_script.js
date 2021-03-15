// Author: Matteus

/**
 * First time setup
 */
function loaded() {
    var number = 0;

    /**
     * Add Button
     */
    $('iframe[name=game-frame]').contents().find("#addButton").click(function () {
        number++;
        $('iframe[name=game-frame]').contents().find("#number").text(number);
    });

    /**
     * Submit Score Button
     */
    $('iframe[name=game-frame]').contents().find('#submit-score').click(function () {
        setHighScore([{name: "highscore", value: number}]);
    });
}
;