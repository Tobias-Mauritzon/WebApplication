package com.awesomeGames.view;

import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.RatingDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
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
public class CurrentGameViewTest {

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
        currentGameView.setGameDAO(gameDAO);
        currentGameView.setUserAccountDAO(userAccountDAO);
    }

    @Test
    public void testInit() throws Exception {
        tx.begin();
        Game game1 = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");
        gameDAO.getEntityManager().flush();

        currentGameView.testInit();
        SelectItem temp = (SelectItem) currentGameView.getGames().get(0);
        Assert.assertEquals("Game1", temp.getLabel());
        Assert.assertEquals("Game1", temp.getValue());

        gameDAO.getEntityManager().refresh(game1);

        gameDAO.remove(game1);

        tx.commit();
    }

    @Test
    public void setGetUser() throws Exception {
        tx.begin();
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        userAccountDAO.create(user1);

        currentGameView.setUser("name1");

        Assert.assertEquals(user1, currentGameView.getUser());

        userAccountDAO.getEntityManager().refresh(user1);
        userAccountDAO.remove(user1);
        tx.commit();
    }

    @Test
    public void setGetGameObject() throws Exception {
        tx.begin();
        Game game1 = gameDAO.createGame("Game1", "author", "description", "javaScriptPath", "imagePath");

        currentGameView.setGameObject(game1.getName());

        Assert.assertEquals(game1, currentGameView.getGameObject());

        gameDAO.getEntityManager().refresh(game1);
        gameDAO.remove(game1);
        tx.commit();
    }

    @Test
    public void setGetGameList() throws Exception {
        List<Game> games = new ArrayList();
        games.add(new Game("Game1", "author", "description", "javaScriptPath", "imagePath", new Timestamp(System.currentTimeMillis())));
        games.add(new Game("Game1", "author", "description", "javaScriptPath", "imagePath", new Timestamp(System.currentTimeMillis())));
        currentGameView.setGameList(games);
        Assert.assertTrue(games.get(0).equals(currentGameView.getGameList().get(0)));
        Assert.assertTrue(games.get(1).equals(currentGameView.getGameList().get(1)));
    }

    @Test
    public void GetDAOs() throws Exception {
        Assert.assertEquals(gameDAO, currentGameView.getGameDAO());
        Assert.assertEquals(userAccountDAO, currentGameView.getUserAccountDAO());
    }

}
