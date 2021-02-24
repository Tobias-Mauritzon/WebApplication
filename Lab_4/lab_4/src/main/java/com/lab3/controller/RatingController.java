/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.controller;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.RatingDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Rating;
import com.lab3.view.RatingView;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Messages;

/**
 *
 * @author Tobias
 */
@RequestScoped
@Named
public class RatingController {

    @EJB
    private GameDAO gameDAO;

    @EJB
    private UserAccountDAO userAccountDAO;
    
    @EJB
    private RatingDAO ratingDAO;

    @Inject
    private RatingView ratingView;

    public boolean create(String userName) {
        boolean res = true;

        try {
            Rating r = new Rating(gameDAO.findGameMatchingName(ratingView.getGame()), userAccountDAO.findUsersWithName(userName), ratingView.getRating());
            ratingDAO.create(r);
        } catch (Exception e) {
            res = false;
            e.printStackTrace();
            Messages.addGlobalError("Rating could not be created");
        }

        return res;
    }
}
