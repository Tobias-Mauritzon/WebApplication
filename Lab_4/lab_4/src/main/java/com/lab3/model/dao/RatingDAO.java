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
    
    /**Finds all ratings for speccified name in the database
     * 
     * @param name
     * @return a List of all ratings made by speciffied name
     */
    public List findAllRatingsByUsername(String name) {
        return entityManager.createQuery("SELECT r.rating FROM Rating r WHERE r.userAccount.name LIKE :username").setParameter("username",name).getResultList();
    }
    
    /**Finds all ratings for speccified game in the database
     * 
     * @param game name
     * @return a List of all ratings for speciffied game
     */
    public List findAllRatingsForGame(String game) {
        return entityManager.createQuery("SELECT r.rating FROM Rating r WHERE r.game.name LIKE :gameName").setParameter("gameName",game).getResultList();
    }
    
    /**Find rating for specific game and mail from database
     * 
     * @param game name
     * @param mail of user
     * @return found Integer ratring if found and null otherwise
     */
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
    
    /**Find rating for specific game and mail from database and updates it
     * 
     * @param game name
     * @param mail of user
     * @param newRating the new game rating
     * @return true if update worked as instended otherwise false
     */
    public boolean updateRatingForGame(String gameName, String mail, int newRating){ 
        int res = entityManager.createQuery("UPDATE Rating r SET r.rating = :rating WHERE (r.game.name LIKE :gameName AND r.userAccount.mail LIKE :userMail)")
                .setParameter("gameName",gameName)
                .setParameter("rating", newRating)
                .setParameter("userMail", mail).executeUpdate();
        if(res == 1){
            return true;
        }else{
            return false;
        }       
    }
}