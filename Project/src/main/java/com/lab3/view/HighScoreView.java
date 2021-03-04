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
import java.util.Iterator;
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
    
    private String game;
    private List<HighScore> highScores;

    @PostConstruct
    private void init() {
        String str = Faces.getViewId();
        str = str.split("\\.")[0];
        game = str.substring(1);
        updateHighscoreForGameWithName(game);
    }
    
    private void updateHighscoreForGameWithName(String game){
        highScores = highScoreDAO.findHighscoresWithGamename(game);
        int counter = 1;
        for(int i = 0; i < highScores.size(); i++){
            String name = highScores.get(i).getUserAccount().getName();
            highScores.get(i).getUserAccount().setName(counter + ": " + name);
            counter++;
        }
    }
}



