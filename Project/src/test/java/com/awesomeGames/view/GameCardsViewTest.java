package com.awesomeGames.view;

import com.awesomeGames.model.dao.GameDAO;
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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for GameCardsView
 *
 * @author Matteus
 */
@RunWith(Arquillian.class)
public class GameCardsViewTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(GameCardsView.class, CurrentGameView.class, GameDAO.class, UserAccountDAO.class, UserAccount.class,
                        Rating.class, Comment.class, HighScore.class, Game.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private GameDAO gameDAO;

    GameCardsView gameCardsView;
    CurrentGameView currentGameView;

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

        gameCardsView = new GameCardsView();
        gameCardsView.setGameDAO(gameDAO);

        currentGameView = new CurrentGameView();

        //starts transaction
        tx.begin();

        //create entities
        game1 = gameDAO.createGame("game1", "author1", "description1", "javaScriptPath1", "imagePath1");

        // Flush inbetween so that they dont get the same timestamp
        gameDAO.getEntityManager().flush();

        game2 = gameDAO.createGame("game2", "author2", "description2", "javaScriptPath2", "imagePath2");

        //flush after create
        gameDAO.getEntityManager().flush();

        currentGameView.setGame("game1");
    }

    /**
     * Test for the method findGames.
     */
    @Test
    public void findGames() {

        gameCardsView.findGames();

        List<Game> expectedList = new ArrayList<>();
        expectedList.add(game1);
        expectedList.add(game2);

        Assert.assertArrayEquals(expectedList.toArray(), gameCardsView.getGameList().toArray());
    }

    /**
     * Test for the method getGameDAO.
     */
    @Test
    public void getGameDAO() {

        Assert.assertEquals(gameDAO, gameCardsView.getGameDAO());
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
