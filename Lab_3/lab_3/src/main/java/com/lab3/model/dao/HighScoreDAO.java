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
 *
 * @author Lerbyn
 */
@Stateless
public class HighScoreDAO extends AbstractDAO<String,HighScore> {
    @Getter @PersistenceContext(unitName = "lab3")
    private EntityManager entityManager;

    public HighScoreDAO() {
        super(HighScore.class);
    }
    
    public List findHighscoreNumbersWithUserAndGame(Users user, Game game) {
        return findHighscoreNumbersWithUsermailAndGamename(user.getMail(), game.getName());
    }
    
    public List findHighscoreNumbersWithUsermailAndGamename(String mail, String gamename) {
       return entityManager.createQuery("SELECT h.highScore FROM HighScore h WHERE (h.users.mail LIKE :mail) AND (h.game.name LIKE :gamename) ORDER BY h.highScore DESC").setParameter("mail",mail).setParameter("gamename",gamename).getResultList();
    }
    
    public List findHighscoresWithUser(Users user) {
       return findHighscoresWithUsermail(user.getMail());
    }
    
    public List findHighscoresWithUsermail(String mail) {
       return entityManager.createQuery("SELECT h FROM HighScore h WHERE h.users.mail LIKE :mail ORDER BY h.highScore DESC").setParameter("mail",mail).getResultList();
    }
    
    public List findHighscoresWithGame(Game game) {
       return findHighscoresWithGamename(game.getName());
    }
    
    public List findHighscoresWithGamename(String name) {
       return entityManager.createQuery("SELECT h FROM HighScore h WHERE h.game.name LIKE :name ORDER BY h.highScore DESC").setParameter("name",name).getResultList();
    }
    
}