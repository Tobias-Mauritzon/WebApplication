/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.Rating;
import com.lab3.model.entity.key.RatingPK;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;

/**
 *
 * @author Matteus
 * @author Tobias
 */
@Stateless
public class RatingDAO extends AbstractDAO<RatingPK,Rating> {
    @Getter @PersistenceContext(unitName = "lab3")
    private EntityManager entityManager;
    
    public RatingDAO() {
        super(Rating.class);
    }
    public List findAllRatingsByUsername(String name) {
        return entityManager.createQuery("SELECT r.rating FROM Rating r WHERE r.userAccount.name LIKE :username").setParameter("username",name).getResultList();
    }
    
    public List findAllRatingsForGame(String game) {
        return entityManager.createQuery("SELECT r.rating FROM Rating r WHERE r.game.name LIKE :gameName").setParameter("gameName",game).getResultList();
    }

    //combine uname and game rating
    public Integer findRatingsByGameNameAndUserMail(String game, String mail){ 
        List<Integer> list;
        list = entityManager.createQuery("SELECT r.rating FROM Rating r WHERE (r.game.name LIKE :gameName AND r.userAccount.mail LIKE :userMail)")
                .setParameter("gameName",game)
                .setParameter("userMail", mail).getResultList();
        
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }
}