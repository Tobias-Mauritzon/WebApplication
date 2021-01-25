/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
$(document).ready(function(){
    $("#login_button").click(function(){
        var user_name = $("#u_name").html();
        var pw = $("#pw").html();
        var res = login(user_name, pw);
        
        if(res){
            $("#login_res").html() = "Succes!!";
        }else{
            $("#login_res").html() = "Wrong Username or Passwords!!";
        }
    });
});
 * 
 */

$(document).ready(function() {
        $("#login_button").click(function(event) {
                var name = $('#u_name').val();
                $.get('LoginWithMongo', {
                        userName : name
                }, function(responseText) {
                        $('#login_res').text(responseText);
                });
        });
});


function login(u_name, pw){
    var res;
    try{
        res = send_command(u_name, pw);
    }catch(err){
        console.log(err);
        res = false;
    }
    
    return res;
}

function loadXMLDoc() {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      document.getElementById("demo").innerHTML =
      this.responseText;
    }
  };
  xhttp.open("GET", "xmlhttp_info.txt", true);
  xhttp.send();
}

$("login_button").click(function(){
  $.ajax({url: "demo_test.txt", success: function(result){
    $("#div1").html(result);
  }});
});



