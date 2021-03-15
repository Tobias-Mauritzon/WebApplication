package com.lab3.controller;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.HighScoreDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.UserAccount;
import com.lab3.resource.ContextMocker;
import com.lab3.view.CurrentGameView;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Assertions;

/**
 * Tests for the GameController class
 * @author Joachim Antfolk
 */
@RunWith(Arquillian.class)
public class GameControllerTest {
  
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(GameDAO.class, HighScoreDAO.class, UserAccountDAO.class, UserAccount.class,
                        Rating.class, Comment.class, HighScore.class, Game.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    UserTransaction tx;  //transaction needed for testing
    
    @EJB
    private GameDAO gameDAO;
    
    @EJB
    private UserAccountDAO userAccountDAO;
    
    @EJB
    private HighScoreDAO highScoreDAO;
    
    private GameController gameController;
    
    private CurrentGameView gameView;
    
    private Game game;
    
    private UserAccount user;
    
    private FacesContext facesContext;
    
    /**
     * Init before tests
     * @throws Exception 
     */
    @Before
    public void setup() throws Exception{
        gameView = new CurrentGameView();
        gameView.setGameDAO(gameDAO);
        gameView.setUserAccountDAO(userAccountDAO);
        gameController = new GameController();
        gameController.setGameDAO(gameDAO);
        gameController.setCurrentGameView(gameView);
        gameController.setHighScoreDAO(highScoreDAO);
        facesContext = ContextMocker.mockServletRequest();
        
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
     * Tests that setup happens correctly
     */
    @Test
    public void setupTest(){
        Assert.assertEquals(gameDAO, gameController.getGameDAO());
        Assert.assertEquals(gameView, gameController.getCurrentGameView());
        Assert.assertEquals(highScoreDAO, gameController.getHighScoreDAO());
    }
    
    /**
     * Test that JavaScript for an existing game can be found
     */
    @Test
    public void getJavaScriptPathSuccessTest(){
        String path = gameController.getJavaScriptPath("matchstick");
        Assert.assertEquals("js/matchstick_script.js", path);
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
    
    /**
     * Tests that redirect happens correctly
     */
    @Test
    public void redirectTest(){
        Assertions.assertDoesNotThrow(() -> {gameController.redirect();});
    }
    
    /**
     * Tests that game is set and redirect happens correctly
     */
    @Test
    public void setGameAndRedirectTest(){
        Assertions.assertDoesNotThrow(() -> {gameController.setGameAndRedirect("matchstick");});
        Assert.assertEquals(gameView.getGame(), "matchstick");
    }
    
    @Test
    public void setHighScoreSuccessTest(){
        Assert.assertTrue(highScoreDAO.findHighscoresWithUserAndGame(user, game).isEmpty());
        gameController.setContext("name", "matchstick");
        try {
            gameController.setGameAndRedirect("matchstick");
        } catch (IOException ex) {}
        
        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().put("highscore", "10");
        
        gameController.setHighScore();
        
        List<HighScore> list = (List<HighScore>)highScoreDAO.findHighscoresWithUserAndGame(user, game);
        for(HighScore score: list)
            highScoreDAO.remove(score);
    }
    
    @Test
    public void setHighScoreFailTest(){
        Assert.assertTrue(highScoreDAO.findHighscoresWithUserAndGame(user, game).isEmpty());
        try {
            gameController.setGameAndRedirect("matchstick");
        } catch (IOException ex) {}

        Map<String, String> session = new HashMap<String, String>();
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        when(ext.getRequestParameterMap()).thenReturn(session);
        session.put("highscore", "10");
        
        gameController.setHighScore();
        
        Assert.assertTrue(highScoreDAO.findHighscoresWithUserAndGame(user, game).isEmpty());
        List<HighScore> list = (List<HighScore>)highScoreDAO.findHighscoresWithUserAndGame(user, game);
        for(HighScore score: list)
            highScoreDAO.remove(score);
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
        
        facesContext.release();
    } 
}
