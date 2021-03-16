/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awesomeGames.view;

import com.awesomeGames.model.dao.RatingDAO;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Data;

/**
 *
 * @author Tobias
 * @author David
 */
@ViewScoped
@Named
@Data
public class RatingView implements Serializable {

    @EJB
    private RatingDAO ratingDAO;

    @Inject
    private CurrentGameView currentGameView;

    private String game;
    private Integer rating;
    private int avgRating;

    @PostConstruct
    private void init() {
        game = currentGameView.getGame();
        getAverageRating();
    }

    /**
     * Method to get averageRating retrieves from database
     */
    public void getAverageRating() {
        Double rat = ratingDAO.avgRatingForGameName(game);
        avgRating = (int) Math.round(rat.doubleValue());
    }

    /**
     * Only meant for testing purposes DO NOT USE!
     */
    public void testInit() {
        init();
    }
}
