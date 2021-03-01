/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import java.sql.Timestamp;
import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.UserAccount;
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
 * Test class for the Comment DAO
 * @author Matteus
 */
@RunWith(Arquillian.class)
public class CommentDAOTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(CommentDAO.class, Comment.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class, 
                            Rating.class,  HighScore.class )
            .addAsResource("META-INF/persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private CommentDAO commentDAO;
        
    @EJB
    private UserAccountDAO userAccountDAO;
      
    @EJB
    private GameDAO gameDAO;
    
    UserAccount user5;
    Game game5;
    Comment comment1;
    Comment comment2;

    @Before
    public void init() throws Exception{
        //starts transaction
        tx.begin();

        //create entities
        user5 = new UserAccount("mail5", "name5", "USER", "password5");
        game5 = new Game("Game5", "desc", "testPath");
        comment1 = commentDAO.createComment(game5, user5, "commentText1");
        comment2 = commentDAO.createComment(game5, user5, "commentText2");

        userAccountDAO.create(user5);
        gameDAO.create(game5);
        commentDAO.create(comment1);
        commentDAO.create(comment2);
        
        //flush after create
        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        commentDAO.getEntityManager().flush();
    }
    
    @Inject
    private UserTransaction tx;  //transaction needed for testing
     
    @Test
	public void createComment() throws Exception{
        Assert.assertTrue(commentDAO.findAll().size() == 2);
	}
    
    @Test
	public void findCommentsWithUserASC() throws Exception{
        
        List<Comment> list = commentDAO.findCommentsWithUserASC(user5);
        Assert.assertTrue(list.get(0).equals(comment1));
        Assert.assertTrue(list.get(1).equals(comment2));          
	}
    
        @Test
	public void findCommentsWithUserDESC() throws Exception{
        
        List<Comment> list = commentDAO.findCommentsWithUserDESC(user5);
        Assert.assertTrue(list.get(0).equals(comment2));
        Assert.assertTrue(list.get(1).equals(comment1));      
	}
    
    @Test
	public void findCommentsWithGameASC() throws Exception{
       
        List<Comment> list = commentDAO.findCommentsWithGameASC(game5);
        Assert.assertTrue(list.get(0).equals(comment1));
        Assert.assertTrue(list.get(1).equals(comment2));
	}
    
        @Test
	public void findCommentsWithGameDESC() throws Exception{
       
        List<Comment> list = commentDAO.findCommentsWithGameDESC(game5);
        Assert.assertTrue(list.get(0).equals(comment2));
        Assert.assertTrue(list.get(1).equals(comment1));
	}
    
    @After
    public void tearDown() throws Exception {
        //refresh before delete
        userAccountDAO.getEntityManager().refresh(user5);
        gameDAO.getEntityManager().refresh(game5);
        commentDAO.getEntityManager().refresh(comment1);
        commentDAO.getEntityManager().refresh(comment2);

        commentDAO.remove(comment1);
        commentDAO.remove(comment2);
        gameDAO.remove(game5);
        userAccountDAO.remove(user5); 
        
        //ends the transaction
        tx.commit();
    }
}
