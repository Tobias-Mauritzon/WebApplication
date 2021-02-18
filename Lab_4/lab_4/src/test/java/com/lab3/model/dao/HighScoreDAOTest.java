/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.UserAccount;
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
 * Test class for the HighScore DAO
 * @author Matteus
 */
@RunWith(Arquillian.class)
public class HighScoreDAOTest {
    @Deployment
    public static WebArchive createDeployment() {
            return ShrinkWrap.create(WebArchive.class)
                    .addClasses(HighScoreDAO.class, HighScore.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class, 
                            Rating.class, Comment.class)
                    .addAsResource("META-INF/persistence.xml")
                    .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private GameDAO gameDAO;

    @EJB
    private UserAccountDAO userAccountDAO;

    @EJB
    private HighScoreDAO highScoreDAO;

    @Inject
    private UserTransaction tx;  
    
    private UserAccount user1;
    private Game game1;
    private HighScore highScore1;
    private HighScore highScore2;
    

    @Before
    public void init() throws Exception{
        //starts transaction
        tx.begin();
        
        user1 = new UserAccount("mail1", "name1", "password1");
        game1 = new Game("Game1");
        highScore1 = new HighScore(game1, user1, 100);
        highScore2 = new HighScore(game1, user1, 150);

        userAccountDAO.create(user1);
        gameDAO.create(game1);
        highScoreDAO.create(highScore1); 
        highScoreDAO.create(highScore2); 

        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        highScoreDAO.getEntityManager().flush();
    }

    @Test
    public void createHighscore() throws Exception {

        UserAccount user2 = new UserAccount("mail2", "name2", "password2");
        Game game2 = new Game("GameH");
        HighScore highScore3 = new HighScore(game2, user2, 200);
        HighScore highScore4 = new HighScore(game2, user2, 300);

        userAccountDAO.create(user2);
        gameDAO.create(game2);
        highScoreDAO.create(highScore3); 
        highScoreDAO.create(highScore4); 

        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        highScoreDAO.getEntityManager().flush();
        
        Assert.assertTrue(highScoreDAO.findAll().size() == 4);
        
        highScoreDAO.getEntityManager().refresh(highScore3);
        highScoreDAO.getEntityManager().refresh(highScore4);
        gameDAO.getEntityManager().refresh(game2);
        userAccountDAO.getEntityManager().refresh(user2);
        
        highScoreDAO.remove(highScore3);
        highScoreDAO.remove(highScore4);
        gameDAO.remove(game2);
        userAccountDAO.remove(user2);
	}
    
    @Test
    public void findHighscoreNumbersWithUserAndGame() throws Exception {

        List list = highScoreDAO.findHighscoreNumbersWithUserAndGame(user1, game1);
        Assert.assertTrue(list.size() == 2);
        Assert.assertTrue(list.get(0).equals(highScore2.getHighScore()));
        Assert.assertTrue(list.get(1).equals(highScore1.getHighScore()));
	}
    
    @Test
    public void findHighscoresWithUserAndGame() throws Exception {

        List list = highScoreDAO.findHighscoresWithUserAndGame(user1, game1);
        Assert.assertTrue(list.size() == 2);
        Assert.assertTrue(list.get(0).equals(highScore2));
        Assert.assertTrue(list.get(1).equals(highScore1));
	}
    
    @Test
    public void findHighscoresWithUser() throws Exception {

        List list = highScoreDAO.findHighscoresWithUser(user1);
        Assert.assertTrue(list.size() == 2);
        Assert.assertTrue(list.get(0).equals(highScore2));
        Assert.assertTrue(list.get(1).equals(highScore1));
	}
    
        @Test
    public void findHighscoresWithGame() throws Exception {

        List list = highScoreDAO.findHighscoresWithGame(game1);
        Assert.assertTrue(list.size() == 2);
        Assert.assertTrue(list.get(0).equals(highScore2));
        Assert.assertTrue(list.get(1).equals(highScore1));
	}
    
    @After
    public void tearDown() throws Exception {
        highScoreDAO.getEntityManager().refresh(highScore1);
        highScoreDAO.getEntityManager().refresh(highScore2);
        gameDAO.getEntityManager().refresh(game1);
        userAccountDAO.getEntityManager().refresh(user1);
        
        highScoreDAO.remove(highScore1);
        highScoreDAO.remove(highScore2);
        gameDAO.remove(game1);
        userAccountDAO.remove(user1);
        
        //end transaction
        tx.commit();
    }
    
}
