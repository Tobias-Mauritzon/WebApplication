/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Users;
import com.lab3.model.entity.key.HighScorePK;
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

@RunWith(Arquillian.class)
public class HighScoreDAOTest {
    @Deployment
    public static WebArchive createDeployment() {
            return ShrinkWrap.create(WebArchive.class)
                    .addClasses(HighScoreDAO.class, HighScore.class, UsersDAO.class, Users.class, GameDAO.class, Game.class)
                    .addAsResource("META-INF/persistence.xml")
                    .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private GameDAO gameDAO;

    @EJB
    private UsersDAO usersDAO;

    @EJB
    private HighScoreDAO highScoreDAO;

    @Inject
    private UserTransaction tx;  
    
    private Users user1;
    private Game game1;
    private HighScore highScore1;
    private HighScore highScore2;
    

    @Before
    public void init() throws Exception{
        //starts transaction
        tx.begin();
        
        user1 = new Users("mail1", "name1", "password1");
        game1 = new Game("Game1");
        highScore1 = new HighScore(game1, user1, 100);
        highScore2 = new HighScore(game1, user1, 150);

        usersDAO.create(user1);
        gameDAO.create(game1);
        highScoreDAO.create(highScore1); 
        highScoreDAO.create(highScore2); 

        usersDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        highScoreDAO.getEntityManager().flush();
    }

    @Test
    public void createHighscore() throws Exception {

        Users user2 = new Users("mail2", "name2", "password2");
        Game game2 = new Game("GameH");
        HighScore highScore3 = new HighScore(game2, user2, 200);
        HighScore highScore4 = new HighScore(game2, user2, 300);

        usersDAO.create(user2);
        gameDAO.create(game2);
        highScoreDAO.create(highScore3); 
        highScoreDAO.create(highScore4); 

        usersDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        highScoreDAO.getEntityManager().flush();
        
        Assert.assertTrue(highScoreDAO.findAll().size() == 4);
        
        highScoreDAO.getEntityManager().refresh(highScore3);
        highScoreDAO.getEntityManager().refresh(highScore4);
        gameDAO.getEntityManager().refresh(game2);
        usersDAO.getEntityManager().refresh(user2);
        
        highScoreDAO.remove(highScore3);
        highScoreDAO.remove(highScore4);
        gameDAO.remove(game2);
        usersDAO.remove(user2);
	}
    
    @Test
    public void findHighscoreNumbersWithUsermailAndGamename() throws Exception {

        List list = highScoreDAO.findHighscoreNumbersWithUsermailAndGamename(user1.getMail(), game1.getName());
        Assert.assertTrue(list.size() == 2);
        Assert.assertTrue(list.get(0).equals(highScore2.getHighScore()));
        Assert.assertTrue(list.get(1).equals(highScore1.getHighScore()));
	}
    
    @Test
    public void findHighscoresWithUsermail() throws Exception {

        List list = highScoreDAO.findHighscoresWithUsermail(user1.getMail());
        Assert.assertTrue(list.size() == 2);
        Assert.assertTrue(list.get(0).equals(highScore2));
        Assert.assertTrue(list.get(1).equals(highScore1));
	}
    
        @Test
    public void findHighscoresWithGamename() throws Exception {

        List list = highScoreDAO.findHighscoresWithGamename(game1.getName());
        Assert.assertTrue(list.size() == 2);
        Assert.assertTrue(list.get(0).equals(highScore2));
        Assert.assertTrue(list.get(1).equals(highScore1));
	}
    
    @After
    public void tearDown() throws Exception {
        highScoreDAO.getEntityManager().refresh(highScore1);
        highScoreDAO.getEntityManager().refresh(highScore2);
        gameDAO.getEntityManager().refresh(game1);
        usersDAO.getEntityManager().refresh(user1);
        
        highScoreDAO.remove(highScore1);
        highScoreDAO.remove(highScore2);
        gameDAO.remove(game1);
        usersDAO.remove(user1);
        
        //end transaction
        tx.commit();
    }
    
}
