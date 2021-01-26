/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function(){
    // Password == confirm password
    $('#inputEmai1, #inputPassword, #inputConfirmPassword').on('keyup', function () {
        if ($('#inputPassword').val() && $('#inputPassword').val() == $('#inputConfirmPassword').val()) {
            $('#confirmInfo').html('Matching').css('color', 'green');
            
            if($('#inputEmai1').val() !== '') {
                $("#submit").removeAttr("disabled");
            } else {
                $("#submit").attr("disabled", "disabled");
            }
        } else {
            $('#confirmInfo').html('Not Matching').css('color', 'red');
            $("#submit").attr("disabled", "disabled");
        }
    });
    
    
    $("#create-account-form").submit(function(){
        console.log("Email", this.elements[0].value);
        console.log("Password", this.elements[1].value);
    });

});