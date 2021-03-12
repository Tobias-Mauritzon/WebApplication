// /*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.lab3.controller;
//
//
//import com.lab3.model.dao.GameDAO;
//import com.lab3.model.dao.RatingDAO;
//import com.lab3.model.dao.UserAccountDAO;
//import com.lab3.model.entity.Comment;
//import com.lab3.model.entity.Game;
//import com.lab3.model.entity.HighScore;
//import com.lab3.model.entity.Rating;
//import com.lab3.model.entity.UserAccount;
//import com.lab3.view.CurrentGameView;
//import com.lab3.view.RatingView;
//import javax.ejb.EJB;
//import javax.inject.Inject;
//import javax.transaction.UserTransaction;
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.WebArchive;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
///**
// *
// * @author David
// */
//@RunWith(Arquillian.class)
//public class RatingControllerTest {
//    @Deployment
//    public static WebArchive createDeployment() {
//        return ShrinkWrap.create(WebArchive.class)
//                .addClasses(RatingDAO.class, Rating.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class,
//                        Comment.class, HighScore.class)
//                .addAsResource("META-INF/persistence.xml")
//                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//    }
//    
//    RatingController ratingController;
//    RatingView ratingView;
//    CurrentGameView currentGameView;
//    
//    @EJB
//    private RatingDAO ratingDAO;
//
//    @EJB
//    private UserAccountDAO userAccountDAO;
//
//    @EJB
//    private GameDAO gameDAO;
//    
//
//    @Inject
//    private UserTransaction tx;
//    
//    @Before
//    public void init() throws Exception {
//        currentGameView = new CurrentGameView();
//        ratingController = new RatingController();
//        ratingController.setUserAccountDAO(userAccountDAO);
//        ratingController.setGameDAO(gameDAO);
//        ratingController.setRatingDAO(ratingDAO);
//    }
//    
//    @Test
//    public void createTestAllConditionsFail() {
////        ratingController.create("doesntExist");
//        //Messages == null
////        Assert.assertFalse(test);
//    }
//}