/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.view;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.HighScoreDAO;
import com.lab3.model.dao.RatingDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.UserAccount;
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Wille
 */
@RunWith(Arquillian.class)
public class HighScoreViewTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(RatingDAO.class, Rating.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class,
                        Comment.class, HighScore.class, HighScoreDAO.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private HighScoreView highScoreView;
    private CurrentGameView currentGameView;
    
    @EJB
    private HighScoreDAO highScoreDAO;

    @EJB
    private UserAccountDAO userAccountDAO;

    @EJB
    private GameDAO gameDAO;

    @Inject
    private UserTransaction tx;
    
    @Before
    public void init() throws Exception {
        highScoreView = new HighScoreView();
        highScoreView.setHighScoreDAO(highScoreDAO);
        highScoreView.setUserDAO(userAccountDAO);
        highScoreView.setGameDAO(gameDAO);
        currentGameView = new CurrentGameView();
        highScoreView.setCurrentGameView(currentGameView);
    }
    
    @Test
    public void setGetGameTest() {
        highScoreView.setGame("game");
        //make sure the value is string
        Assert.assertEquals("game",highScoreView.getGame());
    }
    
    @Test
    public void setGetHighScores1Test() {
        List<HighScore> highScores1 = new ArrayList<>();
        highScoreView.setHighScores1(highScores1);
        Assert.assertEquals(highScores1, highScoreView.getHighScores1());
    }
    
    @Test
    public void setGetHighScores2Test() {
        List<HighScore> highScores2 = new ArrayList<>();
        highScoreView.setHighScores2(highScores2);
        Assert.assertEquals(highScores2, highScoreView.getHighScores2());
    }
    
    @Test
    public void getDAOs() throws Exception {
        Assert.assertEquals(highScoreDAO, highScoreView.getHighScoreDAO());
        Assert.assertEquals(userAccountDAO, highScoreView.getUserDAO());
        Assert.assertEquals(gameDAO, highScoreView.getGameDAO());
    }
    
    @Test
    public void getViews() throws Exception {
        Assert.assertEquals(currentGameView, highScoreView.getCurrentGameView());
    }
    
    /*
    Not implemented yet
    @Test
    public void updateHighscoreListForGameWithNameTest() throws Exception{
        tx.begin();
        
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        UserAccount user2 = new UserAccount("mail2", "name2", "USER", "password1");
        userAccountDAO.create(user1);
        userAccountDAO.create(user2);
        Game game1 = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");
        
        HighScore highScoreA = new HighScore(game1, user1, 100);
//        HighScore highScoreB = new HighScore(game1, user2, 50);
        
        highScoreView.newHighScore(highScoreA.getGame().getName(), highScoreA.getUserAccount().getName(), highScoreA.getHighScore());
        //highScoreView.newHighScore(highScoreB.getGame().getName(), highScoreB.getUserAccount().getName(), highScoreB.getHighScore());
        
        
        
        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        highScoreDAO.getEntityManager().flush();
        
        
        
//        System.out.println(highScoreView.getHighScores1());
        //Assert.assertEquals(highScoreA, highScoreView.getHighScores1());

        Assert.assertTrue(true);
        
        highScoreDAO.getEntityManager().refresh(highScoreA);
        highScoreDAO.getEntityManager().refresh(highScoreB);
        gameDAO.getEntityManager().refresh(game1);
        userAccountDAO.getEntityManager().refresh(user1);
        userAccountDAO.getEntityManager().refresh(user2);

        highScoreDAO.remove(highScoreA);
        highScoreDAO.remove(highScoreB);
        gameDAO.remove(game1);
        userAccountDAO.remove(user1);
        userAccountDAO.remove(user2);
        
        
        
        
        tx.commit();
    }
    */
}
