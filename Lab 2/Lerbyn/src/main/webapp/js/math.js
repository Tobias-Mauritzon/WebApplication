/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * 
 */
$(document).ready(init);

var answerInput;

var partA;
var partB;
var signPart;
var answer;
var difficulty;
var diffucultyButtons;

function generateEasyEquation(){
    partA = randomNumber(10);
    partB = randomNumber(10);
    signPart = (randomNumber(1)?"+":"-");
    answer = partA + (signPart=="+"?1:-1) * partB;
    setEquation();
}

function generateMediumEquation(){
    partA = randomNumber(10);
    partB = randomNumber(10);
    signPart = "*" //(randomNumber(1)?"*":"/");
    answer = partA * partB;
    setEquation();
}

function setEquation(){
    $("#input-answer").val("");
    var equation = partA + " " + signPart + " " + partB;
    alert("equation set");
    $("#equation").text(equation);
}

function randomNumber(max){
    return Math.floor(Math.random() * (max+1));
}

function init(){
    $("#easy").click(setDifficulty);
    $("#medium").click(setDifficulty);
    $("#hard").click(setDifficulty);
    
    answerInput = $("#input-answer").on("input",checkAnswer);
//    answerInput.addEventListener("input", checkAnswer);
    difficulty = "easy";
    generateEquation();
}

function setDifficulty(){
    switch(this.id){
        case ("easy"):
            difficulty = "easy"
            break;
        case ("medium"):
            difficulty = "medium"
            break;
        case ("hard"):
            difficulty = "hard"
            break;
    }
    generateEquation();
}

function generateEquation(){
    switch(difficulty){
        case ("easy"):
            generateEasyEquation();
            break;
        case ("medium"):
            generateMediumEquation();
            break;
        case ("hard"):
            //generateHardEquation();
            break;
    }
}

function checkAnswer(){
    
    if($("#inputAnswer").val() == answer){
        alert("answer correct");
//        answerInput.classList.remove("blink");
        generateEquation();
//        answerInput.classList.add("blink");
    }
}