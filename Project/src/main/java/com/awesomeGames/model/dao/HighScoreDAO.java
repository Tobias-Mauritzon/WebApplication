package com.awesomeGames.model.dao;

import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.UserAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;

/**
 * DAO to the HighScore entity
 *
 * @author Matteus
 * @author David
 * @author Simon
 */
@Stateless
public class HighScoreDAO extends AbstractDAO<String, HighScore> {

    @Getter
    @PersistenceContext(unitName = "awesomeGamesPersistence")
    private EntityManager entityManager;

    public HighScoreDAO() {
        super(HighScore.class);
    }

    /**
     * Finds and returns the HighScore Numbers of the inputed User and Game from
     * the database
     *
     * @param user the user to get Highscores from
     * @param game the game to get Highscores from
     * @return List of HighScore Numbers from the mathcing user and game
     */
    public List findHighscoreNumbersWithUserAndGame(UserAccount user, Game game) {
        return findHighscoreNumbersWithUsermailAndGamename(user.getMail(), game.getName());
    }

    /**
     * Finds and returns the HighScore Numbers of the inputed User mail and
     * Gamename from the database
     *
     * @param mail the user mail to get Highscores from
     * @param gamename the gamename to get Highscores from
     * @return List of HighScore Numbers from the mathcing user mail and
     * gamename
     */
    public List findHighscoreNumbersWithUsermailAndGamename(String mail, String gamename) {
        return entityManager.createQuery("SELECT h.highScore FROM HighScore h WHERE (h.userAccount.mail LIKE :mail) AND (h.game.name LIKE :gamename) ORDER BY h.highScore DESC").setParameter("mail", mail).setParameter("gamename", gamename).getResultList();
    }

    /**
     * Finds and returns the HighScores of the inputed User and Game from the
     * database
     *
     * @param user the user to get Highscores from
     * @param game the game to get Highscores from
     * @return List of HighScores from the mathcing user and game
     */
    public List findHighscoresWithUserAndGame(UserAccount user, Game game) {
        return findHighscoresWithUsermailAndGamename(user.getMail(), game.getName());
    }

    /**
     * Finds and returns the HighScores of the inputed User mail and Gamename
     * from the database
     *
     * @param mail the user mail to get Highscores from
     * @param gamename the gamename to get Highscores from
     * @return List of HighScores from the mathcing user mail and gamename
     */
    public List findHighscoresWithUsermailAndGamename(String mail, String gamename) {
        return entityManager.createQuery("SELECT h FROM HighScore h WHERE (h.userAccount.mail LIKE :mail) AND (h.game.name LIKE :gamename) ORDER BY h.highScore DESC").setParameter("mail", mail).setParameter("gamename", gamename).getResultList();
    }

    /**
     * Finds and returns the HighScores of the inputed User from the database
     *
     * @param user the user to get Highscores from
     * @return List of HighScores from the mathcing user
     */
    public List findHighscoresWithUser(UserAccount user) {
        return findHighscoresWithUsermail(user.getMail());
    }

    /**
     * Finds and returns the HighScores of the inputed User mail from the
     * database
     *
     * @param mail the user mail to get Highscores from
     * @return List of HighScores from the mathcing user mail
     */
    public List findHighscoresWithUsermail(String mail) {
        return entityManager.createQuery("SELECT h FROM HighScore h WHERE h.userAccount.mail LIKE :mail ORDER BY h.highScore DESC").setParameter("mail", mail).getResultList();
    }

    /**
     * Finds and returns the HighScores of the inputed Game from the database
     *
     * @param game the game to get Highscores from
     * @return List of HighScores from the mathcing and game
     */
    public List findHighscoresWithGame(Game game) {
        return findTenHighscoresWithGamename(game.getName());
    }

    /**
     * Finds and returns ten HighScores of the inputed Game from the database
     *
     * @param gamename the gamename to get Highscores from
     * @return List of HighScores from the mathcing gamename
     */
    public List findTenHighscoresWithGamename(String gamename) {
        return entityManager.createQuery("SELECT h FROM HighScore h WHERE h.game.name LIKE :name ORDER BY h.highScore DESC").setParameter("name", gamename).setMaxResults(10).getResultList();
    }
}
