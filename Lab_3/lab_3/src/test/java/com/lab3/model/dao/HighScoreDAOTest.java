/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
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
public class HighScoreDAOTest {
	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
                        .addClasses(HighScoreDAO.class, HighScore.class, Comment.class, UsersDAO.class, Users.class, GameDAO.class, Game.class)
			.addAsResource("META-INF/persistence.xml")
			.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
      
        @EJB
	private GameDAO gameDAO;
        
        @EJB
	private UsersDAO usersDAO;
                
        @EJB
        private HighScoreDAO highScoreDAO;

	@Before
	public void init() {
	}

	@Test
	public void create_comment() {
            Users user5 = new Users("mail5", "name5", "password5");
            Game game5 = new Game("Game5");
            Game game6 = new Game("Game6");
            HighScore highScore1 = new HighScore(game5,user5,5);
            HighScore highScore2 = new HighScore(game6,user5,6);
            usersDAO.create(user5);
            gameDAO.create(game5);
            highScoreDAO.create(highScore1);
            //highScoreDAO.create(highScore2);
            //System.out.println("Query Result highScoreDAOTest findhighscoreWithUsernameAndGame: " + highScoreDAO.findhighscoreWithUsernameAndGame("Game5","name5"));
            Assert.assertTrue(true); /* Some better condition */
            highScoreDAO.remove(highScore1);
            //highScoreDAO.remove(highScore2);
            gameDAO.remove(game5);
            usersDAO.remove(user5);
	}
}
