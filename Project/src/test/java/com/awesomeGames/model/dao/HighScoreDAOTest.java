package com.awesomeGames.model.dao;

import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.HighScoreDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
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
 * Test class for the HighScore DAO
 *
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
        user1 = new UserAccount("mail1", "name1", "USER", "password1");
        game1 = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");
        highScore1 = new HighScore(game1, user1, 100);
        highScore2 = new HighScore(game1, user1, 150);

        userAccountDAO.create(user1);
        highScoreDAO.create(highScore1);
        highScoreDAO.create(highScore2);

        //flush after create
        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        highScoreDAO.getEntityManager().flush();
    }

    @Test
    public void createHighscore() throws Exception {

        UserAccount user2 = new UserAccount("mail2", "name2", "USER", "password2");
        Game game2 = gameDAO.createGame("GameH", "author", "description", "javaScriptPath", "imagePath");
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

    /**
     * Test for the method findHighscoreNumbersWithUserAndGame.
     */
    @Test
    public void findHighscoreNumbersWithUserAndGame() {

        List list = highScoreDAO.findHighscoreNumbersWithUserAndGame(user1, game1);
        Assert.assertTrue(list.size() == 2);
        Assert.assertTrue(list.get(0).equals(highScore2.getHighScore()));
        Assert.assertTrue(list.get(1).equals(highScore1.getHighScore()));
    }

    /**
     * Test for the method findHighscoresWithUserAndGame.
     */
    @Test
    public void findHighscoresWithUserAndGame() {

        List list = highScoreDAO.findHighscoresWithUserAndGame(user1, game1);
        Assert.assertTrue(list.size() == 2);
        Assert.assertTrue(list.get(0).equals(highScore2));
        Assert.assertTrue(list.get(1).equals(highScore1));
    }

    /**
     * Test for the method findHighscoresWithUser.
     */
    @Test
    public void findHighscoresWithUser() {

        List list = highScoreDAO.findHighscoresWithUser(user1);
        Assert.assertTrue(list.size() == 2);
        Assert.assertTrue(list.get(0).equals(highScore2));
        Assert.assertTrue(list.get(1).equals(highScore1));
    }

    /**
     * Test for the method findHighscoresWithGame.
     */
    @Test
    public void findHighscoresWithGame() {

        List list = highScoreDAO.findHighscoresWithGame(game1);
        Assert.assertTrue(list.size() == 2);
        Assert.assertTrue(list.get(0).equals(highScore2));
        Assert.assertTrue(list.get(1).equals(highScore1));
    }

    /**
     * TearDown for tests
     *
     * @throws Exception Throws if UserTransaction "commit" fails
     */
    @After
    public void tearDown() throws Exception {
        //refresh before delete
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
