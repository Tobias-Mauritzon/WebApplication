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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import static javax.ws.rs.client.Entity.entity;
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

        @Inject
        private UserTransaction tx;  
        
	@Test
	public void create_highscore() throws Exception {
            tx.begin();
            
            Users user5 = new Users("mail5", "name5", "password5");
            Game game5 = new Game("Game5");
            Game game6 = new Game("Game6");
           
//            HighScore highScore1 = new HighScore(user5,5);
//            HighScore highScore2 = new HighScore(user5,6);
            usersDAO.create(user5);
            gameDAO.create(game5);
            gameDAO.create(game6);
            gameDAO.getEntityManager().flush();
            HighScore highScore1 = new HighScore(gameDAO.find("Game5"),usersDAO.find("mail5"));
            HighScore highScore2 = new HighScore(game6,user5);

            highScoreDAO.create(highScore1);
            highScoreDAO.create(highScore2);
            highScoreDAO.getEntityManager().flush();
            
            gameDAO.getEntityManager().refresh(game5);
            gameDAO.getEntityManager().refresh(game6);
            usersDAO.getEntityManager().refresh(user5);
            gameDAO.remove(game5);
            usersDAO.remove(user5);
            tx.commit();
            Assert.assertTrue(true); /* Some better condition */

            
	}
}
