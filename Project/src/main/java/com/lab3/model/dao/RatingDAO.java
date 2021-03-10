package com.lab3.model.dao;

import com.lab3.model.entity.Game;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.key.RatingPK;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.Getter;

/**
 * DAO for the Rating entity
 *
 * @author Matteus
 * @author Tobias
 */
@Stateless
public class RatingDAO extends AbstractDAO<RatingPK, Rating> {

    @Getter
    @PersistenceContext(unitName = "lab3")
    private EntityManager entityManager;

    public RatingDAO() {
        super(Rating.class);
    }

    /**
     * Finds all ratings for speccified name in the database
     *
     * @param name
     * @return a List of all ratings made by speciffied name
     */
    public List findAllRatingsByUsername(String name) {
        return entityManager.createQuery("SELECT r.rating FROM Rating r WHERE r.userAccount.name LIKE :username").setParameter("username", name).getResultList();
    }

    /**
     * Finds all ratings for speccified game in the database
     *
     * @param game name
     * @return a List of all ratings for speciffied game
     */
    public List findAllRatingsForGame(String game) {
        return entityManager.createQuery("SELECT r.rating FROM Rating r WHERE r.game.name LIKE :gameName").setParameter("gameName", game).getResultList();
    }

    /**
     * Find rating for specific game and mail from database
     *
     * @param game name
     * @param mail of user
     * @return found Integer ratring if found and null otherwise
     */
    public Integer findRatingsByGameNameAndUserMail(String game, String mail) {
        List<Integer> list;
        list = entityManager.createQuery("SELECT r.rating FROM Rating r WHERE (r.game.name LIKE :gameName AND r.userAccount.mail LIKE :userMail)")
                .setParameter("gameName", game)
                .setParameter("userMail", mail).getResultList();

        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * Find rating for specific game and mail from database and updates it
     *
     * @param gameName of the game
     * @param mail of user
     * @param newRating the new game rating
     * @return true if update worked as instended otherwise false
     */
    public boolean updateRatingForGame(String gameName, String mail, int newRating) {
        int res = entityManager.createQuery("UPDATE Rating r SET r.rating = :rating WHERE (r.game.name LIKE :gameName AND r.userAccount.mail LIKE :userMail)")
                .setParameter("gameName", gameName)
                .setParameter("rating", newRating)
                .setParameter("userMail", mail).executeUpdate();
        if (res == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Finds all rating for specific game and returns the average rating
     *
     * @param gameName name of game
     * @return Average rating for game in integer format if no rating for game
     * is found it returns 0
     */
    public Double avgRatingForGameName(String gameName) {
        List<Double> list = entityManager.createQuery("SELECT AVG(r.rating) FROM Rating r WHERE r.game.name LIKE :gameName")
                .setParameter("gameName", gameName)
                .getResultList();
        Double res = list.get(0);

        if (res == null) {
            return 0.0;
        } else {
            return res;
        }
    }

    /**
     * Finds the Highest Average Rated Game
     *
     * @return Game
     */
    public Game findsHighestAvgRatedGame() {
        TypedQuery<Game> q = entityManager.createQuery("SELECT r.game FROM Rating r GROUP BY r.game ORDER BY AVG(r.rating) DESC", Game.class);
        return q.getResultList().get(0);
    }
}
