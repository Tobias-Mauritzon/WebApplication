package com.lab3.view;

import com.lab3.model.dao.CommentDAO;
import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.RatingDAO;
import com.lab3.model.entity.Game;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Data;
import org.omnifaces.util.Messages;

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

    @PostConstruct
    private void init() {
        try {
            String mostCommentedGameName = commentDAO.findsGameNameWithMostComments();
            mostCommentedGame = gameDAO.findGameMatchingName(mostCommentedGameName);

            newestGame = gameDAO.findNewestGame();

            highestRatedGame = ratingDAO.findsHighestAvgRatedGame();
        } catch (Exception e) {
            Messages.addGlobalError("Couldn't find most Commented Game, Newest Game and or Highest Rated Game");
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
