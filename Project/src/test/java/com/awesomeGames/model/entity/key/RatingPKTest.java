/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awesomeGames.model.entity.key;
import com.awesomeGames.model.entity.key.RatingPK;
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
public class RatingPKTest {

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
      RatingPK raitingPK = new RatingPK();
      Assert.assertNotNull(raitingPK);
        
    }
    
    @Test
    public void hashCodeEqualsTest() {
        RatingPK ratingPK1 = new RatingPK("game","user");
        RatingPK ratingPK2 = new RatingPK("game","user");
        int r1 = ratingPK1.hashCode();
        int r2 = ratingPK2.hashCode();
        Assert.assertEquals(r1,r2);
        Assert.assertTrue(ratingPK1.equals(ratingPK2));
    }
        
}