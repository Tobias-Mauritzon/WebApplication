/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.Users;
import java.sql.Timestamp;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
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

@RunWith(Arquillian.class)
public class CommentDAOTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(CommentDAO.class, Comment.class, UsersDAO.class, Users.class, GameDAO.class, Game.class)
            .addAsResource("META-INF/persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private CommentDAO commentDAO;
        
    @EJB
    private UsersDAO usersDAO;
      
    @EJB
    private GameDAO gameDAO;

    @Before
    public void init() {
    }
    
    @Inject
    private UserTransaction tx;  //transaction needed for testing

    @Test
    public void create_comment() throws Exception{
        //starts transaction
        tx.begin();
        
        //create entities
        Users user5 = new Users("mail5", "name5", "password5");
        Game game5 = new Game("Game5");
        Comment comment1 = new Comment(user5,game5,"comment_text1",new Timestamp(System.currentTimeMillis()));
        Comment comment2 = new Comment(user5,game5,"comment_text2",new Timestamp(System.currentTimeMillis()));
        
        usersDAO.create(user5);
        gameDAO.create(game5);
        commentDAO.create(comment1);
        commentDAO.create(comment2);
        //flush after create
        usersDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        commentDAO.getEntityManager().flush();
        
        System.out.println("Query Result commentDAOtest findCommentsWithUsername: " + commentDAO.findCommentsWithUsername("mail5"));
        
        //refresh before delete
        usersDAO.getEntityManager().refresh(user5);
        gameDAO.getEntityManager().refresh(game5);
        commentDAO.getEntityManager().refresh(comment1);
        commentDAO.getEntityManager().refresh(comment2);
        
        //delete game and user comment should be removed by cascade
        gameDAO.remove(game5);
        usersDAO.remove(user5);
        Assert.assertTrue(true);
        
        //ends the transaction
        tx.commit();
    }
}
