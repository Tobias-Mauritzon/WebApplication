package com.awesomeGames.view;

import com.awesomeGames.view.CurrentGameView;
import com.awesomeGames.view.FeaturedGamesView;
import com.awesomeGames.model.dao.CommentDAO;
import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.HighScoreDAO;
import com.awesomeGames.model.dao.RatingDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
import com.awesomeGames.resource.ContextMocker;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.NotSupportedException;
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
 * Tests for FeaturedGamesView
 *
 * @author Matteus, David
 */
@RunWith(Arquillian.class)
public class FeaturedGamesViewTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(GameDAO.class, RatingDAO.class, CommentDAO.class, UserAccountDAO.class, HighScoreDAO.class,
                        UserAccount.class, Rating.class, Comment.class, HighScore.class, Game.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    FeaturedGamesView featuredGamesView;
    CurrentGameView currentGameView;
    private FacesContext facesContext;

    @EJB
    private RatingDAO ratingDAO;
    @EJB
    private UserAccountDAO userAccountDAO;
    @EJB
    private GameDAO gameDAO;
    @EJB
    private CommentDAO commentDAO;
    @EJB
    private HighScoreDAO highScoreDAO;

    @Inject
    private UserTransaction tx;

    private UserAccount user1;
    private Game game1;
    private Rating rating1;
    private HighScore highScore1;
    private Comment comment1;
    

    /**
     * Init for tests
     *
     * @throws Exception if UserTransaction logging fails
     */
    @Before
    public void setUp() throws Exception {
        featuredGamesView = new FeaturedGamesView();
        facesContext = ContextMocker.mockServletRequest();
        featuredGamesView.setFacesContext(facesContext);
        featuredGamesView.setGameDAO(gameDAO);
        featuredGamesView.setCommentDAO(commentDAO);
        featuredGamesView.setRatingDAO(ratingDAO);
        

        currentGameView = new CurrentGameView();

        tx.begin();
        user1 = new UserAccount("mail1", "name1", "USER", "password1");
        game1 = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");
        rating1 = new Rating(game1, user1, 4);
        highScore1 = new HighScore(game1, user1, 100);
        comment1 = commentDAO.createComment(game1, user1, "commentText1");

        userAccountDAO.create(user1);
        ratingDAO.create(rating1);
        highScoreDAO.create(highScore1);

        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        ratingDAO.getEntityManager().flush();
        commentDAO.getEntityManager().flush();
        highScoreDAO.getEntityManager().flush();

        currentGameView.setGame("game1");
    }

    /**
     * Test for the method init in featuredGamesView.
     */
    @Test
    public void testInit() {
        featuredGamesView.testInit();

        Assert.assertEquals(game1, featuredGamesView.getHighestRatedGame());
        Assert.assertEquals(game1, featuredGamesView.getMostCommentedGame());
        Assert.assertEquals(game1, featuredGamesView.getNewestGame());
    }
    
    @Test
    public void testInitWithoutDatabaseRepresentation() throws Exception{
        tearDown();
        facesContext = ContextMocker.mockServletRequest();
        featuredGamesView.setFacesContext(facesContext);
        featuredGamesView.testInit();

        Assert.assertNull(featuredGamesView.getHighestRatedGame());
        Assert.assertNull(featuredGamesView.getMostCommentedGame());
        Assert.assertNull(featuredGamesView.getNewestGame());
        setUp();
    }

    /**
     * Tests for getting DAOs.
     */
    @Test
    public void getGameDAO() {

        Assert.assertEquals(gameDAO, featuredGamesView.getGameDAO());
        Assert.assertEquals(commentDAO, featuredGamesView.getCommentDAO());
        Assert.assertEquals(ratingDAO, featuredGamesView.getRatingDAO());
    }
    
    @Test
    public void getSetFacesContext() {
        featuredGamesView.setFacesContext(facesContext);
        Assert.assertEquals(facesContext,featuredGamesView.getFacesContext());
    }

    /**
     * TearDown for tests
     *
     * @throws Exception Throws if UserTransaction "commit" fails
     */
    @After
    public void tearDown() throws Exception {

        ratingDAO.getEntityManager().refresh(rating1);
        gameDAO.getEntityManager().refresh(game1);
        userAccountDAO.getEntityManager().refresh(user1);
        commentDAO.getEntityManager().refresh(comment1);
        highScoreDAO.getEntityManager().refresh(highScore1);

        ratingDAO.remove(rating1);
        gameDAO.remove(game1);
        userAccountDAO.remove(user1);
        commentDAO.remove(comment1);
        highScoreDAO.remove(highScore1);
        facesContext.release();

        tx.commit();
    }
}
