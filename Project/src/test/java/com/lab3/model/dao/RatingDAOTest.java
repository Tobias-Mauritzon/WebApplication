package com.lab3.model.dao;

import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.UserAccount;
import com.lab3.model.entity.key.RatingPK;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for the Rating DAO
 *
 * @author Matteus
 * @author Tobias
 */
@RunWith(Arquillian.class)
public class RatingDAOTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(RatingDAO.class, Rating.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class,
                        Comment.class, HighScore.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private RatingDAO ratingDAO;

    @EJB
    private UserAccountDAO userAccountDAO;

    @EJB
    private GameDAO gameDAO;

    @Inject
    private UserTransaction tx;

    private UserAccount user1;
    private Game game1;
    private Rating rating1;

    /**
     * Creates entites for the tests and adds them to the database before every
     * test. Opens UserTransaction.
     *
     * @throws Exception
     */
    @Before
    public void init() throws Exception {
        tx.begin();
        user1 = new UserAccount("mail1", "name1", "USER", "password1");
        game1 = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");
        rating1 = new Rating(game1, user1, 4);

        userAccountDAO.create(user1);
        gameDAO.create(game1);
        ratingDAO.create(rating1);

        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        ratingDAO.getEntityManager().flush();
    }

    /**
     * Test that run in sequence for adding and removing many objects, something
     * is wrong though.
     *
     */
    @Test
    @InSequence(1)
    public void create_rating() {
        UserAccount user1 = new UserAccount("mail14", "name2", "USER", "password1");

        userAccountDAO.create(user1);
        userAccountDAO.getEntityManager().flush();

        for (int i = 3; i < 11; i++) {
            Game game = gameDAO.createGame("Game" + i, "author", "description", "javaScriptPath", "imagePath");
            Rating r = new Rating(game, user1, i);

            gameDAO.create(game);
            ratingDAO.create(r);

        }
        gameDAO.getEntityManager().flush();
        ratingDAO.getEntityManager().flush();
        Assert.assertEquals(8, ratingDAO.findAllRatingsByUsername("name2").size());
    }

    @Test
    @InSequence(2)
    public void remove_rating() {
        UserAccount user1 = userAccountDAO.find("mail14");

        for (int i = 3; i < 11; i++) {
            Game game = gameDAO.find("Game" + i);
            Rating r = ratingDAO.find(new RatingPK("Game" + i, "mail14"));

            ratingDAO.getEntityManager().refresh(r);
            gameDAO.getEntityManager().refresh(game);

            ratingDAO.remove(r);
            gameDAO.remove(game);
        }
        userAccountDAO.getEntityManager().refresh(user1);

        userAccountDAO.remove(user1);
        Assert.assertEquals(0, ratingDAO.findAllRatingsByUsername("name2").size());

    }

    /**
     * Tests find rating by name function The name used comes from init
     *
     */
    @Test
    public void testFindAllRatingsByName() {
        Assert.assertEquals(4, ratingDAO.findAllRatingsByUsername("name1").get(0));

    }

    /**
     * Tests find rating by name function The name used comes from init
     *
     */
    @Test
    public void findAllRatingsForGame() {
        Assert.assertEquals(4, ratingDAO.findAllRatingsForGame("Game1").get(0));
    }

    /**
     * Tests find rating by mail1 and Game1 function The Game1 and mail1 used
     * comes from init
     *
     */
    @Test
    public void findRatingsByGameNameAndUserMail() {
        int val = ratingDAO.findRatingsByGameNameAndUserMail("Game1", "mail1");
        Assert.assertEquals(4, val);
    }

    /**
     * Tests find rating by mail1 and Game1 function if rating doesn't exits
     *
     */
    @Test
    public void invalidRatingsByGameNameAndUserMail() {
        Assert.assertEquals(null, ratingDAO.findRatingsByGameNameAndUserMail("Game8", "mail16"));
    }

    /**
     * Tests the methods findsHighestAvgRatedGame
     */
    @Test
    public void findsHighestAvgRatedGame() {
        Assert.assertEquals(game1, ratingDAO.findsHighestAvgRatedGame());
    }

    /**
     * Tests the method updateRatingForGame.
     */
    @Test
    public void updateRatingForGame() {

        int val = ratingDAO.findRatingsByGameNameAndUserMail("Game1", "mail1");
        Assert.assertEquals(4, val);

        ratingDAO.updateRatingForGame(game1.getName(), user1.getMail(), 3);

        val = ratingDAO.findRatingsByGameNameAndUserMail("Game1", "mail1");
        Assert.assertEquals(3, val);

        ratingDAO.updateRatingForGame(game1.getName(), user1.getMail(), 4);

        val = ratingDAO.findRatingsByGameNameAndUserMail("Game1", "mail1");
        Assert.assertEquals(4, val);
    }

    /**
     * Tests the method updateRatingForGame when there is no rating.
     */
    @Test
    public void updateRatingForGameNoRating() {

        Game game2 = gameDAO.createGame("Game2", "author", "description", "javaScriptPath", "imagePath");

        gameDAO.create(game1);
        gameDAO.getEntityManager().flush();

        Assert.assertFalse(ratingDAO.updateRatingForGame(game2.getName(), user1.getMail(), 3));

        gameDAO.getEntityManager().refresh(game1);
        gameDAO.remove(game2);
    }

    /**
     * Tests the methods avgRatingForGameName
     */
    @Test
    public void avgRatingForGameName() {
        Assert.assertTrue(ratingDAO.avgRatingForGameName(rating1.getGame().getName()).equals(4.0));
    }

    /**
     * Tests the methods avgRatingForGameName when there is no rating
     */
    @Test
    public void avgRatingForGameNameNoRating() {

        Game game2 = gameDAO.createGame("Game2", "author", "description", "javaScriptPath", "imagePath");

        gameDAO.create(game1);
        gameDAO.getEntityManager().flush();

        Assert.assertTrue(ratingDAO.avgRatingForGameName(game2.getName()).equals(0.0));

        gameDAO.getEntityManager().refresh(game1);
        gameDAO.remove(game2);
    }

    /**
     * Removes entites created by init from the database after every test.
     * Closes UserTransaction.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {

        ratingDAO.getEntityManager().refresh(rating1);
        gameDAO.getEntityManager().refresh(game1);
        userAccountDAO.getEntityManager().refresh(user1);

        ratingDAO.remove(rating1);
        gameDAO.remove(game1);
        userAccountDAO.remove(user1);

        tx.commit();
    }
}
