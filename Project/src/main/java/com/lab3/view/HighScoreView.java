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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    
    private String game;
    private List<HighScore> highScores1;
    private Map<String, Integer> numerated;
    private List<HighScore> highScores2;

    @PostConstruct
    private void init() {
        String str = Faces.getViewId();
        str = str.split("\\.")[0];
        game = str.substring(1);
        updateHighscoreForGameWithName(game);
    }
    
    private void updateHighscoreForGameWithName(String game){
        highScores1 = highScoreDAO.findHighscoresWithGamename(game);
        highScores2 = new ArrayList<>();
        numerated = new HashMap<>();
        
        for(int i = highScores1.size()-1; i >= 0; i--){
            if(i <= 4){
                String name = highScores1.get(i).getUserAccount().getName();
                numerated.put(name, i+1);    
            }else{
                HighScore highScore = highScores1.get(i);
                highScores1.remove(i);
                highScores2.add(0, highScore);
                numerated.put(highScore.getUserAccount().getName(), i+1);
            }
        }
        System.out.println("11111111111: " + highScores1.size());
        System.out.println("22222222222: " + highScores2.size());
    }
}



