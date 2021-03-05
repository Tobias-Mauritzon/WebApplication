/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.view;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.HighScoreDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.UserAccount;
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
    private UserAccountDAO userDAO;
    
    @EJB
    private GameDAO gameDAO;
    
    private String game;
    private List<HighScoreEntity> highScores1;;
    private List<HighScoreEntity> highScores2;
    
    @PostConstruct
    private void init() {
        String str = Faces.getViewId();
        str = str.split("\\.")[0];
        game = str.substring(1);
        updateHighscoreListForGameWithName(game);
    }
    
    private void updateHighscoreListForGameWithName(String gameName){
        List<HighScore> tempHighScore = highScoreDAO.findHighscoresWithGamename(gameName);
        highScores2 = new ArrayList<>();
        highScores1 = new ArrayList<>();
        
        for(int i = tempHighScore.size()-1; i >= 0; i--){
            if(i <= 4){
                String name = tempHighScore.get(i).getUserAccount().getName();
                highScores1.add(0 , new HighScoreEntity(name, i+1));    
            }else{
                HighScore highScore = tempHighScore.get(i);
//                tempHighScore.remove(i);
                highScores2.add(0 , new HighScoreEntity(highScore.getUserAccount().getName(), i+1));
            }
        }
    }
    
    public void newHighScore(String gameName, String userName, int score){
        Game game = gameDAO.findGameMatchingName(gameName);
        UserAccount user = userDAO.findUsersWithUsermail(userName).get(0);
        HighScore highScore = new HighScore(game, user, score);
        highScoreDAO.create(highScore); 
    }
}