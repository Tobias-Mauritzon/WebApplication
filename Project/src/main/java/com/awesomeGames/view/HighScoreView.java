package com.awesomeGames.view;

import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.HighScoreDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.UserAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Data;

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

    @Inject
    private CurrentGameView currentGameView;

    private List<HighScore> highScores1;
    ;
    private List<HighScore> highScores2;

    @PostConstruct
    private void init() {
        updateHighscoreListForGameWithName(currentGameView.getGame());
    }

    /**
     * Updates scoreboardList for said game
     * @param gameName species for which game to update the scoreboard
     */
    public void updateHighscoreListForGameWithName(String gameName) {
        List<HighScore> tempHighScore = highScoreDAO.findTenHighscoresWithGamename(gameName);
        highScores2 = new ArrayList<>();
        highScores1 = new ArrayList<>();

        for (int i = 10; i >= 0; i--) {
            HighScore hScore;
            if (i <= 4) {
                if (i < tempHighScore.size()) {
                    hScore = tempHighScore.get(i);
                } else {
                    Game tempgame = gameDAO.findGameMatchingName(gameName);
                    UserAccount tempUser = new UserAccount("", "", "USER", "");
                    hScore = new HighScore(tempgame, tempUser, 0);
                }
                highScores1.add(0, hScore);
            } else {
                if (i < tempHighScore.size()) {
                    hScore = tempHighScore.get(i);
                } else {
                    Game tempgame = gameDAO.findGameMatchingName(gameName);
                    UserAccount tempUser = new UserAccount("", "", "USER", "");
                    hScore = new HighScore(tempgame, tempUser, 0);
                }
                highScores2.add(0, hScore);
            }
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
