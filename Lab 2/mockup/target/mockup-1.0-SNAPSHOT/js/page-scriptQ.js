/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    var answerInput;
    var partA;
    var partB;
    var signPart;
    var answer;
    var difficulty;
    
    $("#optionEasy").click(setDifficulty);
    $("#optionMedium").click(setDifficulty);
    $("#optionHard").click(setDifficulty);
    answerInput = document.getElementById("inputAnswer");
    answerInput.addEventListener("input", checkAnswer);
    difficulty = "easy";
    generateEquation();
    
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
        $("#inputAnswer").val('');
        var equation = partA + " " + signPart + " " + partB + " =";
        $("#equation").text(equation);
    }

    function randomNumber(max){
        return Math.floor(Math.random() * (max+1));
    }

    function setDifficulty(){
        switch(this.id){
            case ("optionEasy"):
                difficulty = "easy"
                break;
            case ("optionMedium"):
                difficulty = "medium"
                break;
            case ("optionHard"):
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

        if(answerInput.value == answer){

            answerInput.classList.remove("blink");
            generateEquation();
            answerInput.classList.add("blink");
        }
    }
});