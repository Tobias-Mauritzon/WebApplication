var count = 3;

$(document).ready(function(){
    
    $("#removeOne").click(function(){
       $("#stick" + count + "").hide();
       count -= 1; 
    });
    
    $("#removeTwo").click(function(){
        for(i=0; i<2; i++){
            $("#stick" + count + "").hide();
            count -= 1;
        } 
    });
    
    $("#reset").click(function(){
        for(i=1; i<4; i++){
            $("#stick" + i + "").show();
        }
        count = 3;
    });
});