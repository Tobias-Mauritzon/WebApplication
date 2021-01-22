$(document).ready(function(){
    $("#chatInput").keypress(function(e){
        var key = e.which;
        if(key == 13)  // the enter key code
         {
            $("#chat").text($("#chatInput").text());
            $("#chat").css("color","red")
           return false;  
         }
    })
});