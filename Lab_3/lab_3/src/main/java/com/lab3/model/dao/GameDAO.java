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
public class GameDAO extends AbstractDAO<String,Game> {
    @Getter @PersistenceContext(unitName = "lab3")
    
    private EntityManager entityManager;
    
    public GameDAO() {
        super(Game.class);
    }
    
    /**
     * Find and returns the game with the specified name
     * @return ...
     */
    public Game findGameMatchingName(String game) {
        
        List<Game> returnGameList = entityManager.createQuery("SELECT g FROM Game g WHERE (g.name LIKE :gameName)")
                .setParameter("gameName",game).getResultList();
        
        if(returnGameList.isEmpty()){
            return null;
        }
        else{
            return returnGameList.get(0);
        }

//        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    /**
     * Finds and returns all games from the database
     * @return List of all games
     */
    public List allGames() {
        return entityManager.createQuery("SELECT g FROM Game g").getResultList();
    }
}