/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.controller;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.HighScoreDAO;
import com.lab3.model.entity.HighScore;
import com.lab3.view.CurrentGameView;
import com.lab3.view.GameCardsView;
import java.io.IOException;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Faces;

/**
 *
 * @author lerbyn
 */
@RequestScoped
@Named
public class GameController {
    
    @EJB
    private GameDAO gameDAO;
    
    @Inject
    private CurrentGameView currentGameView;
    
    @Inject
    private HighScoreDAO highScoreDAO;
    
    public String getJavaScriptPath() {
        String str = Faces.getViewId();
        str = str.split("\\.")[0];
        str = str.substring(1);
        System.out.println("spelnamn:" + str);
        return gameDAO.findJavaScriptPathByName(str);
    }
    
    public String getJavaScriptPath(String str) {
        return gameDAO.findJavaScriptPathByName(str);
    }
    
       
    public void setGameAndRedirect(String game) throws IOException {
        System.out.println("game set to: " + game);
        currentGameView.setGame(game);
        FacesContext.getCurrentInstance().getExternalContext().redirect("game.xhtml");
    }
    
    public void setContext(String user, String game){
        System.out.println("gameset: " + game);
        currentGameView.setGameObject(game);
        
        System.out.println("userset: " + user);
        currentGameView.setUser(user);
    }
    
    public void setHighScore() {
        String highscore = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("highscore");
        if(currentGameView.getGame() != null && currentGameView.getUser() != null) {
            highScoreDAO.create(new HighScore(currentGameView.getGameObject(),currentGameView.getUser(),Integer.parseInt(highscore)));
        }
        
    }
}

