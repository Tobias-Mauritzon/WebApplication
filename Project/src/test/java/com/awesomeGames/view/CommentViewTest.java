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
public class CommentViewTest {

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
    CommentView commentView;

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
        commentView = new CommentView();
        currentGameView = new CurrentGameView();
        commentView.setCurrentGameView(currentGameView);
    }

    @Test
    public void initTest() {
        currentGameView.setGame("Game1");
        commentView.testInit();
        Assert.assertTrue(commentView.getDescending());
        Assert.assertEquals("Game1", commentView.getGameName());
    }

    @Test
    public void getDAOsTest() {
        Assert.assertEquals(currentGameView, commentView.getCurrentGameView());
    }

    @Test
    public void getSetGameTest() {
        Game game = new Game("Game1", "author", "description", "javaScriptPath", "imagePath", new Timestamp(System.currentTimeMillis()));
        commentView.setGame(game);
        Assert.assertEquals(game, commentView.getGame());

    }

    @Test
    public void getSetcommentListTest() {
        UserAccount user = new UserAccount("mail1", "name1", "USER", "password1");
        Game game = new Game("Game1", "author", "description", "javaScriptPath", "imagePath", new Timestamp(System.currentTimeMillis()));

        List<Comment> comments = new ArrayList();
        comments.add(new Comment(user, game, "text", new Timestamp(System.currentTimeMillis())));
        comments.add(new Comment(user, game, "text", new Timestamp(System.currentTimeMillis())));
        commentView.setCommentList(comments);
        Assert.assertEquals(comments.get(0), commentView.getCommentList().get(0));
        Assert.assertEquals(comments.get(1), commentView.getCommentList().get(1));

    }

    @Test
    public void getSetTextTest() {
        commentView.setText("text");
        Assert.assertEquals("text", commentView.getText());
    }

}
