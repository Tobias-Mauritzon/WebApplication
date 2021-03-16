package com.awesomeGames.model.dao;

import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
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
 * Tests for GameDAO
 *
 * @author Matteus
 * @author Simon
 */
@RunWith(Arquillian.class)
public class GameDAOTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(GameDAO.class, UserAccount.class,
                        Rating.class, Comment.class, HighScore.class, Game.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private GameDAO gameDAO;

    @Inject
    private UserTransaction tx;

    Game game1;
    Game game2;

    /**
     * Init for tests
     *
     * @throws Exception if UserTransaction logging fails
     */
    @Before
    public void init() throws Exception {
        //starts transaction
        tx.begin();

        //create entities
        game1 = gameDAO.createGame("game1", "author1", "description1", "javaScriptPath1", "imagePath1");

        // Flush inbetween so that they dont get the same timestamp
        gameDAO.getEntityManager().flush();

        game2 = gameDAO.createGame("game2", "author2", "description2", "javaScriptPath2", "imagePath2");

        //flush after create
        gameDAO.getEntityManager().flush();
    }

    /**
     * Testing the createGame Method in init
     */
    @Test
    public void create_game() {
        Assert.assertTrue(gameDAO.findAll().size() == 2);
    }

    /**
     * Test for the method findGameMathingName.
     */
    @Test
    public void findGameMathingName() {

        Assert.assertEquals(game1, gameDAO.findGameMatchingName("game1"));
        Assert.assertEquals(game2, gameDAO.findGameMatchingName("game2"));
    }

    /**
     * Test for the method findGameMathingName when no game.
     */
    @Test
    public void findGameMathingNameNoGame() {
        Game game = gameDAO.findGameMatchingName("game3");
        Assert.assertTrue(game == null);
    }

    /**
     * Test for the method findJavaScriptPathByName.
     */
    @Test
    public void findJavaScriptPathByName() {

        Assert.assertEquals(game1.getJavaScript(), gameDAO.findJavaScriptPathByName("game1"));
        Assert.assertEquals(game2.getJavaScript(), gameDAO.findJavaScriptPathByName("game2"));
    }

    /**
     * Test for the method findNewestGame.
     */
    @Test
    public void findNewestGame() {
        Assert.assertEquals(game2, gameDAO.findNewestGame());
        Assert.assertFalse(game1.equals(gameDAO.findNewestGame()));
    }

    /**
     * Test for the method findAllGames.
     */
    @Test
    public void findAllGames() {
        List<Game> list = gameDAO.findAllGames();
        Assert.assertTrue(list.size() == 2);
    }

    /**
     * TearDown for tests
     *
     * @throws Exception Throws if UserTransaction "commit" fails
     */
    @After
    public void tearDown() throws Exception {
        //refresh before delete
        gameDAO.getEntityManager().refresh(game1);
        gameDAO.getEntityManager().refresh(game2);

        gameDAO.remove(game1);
        gameDAO.remove(game2);

        //ends the transaction
        tx.commit();
    }
}
