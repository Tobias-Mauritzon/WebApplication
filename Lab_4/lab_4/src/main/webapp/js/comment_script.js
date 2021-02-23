/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function ratingSubmit(){
    console.log(document.getElementById('game-type').name);
    var txt = document.getElementById('name-plceholder'); 
         txt.value = document.getElementById('game-type').name;
}
