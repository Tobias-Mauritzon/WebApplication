/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;

/**
 * DAO to the HighScore entity
 * @author Matteus, Lerbyn
 */
@Stateless
public class HighScoreDAO extends AbstractDAO<String,HighScore> {
    @Getter @PersistenceContext(unitName = "lab3")
    private EntityManager entityManager;

    public HighScoreDAO() {
        super(HighScore.class);
    }
    
    /**
     * Finds and returns the HighScore Numbers of the inputed User and Game from the database
     * @param user the user to get Highscores from
     * @param game the game to get Highscores from
     * @return List of HighScore Numbers from the mathcing user and game
     */
    public List findHighscoreNumbersWithUserAndGame(Users user, Game game) {
        return findHighscoreNumbersWithUsermailAndGamename(user.getMail(), game.getName());
    }
    
    /**
     * Finds and returns the HighScore Numbers of the inputed User mail and Gamename from the database
     * @param mail the user mail to get Highscores from
     * @param gamename the gamename to get Highscores from
     * @return List of HighScore Numbers from the mathcing user mail and gamename
     */
    public List findHighscoreNumbersWithUsermailAndGamename(String mail, String gamename) {
       return entityManager.createQuery("SELECT h.highScore FROM HighScore h WHERE (h.users.mail LIKE :mail) AND (h.game.name LIKE :gamename) ORDER BY h.highScore DESC").setParameter("mail",mail).setParameter("gamename",gamename).getResultList();
    }
    
    /**
     * Finds and returns the HighScores of the inputed User and Game from the database
     * @param user the user to get Highscores from
     * @param game the game to get Highscores from
     * @return List of HighScores from the mathcing user and game
     */
    public List findHighscoresWithUserAndGame(Users user, Game game) {
       return findHighscoresWithUsermailAndGamename(user.getMail(), game.getName());
    }
    
    /**
     * Finds and returns the HighScores of the inputed User mail and Gamename from the database
     * @param mail the user mail to get Highscores from
     * @param gamename the gamename to get Highscores from
     * @return List of HighScores from the mathcing user mail and gamename
     */
    public List findHighscoresWithUsermailAndGamename(String mail, String gamename) {
       return entityManager.createQuery("SELECT h FROM HighScore h WHERE (h.users.mail LIKE :mail) AND (h.game.name LIKE :gamename) ORDER BY h.highScore DESC").setParameter("mail",mail).setParameter("gamename",gamename).getResultList();
    }
    
    /**
     * Finds and returns the HighScores of the inputed User from the database
     * @param user the user to get Highscores from
     * @return List of HighScores from the mathcing user
     */
    public List findHighscoresWithUser(Users user) {
       return findHighscoresWithUsermail(user.getMail());
    }
    
    /**
     * Finds and returns the HighScores of the inputed User mail from the database
     * @param mail the user mail to get Highscores from
     * @return List of HighScores from the mathcing user mail
     */
    public List findHighscoresWithUsermail(String mail) {
       return entityManager.createQuery("SELECT h FROM HighScore h WHERE h.users.mail LIKE :mail ORDER BY h.highScore DESC").setParameter("mail",mail).getResultList();
    }
    
    /**
     * Finds and returns the HighScores of the inputed Game from the database
     * @param game the game to get Highscores from
     * @return List of HighScores from the mathcing and game
     */
    public List findHighscoresWithGame(Game game) {
       return findHighscoresWithGamename(game.getName());
    }
    
        /**
     * Finds and returns the HighScores of the inputed Gamename from the database
     * @param gamename the gamename to get Highscores from
     * @return List of HighScores from the mathcing gamename
     */
    public List findHighscoresWithGamename(String gamename) {
       return entityManager.createQuery("SELECT h FROM HighScore h WHERE h.game.name LIKE :name ORDER BY h.highScore DESC").setParameter("name",gamename).getResultList();
    }
    
}