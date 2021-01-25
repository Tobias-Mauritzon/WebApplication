$(document).ready(function(){
    $("#chatInput").keypress(function(e){
        var key = e.which;
        if(key == 13)  // the enter key code
         {
            // $("#chat").text($("#chat").text() + $("#chatInput").val());
            

            var txt1 = $("<b></b>").text("you: ");
            var txt2 = $("<p></p>").text($("#chatInput").val()).css({"margin-bottom":"0","display":"inline"});

            var div1 = $("<div></div>");

            div1.append(txt1,txt2);
            $("#chat").append(div1);     
            $("#chatInput").val("");
           return true;  
         }
    })
});