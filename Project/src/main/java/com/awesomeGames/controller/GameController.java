package com.awesomeGames.controller;

import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.HighScoreDAO;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.view.CurrentGameView;
import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Data;
import org.omnifaces.util.Messages;

/**
 * Controller for game bean
 *
 * @author David
 * @author Joachim Antfolk
 */
@RequestScoped
@Named
@Data
public class GameController {

    @EJB
    private GameDAO gameDAO;

    @Inject
    private CurrentGameView currentGameView;

    @Inject
    private HighScoreDAO highScoreDAO;

    /**
     * Finds a games JavaScript path
     *
     * @param str name of the game
     * @return path to games JavaScript
     */
    public String getJavaScriptPath(String str) {
        return gameDAO.findJavaScriptPathByName(str);
    }

    /**
     * Redirects context to game page
     *
     * @throws IOException if redirection fails
     */
    public void redirect() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("game.xhtml");
    }

    /**
     * Sets the game and redirects to game page
     *
     * @param game name of game to redirect to
     * @throws IOException if redirection fails
     */
    public void setGameAndRedirect(String game) throws IOException {
        currentGameView.setGame(game);
        FacesContext.getCurrentInstance().getExternalContext().redirect("game.xhtml");
    }

    /**
     * Sets user and game context
     *
     * @param user user name
     * @param game game name
     */
    public void setContext(String user, String game) {
        currentGameView.setGameObject(game);

        currentGameView.setUser(user);
    }

    /**
     * Sets highscore for the user for this game
     */
    public void setHighScore() {
        String highscore = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("highscore");
        if (currentGameView.getGame() != null && currentGameView.getUser() != null) {
            highScoreDAO.create(new HighScore(currentGameView.getGameObject(), currentGameView.getUser(), Integer.parseInt(highscore)));
            Messages.addGlobalInfo("High Score Submitted");
        } else {
            Messages.addGlobalError("User not logged in");
        }
    }
}
