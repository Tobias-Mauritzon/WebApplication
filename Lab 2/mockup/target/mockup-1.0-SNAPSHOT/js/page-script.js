/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

window.onload = init;

var answer = 12345;
var answerInput;

function init(){
    answerInput = document.getElementById("inputAnswer");
    answerInput.addEventListener("input", checkAnswer);
}

function checkAnswer(){
    
    if(answerInput.value == answer){
        alert("CORRECT!");
    }
}