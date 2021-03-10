


function loaded() {
    var number = 0;
    $('iframe[name=game-frame]').contents().find("#addButton").click(function(){
        number++;
        $('iframe[name=game-frame]').contents().find("#number").text(number);
    })
    
    $('iframe[name=game-frame]').contents().find('#submit-score').click(function(){
        setHighScore([{ name: "highscore", value: score}]);
    });
};