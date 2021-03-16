package com.awesomeGames.controller;

import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.RatingDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
import com.awesomeGames.model.entity.key.RatingPK;
import com.awesomeGames.view.RatingView;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.transaction.UserTransaction;
import lombok.Data;

/**
 *
 * @author Tobias, Simon, David, William
 */
@RequestScoped
@Named
@Data
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

    @Inject
    private FacesContext facesContext;

    /**
     * Creates a rating for the given user on the game page
     *
     * @param userName Unique name of the user
     * @return false if rating could not be created otherwise true
     */
    public boolean create(String userName) {
        boolean res = true;
        boolean signedIn = true;
        boolean gameFound = true;

        UserAccount user = userAccountDAO.findUserWithName(userName);
        if (user == null) {
            signedIn = false;
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not found", null));
        }
        Game game = gameDAO.findGameMatchingName(ratingView.getGame());
        if (game == null) {
            gameFound = false;
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Game not found", null));
        }

        if (signedIn && gameFound) {
            if (ratingDAO.findRatingsByGameNameAndUserMail(game.getName(), user.getMail()) == null) {
                Rating r = new Rating(game, user, ratingView.getRating());
                ratingDAO.create(r);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rating created", null));
            } else {
                ratingDAO.updateRatingForGame(game.getName(), user.getMail(), ratingView.getRating());
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rating updated", null));
            }
        } else {
            res = false;
        }

        setAverageRating();
        return res;
    }

    public void setAverageRating() {
        Double avgRating;

        Game game = gameDAO.findGameMatchingName(ratingView.getGame());
        if (game == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Game not found", null));
        } else {
            avgRating = ratingDAO.avgRatingForGameName(game.getName());
            ratingView.setAvgRating((int) Math.round(avgRating.doubleValue()));
        }
    }

    public int getAverageRating() {
        setAverageRating();
        return ratingView.getAvgRating();
    }

    public boolean removeRating(String userName) {

        boolean res = true;
        boolean signedIn = true;
        boolean gameFound = true;

        UserAccount user = userAccountDAO.findUserWithName(userName);
        if (user == null) {
            signedIn = false;
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not found or logged in", null));
        }
        Game game = gameDAO.findGameMatchingName(ratingView.getGame());
        if (game == null) {
            gameFound = false;
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Game not found", null));
        }

        if (signedIn && gameFound) {
            try {
                if (utx == null) {
                    throw new IllegalArgumentException("Rating not found");
                }
                if (!(ratingDAO.findRatingsByGameNameAndUserMail(game.getName(), user.getMail()) == null)) {
                    utx.begin();
                    Rating rating = ratingDAO.find(new RatingPK(game.getName(), user.getMail()));
                    ratingDAO.remove(rating);
                    utx.commit();
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rating deleted", null));
                }
            } catch (Exception e) {
                res = false;
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "rating not removed reason" + e.toString(), null));
            }
        } else {
            res = false;
        }
        setAverageRating();
        return res;
    }
}
