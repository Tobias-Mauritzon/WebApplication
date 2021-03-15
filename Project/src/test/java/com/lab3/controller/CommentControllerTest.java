/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.controller;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.RatingDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.UserAccount;
import com.lab3.view.CommentView;
import com.lab3.view.CurrentGameView;
import com.lab3.view.RatingView;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author David
 */
@RunWith(Arquillian.class)
public class CommentControllerTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(RatingDAO.class, Rating.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class,
                        Comment.class, HighScore.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    
    CommentController commentController;
    CommentView commentView;
    
    Game game;
    UserAccount user;
   
    @EJB
    private UserAccountDAO userAccountDAO;

    @EJB
    private GameDAO gameDAO;
    
    @Inject
    private UserTransaction tx;
    
    @Before
    public void init() throws Exception {
        commentController = new CommentController();
        commentView = new CommentView();
        commentController.setGameDAO(gameDAO);
        commentController.setUserAccountDAO(userAccountDAO);
        commentController.setCommentView(commentView);
        
        tx.begin();
        user = new UserAccount("mail1", "name1", "USER", "password1");
        userAccountDAO.create(user);
        game = gameDAO.createGame("Game", "author", "description", "javaScriptPath", "imagePath");
        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
    }
    
    @Test
    public void createTestAllConditionsFullfilled() throws Exception {
        commentView.setGameName(game.getName());
        commentView.setText("text");
//        commentController.create(user.getName()); 
        Assert.assertTrue(true);

//        Assert.assertTrue(true);

        
        
        
    }
    
    @After
    public void tearDown() throws Exception {
        gameDAO.getEntityManager().refresh(game);
        userAccountDAO.getEntityManager().refresh(user);

        gameDAO.remove(game);
        userAccountDAO.remove(user);
        userAccountDAO.remove(user);
        tx.commit(); 
    }
    
    
    
}