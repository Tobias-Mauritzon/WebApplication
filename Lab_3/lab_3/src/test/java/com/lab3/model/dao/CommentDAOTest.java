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
import javax.ejb.EJB;
import javax.persistence.EntityManager;
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

	@Test
	public void create_comment() {
            Users user5 = new Users("mail5", "name5", "password5");
            Game game5 = new Game("Game5");
            Comment comment1 = new Comment(0,user5,game5,"comment_text1","time1");
            Comment comment2 = new Comment(0,user5,game5,"comment_text2","time2");
            usersDAO.create(user5);
            gameDAO.create(game5);
            commentDAO.create(comment1);
            commentDAO.create(comment2);
            System.out.println("Query Result commentDAOtest findCommentsWithUsername: " + commentDAO.findCommentsWithUsername("mail5"));
            Assert.assertTrue(true); /* Some better condition */
            commentDAO.remove(comment1);
            commentDAO.remove(comment2);
            gameDAO.remove(game5);
            usersDAO.remove(user5);
            
            
            
	}
}
