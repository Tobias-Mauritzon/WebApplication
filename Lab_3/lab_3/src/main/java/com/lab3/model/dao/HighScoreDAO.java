/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.HighScore;
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
    
    public List findhighscoreWithUsernameAndGame(String game, String uid) {
        //not implemented correctly but works for testing
       return entityManager.createQuery("SELECT h.highScore FROM HighScore h WHERE h.users.name LIKE :username").setParameter("username",uid).getResultList();
    }
}