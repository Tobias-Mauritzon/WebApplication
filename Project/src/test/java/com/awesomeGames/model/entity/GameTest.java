package com.awesomeGames.model.entity;

import com.awesomeGames.model.dao.CommentDAO;
import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 *
 * @author David
 */
@RunWith(Arquillian.class)
public class GameTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(CommentDAO.class, Comment.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class,
                        Rating.class, HighScore.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void setGetHighScore() {
        List<HighScore> highScores = new ArrayList();
        Game game = new Game("Game1", "author", "description", "javaScriptPath", "imagePath", new Timestamp(System.currentTimeMillis()));
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        UserAccount user2 = new UserAccount("mail2", "name2", "USER", "password2");
        highScores.add(new HighScore(game, user1, 10));
        highScores.add(new HighScore(game, user2, 11));
        game.setHighScore(highScores);
        Assert.assertEquals(highScores, game.getHighScore());

    }

    @Test
    public void setGetComment() {
        List<Comment> comments = new ArrayList();
        Game game = new Game("Game1", "author", "description", "javaScriptPath", "imagePath", new Timestamp(System.currentTimeMillis()));
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        UserAccount user2 = new UserAccount("mail2", "name2", "USER", "password2");
        comments.add(new Comment(user1, game, "text", new Timestamp(System.currentTimeMillis())));
        comments.add(new Comment(user2, game, "text", new Timestamp(System.currentTimeMillis())));
        game.setComment(comments);
        Assert.assertEquals(comments, game.getComment());
    }

    @Test
    public void setGetRating() {
        List<Rating> ratings = new ArrayList();
        Game game = new Game("Game1", "author", "description", "javaScriptPath", "imagePath", new Timestamp(System.currentTimeMillis()));
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        UserAccount user2 = new UserAccount("mail2", "name2", "USER", "password2");
        ratings.add(new Rating(game, user1, 4));
        ratings.add(new Rating(game, user2, 3));
        game.setRating(ratings);
        Assert.assertEquals(ratings, game.getRating());
    }
}
