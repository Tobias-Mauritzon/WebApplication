/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.view;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.HighScoreDAO;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Data;
import org.omnifaces.util.Faces;


/**
 *
 * @author Simon
 */
@ViewScoped
@Named
@Data
public class HighScoreView implements Serializable {
    @EJB
    private HighScoreDAO highScoreDAO;
    
    @EJB
    private GameDAO gameDAO;
    
    private String game;
    private String test;
    private List<HighScore> highScores;
    
//    public String getHighScore(){
//        List returnList = new ArrayList<HighScore>();
//        highScoreDAO.findHighscoresWithGame(game);
//        System.out.println("");
//        return test;
//    }

    @PostConstruct
    private void init() {
        String str = Faces.getViewId();
        str = str.split("\\.")[0];
        game = str.substring(1);
        highScores = highScoreDAO.findHighscoresWithGamename(game);
        int counter = 0;
        for(HighScore score : highScores){
            System.out.println("counter: " + counter++);
            System.out.println(score.getUserAccount().getName());
            System.out.println(score.getHighScore());
            System.out.println("");
        }
        System.out.println(highScores);
        test = "Matti";
    }
}



