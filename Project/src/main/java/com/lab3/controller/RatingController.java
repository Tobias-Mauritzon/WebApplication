/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.controller;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.RatingDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.UserAccount;
import com.lab3.model.entity.key.RatingPK;
import com.lab3.view.RatingView;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Messages;
import java.lang.Math;
import javax.annotation.Resource;
import javax.transaction.UserTransaction;

/**
 *
 * @author Tobias, Simon
 */
@RequestScoped
@Named
public class RatingController {

    @Resource
    UserTransaction utx;

    @EJB
    private GameDAO gameDAO;

    @EJB
    private UserAccountDAO userAccountDAO;

    @EJB
    private RatingDAO ratingDAO;

    @Inject
    private RatingView ratingView;

    /**
     * Creates a rating for the given user on the game page
     *
     * @param userName
     * @return false if rating could not be created otherwise true
     */
    public boolean create(String userName) {
        boolean res = true;
        boolean signedIn = true;
        boolean gameFound = true;

        UserAccount user = new UserAccount("Necessary@Necessary.Necessary", "Necessary", "user", "NecessaryPass");
        Game game = new Game();

        try {
            user = userAccountDAO.findUserWithName(userName);
        } catch (Exception e) {
            signedIn = false;
            Messages.addGlobalError("User not found or logged in");
        }

        try {
            game = gameDAO.findGameMatchingName(ratingView.getGame());
        } catch (Exception e) {
            gameFound = false;
            Messages.addGlobalError("Game not found");
        }

        if (signedIn && gameFound) {
            if ((ratingDAO.findRatingsByGameNameAndUserMail(game.getName(), user.getMail()) == null)) {
                try {
                    Rating r = new Rating(game, user, ratingView.getRating());
                    ratingDAO.create(r);
                    Messages.addGlobalInfo("Rating created");
                } catch (Exception e) {
                    res = false;
                    e.printStackTrace();
                    Messages.addGlobalError("Rating could not be created");
                }
            } else {
                try {
                    Rating r = new Rating(game, user, ratingView.getRating());
                    ratingDAO.updateRatingForGame(game.getName(), user.getMail(), ratingView.getRating());
                    Messages.addGlobalInfo("Rating updated");
                } catch (Exception e) {
                    res = false;
                    e.printStackTrace();
                    Messages.addGlobalError("Rating could not be updated");
                }
            }

        }

        setAverageRating();
        return res;
    }

    public void setAverageRating() {
        Double avgRating;
        Game game = new Game();

        try {
            game = gameDAO.findGameMatchingName(ratingView.getGame());
        } catch (Exception e) {
            Messages.addGlobalError("Game not found");
        }

        avgRating = ratingDAO.avgRatingForGameName(game.getName());
        ratingView.setAvgRating((int) Math.round(avgRating.doubleValue()));
    }

    public int getAverageRating() {
        setAverageRating();
        return ratingView.getAvgRating();
    }

    public boolean removeRating(String userName) {

        boolean res = true;
        boolean signedIn = true;
        boolean gameFound = true;

        UserAccount user = new UserAccount();
        Game game = new Game();

        try {
            user = userAccountDAO.findUserWithName(userName);
        } catch (Exception e) {
            signedIn = false;
            Messages.addGlobalError("User not found or logged in");
        }

        try {
            game = gameDAO.findGameMatchingName(ratingView.getGame());
        } catch (Exception e) {
            gameFound = false;
            Messages.addGlobalError("Game not found");
        }

        if (signedIn && gameFound) {
            if ((ratingDAO.findRatingsByGameNameAndUserMail(game.getName(), user.getMail()) == null)) {
                Messages.addGlobalError("No rating found");
            } else {
                try {
                    utx.begin();
                    Rating rating = ratingDAO.find(new RatingPK(game.getName(), user.getMail()));
                    ratingDAO.remove(rating);
                    utx.commit();
                    Messages.addGlobalInfo("Rating deleted");
                } catch (Exception e) {
                    res = false;
                    e.printStackTrace();
                    Messages.addGlobalError("Rating could not be deleted");
                }
            }

        }
        setAverageRating();
        return res;
    }
}
