package com.awesomeGames.controller;

import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.HighScoreDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
import com.awesomeGames.resource.ContextMocker;
import com.awesomeGames.view.UserScoresView;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;

/**
 * Test class for UserScoresController
 * @author Joachim Antfolk
 */
@RunWith(Arquillian.class)
public class UserScoresControllerTest {
    
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(HighScoreDAO.class, HighScore.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class,
                        Rating.class, Comment.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Inject
    UserTransaction tx;  //transaction needed for testing
    
    @EJB
    private HighScoreDAO highScoreDAO;
    
    @EJB
    private UserAccountDAO userAccountDAO;
    
    @EJB
    private GameDAO gameDAO;
    
    private String userName;
    
    private UserAccount user;
    
    private Game game;
    
    private HighScore highScore1;
    
    private HighScore highScore2;
    
    private UserScoresView userScoresView;
    
    private UserScoresController userScoresController;
    
    private FacesContext facesContext;         
    
    /**
     * Init before tests
     *
     * @throws Exception
     */
    @Before
    public void setup() throws Exception { 
        userName = "user";
        
        facesContext = ContextMocker.mockServletRequest();
        when(facesContext.getExternalContext().getUserPrincipal().getName()).thenReturn(userName);
        
        userScoresView = new UserScoresView();
        userScoresController = new UserScoresController();
        
        userScoresController.setFacesContext(facesContext);
        userScoresController.setHighScoreDAO(highScoreDAO);
        userScoresController.setUserAccountDAO(userAccountDAO);
        userScoresController.setUserScoresView(userScoresView);
        
        tx.begin();
        user = new UserAccount("user@mail.com", userName, "USER", "password1");       
        game = gameDAO.createGame("Game", "Author1", "Game Description", "gamepath.js", "imagepath.png");
        highScore1 = new HighScore(game, user, 100);
        highScore2 = new HighScore(game, user, 150);

        userAccountDAO.create(user);
        highScoreDAO.create(highScore1);
        highScoreDAO.create(highScore2);

        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        highScoreDAO.getEntityManager().flush();
    }
    
    /**
     * Test for init of UserScoresController
     */
    @Test
    public void userScoresControllerInitTest(){
        Assert.assertEquals(facesContext, userScoresController.getFacesContext());
        Assert.assertEquals(highScoreDAO, userScoresController.getHighScoreDAO());
        Assert.assertEquals(userAccountDAO, userScoresController.getUserAccountDAO());
        Assert.assertEquals(userScoresView, userScoresController.getUserScoresView());
    }

    /**
     * Test for deleting all scores, expected success
     */
    @Test
    public void userScoresControllerDeleteSuccessTest(){
        Assert.assertFalse(highScoreDAO.findHighscoresWithUser(user).isEmpty());
        userScoresController.deleteAllUserScores();
        Assert.assertTrue(highScoreDAO.findHighscoresWithUser(user).isEmpty());
        Assert.assertEquals("Scores Deleted!", facesContext.getMessageList().get(0).getSummary());
        
        /*Restore Data*/
        highScore1 = new HighScore(game, user, 100);
        highScore2 = new HighScore(game, user, 150);
        highScoreDAO.create(highScore1);
        highScoreDAO.create(highScore2);
        highScoreDAO.getEntityManager().flush();
    }
    
    /**
     * Test for deleting all scores, expected failure
     */
    @Test
    public void userScoresControllerDeleteFailureTest(){
        Assert.assertFalse(highScoreDAO.findHighscoresWithUser(user).isEmpty());
        
        highScoreDAO.getEntityManager().refresh(highScore1);
        highScoreDAO.getEntityManager().refresh(highScore2);
        highScoreDAO.remove(highScore1);
        highScoreDAO.remove(highScore2);
        
        Assert.assertTrue(highScoreDAO.findHighscoresWithUser(user).isEmpty());
        
        userScoresController.deleteAllUserScores();
        Assert.assertEquals("Can't delete scores!", facesContext.getMessageList().get(0).getSummary());

        /*Restore Data*/
        highScore1 = new HighScore(game, user, 100);
        highScore2 = new HighScore(game, user, 150);
        highScoreDAO.create(highScore1);
        highScoreDAO.create(highScore2);
        highScoreDAO.getEntityManager().flush();
    }
    
    /**
     * Test for finding user scores, expected success
     */
    @Test
    public void findScoresSuccessTest(){
        Assert.assertFalse(highScoreDAO.findHighscoresWithUser(user).isEmpty());   
        
        userScoresController.findScores();
        List<HighScore> list = userScoresView.getScoreList();
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.contains(highScore1));
        Assert.assertTrue(list.contains(highScore2));
        

    }
    
    /**
     * Test for finding user scores, expected failure
     */
    @Test
    public void findScoresFailureTest(){
        Assert.assertFalse(highScoreDAO.findHighscoresWithUser(user).isEmpty());
        
        highScoreDAO.getEntityManager().refresh(highScore1);
        highScoreDAO.getEntityManager().refresh(highScore2);
        highScoreDAO.remove(highScore1);
        highScoreDAO.remove(highScore2);
        
        Assert.assertTrue(highScoreDAO.findHighscoresWithUser(user).isEmpty());
        
        userScoresController.findScores();
        Assert.assertTrue(userScoresView.getScoreList().isEmpty());

        /*Restore Data*/
        highScore1 = new HighScore(game, user, 100);
        highScore2 = new HighScore(game, user, 150);
        highScoreDAO.create(highScore1);
        highScoreDAO.create(highScore2);
        highScoreDAO.getEntityManager().flush();
    }
    
    /**
    * TearDown after tests
    *
    * @throws Exception
    */
    @After
    public void tearDown() throws Exception {
        highScoreDAO.getEntityManager().refresh(highScore1);
        highScoreDAO.getEntityManager().refresh(highScore2);
        gameDAO.getEntityManager().refresh(game);
        userAccountDAO.getEntityManager().refresh(user);

        highScoreDAO.remove(highScore1);
        highScoreDAO.remove(highScore2);
        gameDAO.remove(game);
        userAccountDAO.remove(user);
        tx.commit();

        facesContext.release();
    }
}
