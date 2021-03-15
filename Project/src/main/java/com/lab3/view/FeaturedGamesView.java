package com.lab3.view;

import com.lab3.model.dao.CommentDAO;
import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.RatingDAO;
import com.lab3.model.entity.Game;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Data;

/**
 * View for the FeaturedGames template
 *
 * @author Matteus
 */
@ViewScoped
@Named
@Data
public class FeaturedGamesView implements Serializable {

    @EJB
    private GameDAO gameDAO;

    @EJB
    private CommentDAO commentDAO;

    @EJB
    private RatingDAO ratingDAO;

    private Game mostCommentedGame;

    private Game newestGame;

    private Game highestRatedGame;
    
    @Inject
    private FacesContext facesContext;

    @PostConstruct
    private void init() {
        String mostCommentedGameName = commentDAO.findsGameNameWithMostComments();
        mostCommentedGame = gameDAO.findGameMatchingName(mostCommentedGameName); 
        if(mostCommentedGame == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Couldn't find most Commented Game", null));
        }
        
        newestGame = gameDAO.findNewestGame();
        if(newestGame == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Couldn't find Newest Game", null));
        }
        
        highestRatedGame = ratingDAO.findsHighestAvgRatedGame();
        if(highestRatedGame == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Couldn't find Highest Rated Game", null));
        }
    }

    /**
     * Method to run init from public scope, is only supposed to be run from
     * tests
     */
    public void testInit() {
        init();
    }
}
