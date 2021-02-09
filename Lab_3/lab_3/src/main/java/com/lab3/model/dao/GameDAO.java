/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.Game;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;

/**
 *
 * @author Matteus
 */
@Stateless
public class GameDAO extends AbstractDAO<Game> {
    @Getter @PersistenceContext(unitName = "lab3")
    
    private EntityManager entityManager;
    
    public GameDAO() {
        super(Game.class);
    }
    public List<Game>findGameMatchingName() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public List allGames() {
        return entityManager.createQuery("SELECT g FROM Game g").getResultList();
    }
//    
//    public List findRatingWithName(String name) {
//        return entityManager.createQuery("SELECT g.averageRating FROM Game g WHERE g.name LIKE :gameName").setParameter("gameName",name).getResultList();
//    }
//    
//    public List findHighScoreWithName(String name) {
//       // return entityManager.createQuery("SELECT g.highScore FROM Game g WHERE g.name LIKE :gameName ORDER BY e.highScore DESC").setParameter("gameName",name).getResultList();
//       return entityManager.createQuery("SELECT g.highScore FROM Game g WHERE g.name LIKE :gameName").setParameter("gameName",name).getResultList();
//    }
}