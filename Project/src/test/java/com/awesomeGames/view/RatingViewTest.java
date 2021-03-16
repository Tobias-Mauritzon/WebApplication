package com.awesomeGames.view;

import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.RatingDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author David
 */
@RunWith(Arquillian.class)
public class RatingViewTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(RatingDAO.class, Rating.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class,
                        Comment.class, HighScore.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    RatingView ratingView;
    CurrentGameView currentGameView;

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
        ratingView.setRatingDAO(ratingDAO);
        ratingView.setCurrentGameView(currentGameView);
    }

    @Test
    public void setGetRatingTest() {
        ratingView.setRating(9);
        //make sure the value is int
        if (ratingView.getRating() == (int) ratingView.getRating()) {
            Assert.assertEquals(9, (int) ratingView.getRating());
        } else {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void setGetGameTest() {
        ratingView.setGame("game");
        //make sure the value is int
        Assert.assertEquals("game", ratingView.getGame());
    }

    @Test
    public void setGetAvgRatingTest() {
        ratingView.setAvgRating(2);
        //make sure the value is int
        Assert.assertEquals(2, ratingView.getAvgRating());
    }

    @Test
    public void initTest() {
        currentGameView.setGame("game1");
        ratingView.testInit();
        Assert.assertEquals("game1", ratingView.getGame());
        Assert.assertEquals(0, ratingView.getAvgRating());

    }

    @Test
    public void averageRatingTest() throws Exception {
        tx.begin();
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        UserAccount user2 = new UserAccount("mail2", "name2", "USER", "password1");
        userAccountDAO.create(user1);
        userAccountDAO.create(user2);
        Game game1 = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");
        Rating rating1 = new Rating(game1, user1, 1);
        Rating rating2 = new Rating(game1, user2, 4);
        ratingDAO.create(rating1);
        ratingDAO.create(rating2);

        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        ratingDAO.getEntityManager().flush();

        ratingView.setGame("Game1");
        ratingView.setGame(game1.getName());
        ratingView.getAverageRating();
        Assert.assertEquals(3, ratingView.getAvgRating());

        ratingDAO.getEntityManager().refresh(rating1);
        ratingDAO.getEntityManager().refresh(rating2);
        gameDAO.getEntityManager().refresh(game1);
        userAccountDAO.getEntityManager().refresh(user1);
        userAccountDAO.getEntityManager().refresh(user2);

        ratingDAO.remove(rating1);
        ratingDAO.remove(rating2);
        gameDAO.remove(game1);
        userAccountDAO.remove(user1);
        userAccountDAO.remove(user2);

        tx.commit();
    }

    @Test
    public void GetDAOs() throws Exception {
        Assert.assertEquals(ratingDAO, ratingView.getRatingDAO());
        Assert.assertEquals(currentGameView, ratingView.getCurrentGameView());
    }

}
