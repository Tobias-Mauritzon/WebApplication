/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awesomeGames.view;

import com.awesomeGames.view.HighScoreView;
import com.awesomeGames.view.CurrentGameView;
import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.HighScoreDAO;
import com.awesomeGames.model.dao.RatingDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
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
 * Tests for HighScoreView
 * @author William
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
    public void initTest() throws Exception {
        tx.begin();
        Game gameInit = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");
        gameDAO.create(gameInit);
        currentGameView.setGame(gameInit.getName());
        highScoreView.testInit();
        Assert.assertNotEquals(null, highScoreView.getHighScores1());
        gameDAO.remove(gameInit);
        tx.commit();
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
    
    /**
     * Tests if updateHighscoreListForGameWithName updates HighScoreViews
     * high score lists correctly
     * @throws Exception 
     */
    @Test
    public void updateHighscoreListForGameWithNameTest() throws Exception{
        tx.begin();
        
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        UserAccount user2 = new UserAccount("mail2", "name2", "USER", "password1");
        userAccountDAO.create(user1);
        userAccountDAO.create(user2);
        Game game1 = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");
        Game game2 = gameDAO.createGame("Game2", "author", "description", "javaScriptPath", "imagePath");
        
        HighScore highScoreA = new HighScore(game1, user1, 100);
        HighScore highScoreB = new HighScore(game1, user2, 50);
        HighScore highScoreC = new HighScore(game2, user1, 1);
        
        
        Assert.assertEquals(null, highScoreView.getHighScores1());
        Assert.assertEquals(null, highScoreView.getHighScores2());
        
        highScoreView.updateHighscoreListForGameWithName(game1.getName());
        
        List<HighScore> emptyHighScoreList1 = new ArrayList<>();
        List<HighScore> emptyHighScoreList2 = new ArrayList<>();
        emptyHighScoreList1.addAll(highScoreView.getHighScores1());
        emptyHighScoreList2.addAll(highScoreView.getHighScores2());
        
        
        // Add 2 high scores to fill high score top 1-2 for game1
        highScoreDAO.create(highScoreA);
        highScoreDAO.create(highScoreB);
        
        // Add 1 high score to fill high score top 1 for game2
        highScoreDAO.create(highScoreC);

        Assert.assertEquals(emptyHighScoreList1, highScoreView.getHighScores1());
        Assert.assertEquals(emptyHighScoreList2, highScoreView.getHighScores2());
        
        highScoreView.updateHighscoreListForGameWithName(game1.getName());
        
        Assert.assertEquals(highScoreA, highScoreView.getHighScores1().get(0));
        Assert.assertEquals(highScoreB, highScoreView.getHighScores1().get(1));
        Assert.assertEquals(emptyHighScoreList2, highScoreView.getHighScores2());
        
        HighScore highScoreD = new HighScore(game1, user2, 50);
        HighScore highScoreE = new HighScore(game1, user2, 50);
        HighScore highScoreF = new HighScore(game1, user2, 50);
        HighScore highScoreG = new HighScore(game1, user2, 50);
        HighScore highScoreH = new HighScore(game1, user2, 50);
        
        // Add 5 more high scores to fill high score top 1-7 for game1
        highScoreDAO.create(highScoreD);
        highScoreDAO.create(highScoreE);
        highScoreDAO.create(highScoreF);
        highScoreDAO.create(highScoreG);
        highScoreDAO.create(highScoreH);
        
        highScoreView.updateHighscoreListForGameWithName(game1.getName());
        
        // Check if list 2 of game1 is no longer empty 
        Assert.assertNotEquals(emptyHighScoreList2, highScoreView.getHighScores2());
        
        highScoreView.updateHighscoreListForGameWithName(game2.getName());
       
        
        
        // Check if top 1-3 is highScoreC and rest of list one is not equal to another score (which implies it's empty)
        for(int i = 0; i < 5; i++) {
            if(i < 1) {
                Assert.assertEquals(highScoreC, highScoreView.getHighScores1().get(i));
            } else {
                Assert.assertNotEquals(highScoreA, highScoreView.getHighScores1().get(i));
                Assert.assertNotEquals(highScoreB, highScoreView.getHighScores1().get(i));
            }
        }
        
        highScoreDAO.remove(highScoreA);
        highScoreDAO.remove(highScoreB);
        highScoreDAO.remove(highScoreC);
        highScoreDAO.remove(highScoreD);
        highScoreDAO.remove(highScoreE);
        highScoreDAO.remove(highScoreF);
        highScoreDAO.remove(highScoreG);
        highScoreDAO.remove(highScoreH);
        gameDAO.remove(game1);
        gameDAO.remove(game2);
        userAccountDAO.remove(user1);
        userAccountDAO.remove(user2);
        
        tx.commit();
    }
    
}
