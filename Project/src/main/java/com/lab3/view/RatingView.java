/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.view;

import com.lab3.controller.RatingController;
import com.lab3.model.dao.RatingDAO;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.UserAccount;
import java.io.Serializable;
import java.util.LinkedList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Data;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author Tobias
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
//        String str = Faces.getViewId();
//        str = str.split("\\.")[0];
//        game = str.substring(1);
        
        game = currentGameView.getGame();
        getAverageRating();
    }

    public void getAverageRating() {
        Double rat = ratingDAO.avgRatingForGameName(game);

        avgRating = (int) Math.round(rat.doubleValue());
    }
}
