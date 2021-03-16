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
import com.lab3.model.entity.UserAccount;
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
 * Tests for GameDAO
 *
 * @author Matteus
 * @author Simon
 */
@RunWith(Arquillian.class)
public class GameDAOTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(GameDAO.class, UserAccount.class,
                        Rating.class, Comment.class, HighScore.class, Game.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private GameDAO gameDAO;

    @Inject
    private UserTransaction tx;

    Game game1;
    Game game2;

    /**
     *
     * @throws Exception if UserTransaction logging fails
     */
    @Before
    public void init() throws Exception {
        //starts transaction
        tx.begin();

        //create entities
        game1 = gameDAO.createGame("game1", "author1", "description1", "javaScriptPath1", "imagePath1");

        // Flush inbetween so that they dont get the same timestamp
        gameDAO.getEntityManager().flush();

        game2 = gameDAO.createGame("game2", "author2", "description2", "javaScriptPath2", "imagePath2");

        //flush after create
        gameDAO.getEntityManager().flush();

    }

    /**
     * Testing to create, fetch and delete game entities
     *
     * @throws Exception
     */
    @Test
    public void create_game() {
        Assert.assertTrue(gameDAO.findAll().size() == 2);
    }

    /**
     * Test for the method findGameMathingName.
     */
    @Test
    public void findGameWithName() {

        Assert.assertEquals(game1, gameDAO.findGameMatchingName("game1"));
        Assert.assertEquals(game2, gameDAO.findGameMatchingName("game2"));
    }

    /**
     * Test for the method findJavaScriptPathByName.
     */
    @Test
    public void findJavaScriptPathByName() {

        Assert.assertEquals(game1.getJavaScript(), gameDAO.findJavaScriptPathByName("game1"));
        Assert.assertEquals(game2.getJavaScript(), gameDAO.findJavaScriptPathByName("game2"));
    }

    /**
     * Test for the method findNewestGame.
     */
    @Test
    public void findNewestGame() {
        Assert.assertEquals(game2, gameDAO.findNewestGame());
        Assert.assertFalse(game1.equals(gameDAO.findNewestGame()));
    }

    /**
     * @throws Exception Throws if UserTransaction "commit" fails
     */
    @After
    public void tearDown() throws Exception {
        //refresh before delete
        gameDAO.getEntityManager().refresh(game1);
        gameDAO.getEntityManager().refresh(game2);

        gameDAO.remove(game1);
        gameDAO.remove(game2);

        //ends the transaction
        tx.commit();
    }
}
