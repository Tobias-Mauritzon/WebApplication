package com.lab3.controller;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.HighScoreDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.UserAccount;
import com.lab3.view.CurrentGameView;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
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
 * @author Joachim Antfolk
 */
@RunWith(Arquillian.class)
public class GameControllerTest {
  
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(GameDAO.class, HighScoreDAO.class, UserAccountDAO.class, CurrentGameView.class, GameController.class, UserAccount.class,
                        Rating.class, Comment.class, HighScore.class, Game.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Inject
    UserTransaction tx;  //transaction needed for testing
    
    @EJB
    GameDAO gameDAO;
    
    @EJB
    UserAccountDAO userAccountDAO;
    
    @EJB
    HighScoreDAO highScoreDAO;
    
    GameController gameController;
    
    CurrentGameView gameView;
    
    Game game;
    
    UserAccount user;
    
    @Before
    public void setup() throws Exception{
        gameView = new CurrentGameView();
        gameView.setGameDAO(gameDAO);
        gameView.setUserAccountDAO(userAccountDAO);
        gameController = new GameController();
        gameController.setGameDAO(gameDAO);
        gameController.setCurrentGameView(gameView);
        gameController.setHighScoreDAO(highScoreDAO);
        
        //starts transaction
        tx.begin();
        
        //create game entities
        game = gameDAO.createGame("matchstick", "jocke", "a game for testing", "js/matchstick_script.js", "Resources/matchstick/table.png");
        user = new UserAccount("mail@server.com", "name", "USER", "pa55w0rd");
        userAccountDAO.create(user);
        
        //Flush after create
        gameDAO.getEntityManager().flush();
        userAccountDAO.getEntityManager().flush();
    }
    
    
    
    /**
     * Test that JavaScript for an existing game can be found
     */
    @Test
    public void getJavaScriptPathSuccessTest(){
        String path = gameController.getJavaScriptPath("matchstick");
        Assert.assertEquals("js/matchstick_script.js", path);
    }
    
    @Test
    //@RunAsClient
    public void redirectTest(){
        
    }
    
    @Test
    //@RunAsClient
    public void setGameAndRedirect(){
        
    }
      
    /**
     * Test that context can be set for existing game and user
     */
    @Test
    public void setContextSuccessTest(){
        gameController.setContext("name", "matchstick");
        Assert.assertEquals(user, gameView.getUser());
        Assert.assertEquals(game, gameView.getGameObject());
    }
    
    /**
     * Test that context cannot be set for non-existing game and user
     */
    @Test
    public void setContextFailureTest(){
        gameController.setContext("a", "b");
        Assert.assertEquals(null, gameView.getUser());
        Assert.assertEquals(null, gameView.getGameObject());
    }
    
    @Test
    //@RunAsClient
    public void setHighScoreSuccessTest(){
        gameController.setContext("name", "matchstick");
        //gameController.setHighScore();
        
        //Assert.assertFalse(highScoreDAO.findHighscoresWithUserAndGame(user, game).isEmpty());
    }
    
    @Test
    //@RunAsClient
    public void setHighScoreFailTest(){
        gameController.setContext("name", "matchstick");
        //gameController.setHighScore();
        
        //Assert.assertFalse(highScoreDAO.findHighscoresWithUserAndGame(user, game).isEmpty());
    }
    
    
    
    
    /**
     * TearDown after tests
     *
     * @throws Exception Throws if "commit" fails
     */
    @After
    public void tearDown() throws Exception {
        //Refresh before delete
        gameDAO.getEntityManager().refresh(game);
        userAccountDAO.getEntityManager().refresh(user);
        
        //Remove game
        gameDAO.remove(game);
        userAccountDAO.remove(user);

        //Ends the transaction
        tx.commit();
    }
}
