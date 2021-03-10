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
import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
/**
 *
 * @author David
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
    
    public String getJavaScriptPath(String str) {
        return gameDAO.findJavaScriptPathByName(str);
    }
    
    public void redirect() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("game.xhtml");
    }
       
    public void setGameAndRedirect(String game) throws IOException {
        currentGameView.setGame(game);
        FacesContext.getCurrentInstance().getExternalContext().redirect("game.xhtml");
    }
    
    public void setContext(String user, String game){
        currentGameView.setGameObject(game);
        
        currentGameView.setUser(user);
    }
    
    public void setHighScore() {
        String highscore = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("highscore");
        if(currentGameView.getGame() != null && currentGameView.getUser() != null) {
            highScoreDAO.create(new HighScore(currentGameView.getGameObject(),currentGameView.getUser(),Integer.parseInt(highscore)));
        }
        
    }
}

