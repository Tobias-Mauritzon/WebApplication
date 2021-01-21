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
var partC;
var signPartA;
var signPartB;
var answer;
var difficulty;
var diffucultyButtons;

function generateEasyEquation(){
    partA = randomNumber(9) + 1;
    partB = randomNumber(9) + 1;
    signPartA = (randomNumber(1)?"+":"-");
    answer = partA + (signPartA=="+"?1:-1) * partB;
    setEquation();
}

function generateMediumEquation(){
    partA = randomNumber(9) + 1;
    partB = randomNumber(9) + 1;
    signPartA = "*"; 
    answer = partA * partB;
    setEquation();
}

function generateHardEquation(){
    partA = randomNumber(99) + 1;
    partB = randomNumber(99) + 1;
    partC = randomNumber(99) + 1;
    signPartA = "*";
    signPartB = "+"
    answer = partA * partB + partC;
    setHardEquation();
}

function setEquation(){
    $("#input-answer").val("");
    var equation = partA + " " + signPartA + " " + partB;
    $("#equation").text(equation);
}

function setHardEquation(){
    $("#input-answer").val("");
    var equation = partA + " " + signPartA + " " + partB + " " + signPartB + " " + partC;
    $("#equation").text(equation);
}

function randomNumber(max){
    return Math.floor(Math.random() * (max+1));
}

function init(){
    $("#easy").click(setDifficulty);
    $("#medium").click(setDifficulty);
    $("#hard").click(setDifficulty);
    
    $("#input-answer").on("input",checkAnswer);
    
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
            generateHardEquation();
            break;
    }
}

function checkAnswer(){
    if($("#input-answer").val() == answer){
        generateEquation();
    }
}