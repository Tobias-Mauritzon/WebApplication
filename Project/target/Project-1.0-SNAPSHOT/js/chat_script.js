$(document).ready(function(){

    
});

function loaded() {
    console.log("loaded");
    $('iframe[name=game-frame]').contents().find('#chatInput').keypress(function(e){
        console.log("key pressed");
        var key = e.which;
        console.log(key);
        if(key == 13)  // the enter key code
         {
            // $("#chat").text($("#chat").text() + $("#chatInput").val());
            

            var txt1 = $("<b></b>").text("you: ");
            var txt2 = $("<p></p>").text($('iframe[name=game-frame]').contents().find('#chatInput').val()).css({"margin-bottom":"0","display":"inline"});

            var div1 = $("<div></div>");

            div1.append(txt1,txt2);
            $('iframe[name=game-frame]').contents().find('#chat').append(div1);     
            $('iframe[name=game-frame]').contents().find('#chatInput').val("");
           return true;  
         }
    })
}