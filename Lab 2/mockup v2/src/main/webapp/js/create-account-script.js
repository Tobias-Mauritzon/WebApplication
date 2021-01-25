/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function(){
    // Password == confirm password
    $('#inputPassword, #inputConfirmPassword').on('keyup', function () {
        if ($('#inputPassword').val() > 0 && $('#inputPassword').val() == $('#inputConfirmPassword').val()) {
            $('#confirmInfo').html('Matching').css('color', 'green');
            $("#submit").removeAttr("disabled");
        } else {
            $('#confirmInfo').html('Not Matching').css('color', 'red');
            $("#submit").attr("disabled", "disabled");
        }
    });
    

});