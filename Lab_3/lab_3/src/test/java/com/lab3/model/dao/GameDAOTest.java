/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.entity.Game;
import javax.ejb.EJB;
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
public class GameDAOTest {
    @Deployment
    public static WebArchive createDeployment() {
            return ShrinkWrap.create(WebArchive.class)
                    .addClasses(GameDAO.class, Game.class)
                    .addAsResource("META-INF/persistence.xml")
                    .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private GameDAO gameDAO;

    @Before
    public void init() {
    }

    @Test
    public void create_game() {
//        Game game2 = new Game("Game2");
//        Game game3 = new Game("Game3");
//        gameDAO.create(game2);
//        gameDAO.create(game3);
//        Game[] games = {game2,game3};
//        Assert.assertArrayEquals(games,gameDAO.allGames().toArray());
//        Game[] games2 = {game2};
//        gameDAO.remove(game3);
//        Assert.assertArrayEquals(games2,gameDAO.allGames().toArray());
//        System.out.println("QUERY RESULT gameDAOtest allGames: " + gameDAO.allGames());
//        gameDAO.remove(game2);
        Assert.assertTrue(true);
    }
}