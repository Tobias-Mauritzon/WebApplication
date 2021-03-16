package com.awesomeGames.model.entity.key;

import com.awesomeGames.model.entity.key.CommentPK;
import com.awesomeGames.model.dao.CommentDAO;
import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
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
public class CommentPKTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(CommentDAO.class, Comment.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class,
                        Rating.class, HighScore.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void noArgsConstructorTest() {
        CommentPK commentPK = new CommentPK();
        Assert.assertNotNull(commentPK);

    }

    @Test
    public void AllArgsConstructorTest() {
        CommentPK commentPK = new CommentPK(2, "user", "game");
        Assert.assertNotNull(commentPK);

    }

    @Test
    public void hashCodeEqualsTest() {
        CommentPK commentPK1 = new CommentPK(2, "user", "game");
        CommentPK commentPK2 = new CommentPK(2, "user", "game");
        int r1 = commentPK1.hashCode();
        int r2 = commentPK2.hashCode();
        Assert.assertEquals(r1, r2);
        Assert.assertTrue(commentPK1.equals(commentPK2));
    }

}
