/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.view;

import com.lab3.model.dao.CommentDAO;
import com.lab3.model.dao.GameDAO;
import com.lab3.model.entity.Game;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Data;

/**
 *
 * @author Matteus
 */
@ViewScoped
@Named
@Data
public class FeaturedGamesView implements Serializable{
    
    @EJB
    private GameDAO gameDAO;
    
    @EJB
    private CommentDAO commentDAO;
    
    private Game mostCommentedGame;
    
    @PostConstruct
    private void init() {
        String mostCommentedGameName = commentDAO.findsGameNameWithMostComments();
        mostCommentedGame = gameDAO.findGameMatchingName(mostCommentedGameName);
    }
    
}
