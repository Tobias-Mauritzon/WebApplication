package com.awesomeGames.controller;

import com.awesomeGames.model.dao.HighScoreDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.view.UserScoresView;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Data;

/**
 * Controller class for user scores component
 * @author Joachim Antfolk
 */
@RequestScoped
@Named
@Data
public class UserScoresController implements Serializable{
    @EJB
    private HighScoreDAO highScoreDAO;
    
    @EJB
    private UserAccountDAO userAccountDAO;
    
    @Inject
    private UserScoresView userScoresView;
    
    @Inject
    private FacesContext facesContext;         
    
    /**
     * Finds all scores for the current user in descending order and adds them to view. 
     */
    public void findScores() {
        String user = facesContext.getExternalContext().getUserPrincipal().getName();
        List<HighScore> list = highScoreDAO.findHighscoresWithUser(userAccountDAO.findUserWithName(user));
        
        userScoresView.setScoreList(list);
    }
    
    /**
     * Deletes all scores for the current user.
     */
    public void deleteAllUserScores() {
        String user = facesContext.getExternalContext().getUserPrincipal().getName();   
        if(highScoreDAO.deleteHighScoresWithUserName(user)){
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Scores Deleted!", null));
        }else{
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Can't delete scores!", null));
        }
    }
}
