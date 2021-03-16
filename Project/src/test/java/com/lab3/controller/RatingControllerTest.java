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
import com.lab3.resource.ContextMocker;
import com.lab3.view.CurrentGameView;
import com.lab3.view.RatingView;
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 *
 * @author David
 */
@RunWith(Arquillian.class)
public class RatingControllerTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(RatingDAO.class, Rating.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class,
                        Comment.class, HighScore.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private RatingController ratingController;
    private RatingView ratingView;
    private CurrentGameView currentGameView;
    private FacesContext facesContext;
    private UserTransaction utx;
    
    @EJB
    private RatingDAO ratingDAO;

    @EJB
    private UserAccountDAO userAccountDAO;

    @EJB
    private GameDAO gameDAO;
    

    @Inject
    private UserTransaction tx;
    
    @Before
    public void init() throws Exception {
        currentGameView = new CurrentGameView();
        ratingView = new RatingView();
        ratingController = new RatingController();
        ratingController.setUserAccountDAO(userAccountDAO);
        ratingController.setGameDAO(gameDAO);
        ratingController.setRatingDAO(ratingDAO);
        ratingController.setRatingView(ratingView);
        facesContext = ContextMocker.mockServletRequest();
        ratingController.setFacesContext(facesContext);
        utx = Mockito.mock(UserTransaction.class);
        ratingController.setUtx(utx);
        
    }
    
    @After 
    public void release() {
        facesContext.release();
    }
    
    @Test
    public void createTestAllConditionsFailTest() {
        boolean shouldBeFalse = ratingController.create("doesntExist");
        Assert.assertFalse(shouldBeFalse);
    }
    
        @Test
    public void removeRatingAllConditionsFailTest() throws Exception {
        boolean shouldBeFalse = ratingController.removeRating("name");
        Assert.assertFalse(shouldBeFalse);
    }
    
    @Test
    public void createAllConditionsSuceedTest() throws Exception {
        //starts transaction
        tx.begin();

        //create entities
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        Game game1 = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");
//        Rating rating = new Rating(game1, user1, 4);

        userAccountDAO.create(user1);
        ratingView.setRating(3);
        ratingView.setGame(game1.getName());

        //flush after create
        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        
        boolean shouldBeTrue = ratingController.create(user1.getName());
        Assert.assertTrue(shouldBeTrue);
        
        shouldBeTrue = ratingController.removeRating(user1.getName());
        Assert.assertTrue(shouldBeTrue);
        
        gameDAO.getEntityManager().refresh(game1);
        userAccountDAO.getEntityManager().refresh(user1);

        gameDAO.remove(game1);
        userAccountDAO.remove(user1);

        tx.commit();
    }

    
    @Test
    public void createUpdateTest() throws Exception {
        //starts transaction
        tx.begin();

        //create entities
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        Game game1 = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");
//        Rating rating = new Rating(game1, user1, 4);

        userAccountDAO.create(user1);
        ratingView.setRating(3);
        ratingView.setGame(game1.getName());

        //flush after create
        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();

        boolean shouldBeTrue = ratingController.create(user1.getName());
        Assert.assertTrue(shouldBeTrue);
        
        ratingView.setRating(4);
        
        shouldBeTrue = ratingController.create(user1.getName());
        Assert.assertTrue(shouldBeTrue);

        shouldBeTrue = ratingController.removeRating(user1.getName());
        Assert.assertTrue(shouldBeTrue);

        gameDAO.getEntityManager().refresh(game1);
        userAccountDAO.getEntityManager().refresh(user1);

        gameDAO.remove(game1);
        userAccountDAO.remove(user1);

        tx.commit();
    }
    
    @Test
    public void removeRatingRatingDoesntExistTest() throws Exception {

        //starts transaction
        tx.begin();

        //create entities
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        Game game1 = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");
//        Rating rating = new Rating(game1, user1, 4);

        userAccountDAO.create(user1);

        //flush after create
        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();

        boolean shouldBeFalse = ratingController.removeRating(user1.getName());
        Assert.assertFalse(shouldBeFalse);

        gameDAO.getEntityManager().refresh(game1);
        userAccountDAO.getEntityManager().refresh(user1);

        gameDAO.remove(game1);
        userAccountDAO.remove(user1);

        tx.commit();
    }
    
    @Test
    public void removeRatingUtxFailsTest() throws Exception {

        //starts transaction
        tx.begin();
        //create entities
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        Game game1 = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");

        userAccountDAO.create(user1);

        //flush after create
        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        
        ratingView.setRating(4);
        ratingView.setGame(game1.getName());
        
        boolean shouldBeTrue = ratingController.create(user1.getName());
//        Assert.assertTrue(shouldBeTrue);

        ratingController.setUtx(null);
        
        boolean shouldBeFalse = ratingController.removeRating(user1.getName());
        Assert.assertFalse(shouldBeFalse);
        
        ratingController.setUtx(utx);

        shouldBeTrue = ratingController.removeRating(user1.getName());
        Assert.assertTrue(shouldBeTrue);
        
        shouldBeTrue = ratingController.removeRating(user1.getName());
        Assert.assertTrue(shouldBeTrue);
        
        gameDAO.getEntityManager().refresh(game1);
        userAccountDAO.getEntityManager().refresh(user1);

        gameDAO.remove(game1);
        userAccountDAO.remove(user1);

        tx.commit();
    }
    
    @Test
    public void getSetGameDAOTest() {
        ratingController.setGameDAO(gameDAO);
        Assert.assertEquals(gameDAO,ratingController.getGameDAO());
    }
    
    @Test
    public void getSetUserAccountDAOTest() {
        ratingController.setUserAccountDAO(userAccountDAO);
        Assert.assertEquals(userAccountDAO,ratingController.getUserAccountDAO());
    }
    
    @Test
    public void getSetRatingDAOTest() {
        ratingController.setRatingDAO(ratingDAO);
        Assert.assertEquals(ratingDAO,ratingController.getRatingDAO());
    }
    
    @Test
    public void getSetRatingViewTest() {
        ratingController.setRatingView(ratingView);
        Assert.assertEquals(ratingView,ratingController.getRatingView());
    }
    
    @Test
    public void getSetFacesContextTest() {
        ratingController.setFacesContext(facesContext);
        Assert.assertEquals(facesContext,ratingController.getFacesContext());
    }
    
    @Test
    public void getAverageRatingTest() throws Exception {
        tx.begin();

        //create entities
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        UserAccount user2 = new UserAccount("mail2", "name2", "USER", "password2");
        Game game1 = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");
//        Rating rating = new Rating(game1, user1, 4);

        userAccountDAO.create(user1);
        userAccountDAO.create(user2);
        ratingView.setRating(2);
        ratingView.setGame(game1.getName());

        //flush after create
        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        
        boolean shouldBeTrue = ratingController.create(user1.getName());
        Assert.assertTrue(shouldBeTrue);
        
        ratingView.setRating(5);
        
        shouldBeTrue = ratingController.create(user2.getName());
        Assert.assertTrue(shouldBeTrue);
        
        Assert.assertEquals(4,ratingController.getAverageRating());
        
        shouldBeTrue = ratingController.removeRating(user1.getName());
        Assert.assertTrue(shouldBeTrue);
        
        shouldBeTrue = ratingController.removeRating(user2.getName());
        Assert.assertTrue(shouldBeTrue);
        
        gameDAO.getEntityManager().refresh(game1);
        userAccountDAO.getEntityManager().refresh(user1);
        userAccountDAO.getEntityManager().refresh(user2);

        gameDAO.remove(game1);
        userAccountDAO.remove(user1);
        userAccountDAO.remove(user2);

        tx.commit();
    }
    
    @Test
    public void getSetUtxTest() {
        ratingController.setUtx(utx);
        Assert.assertEquals(utx,ratingController.getUtx());
    } 
}
