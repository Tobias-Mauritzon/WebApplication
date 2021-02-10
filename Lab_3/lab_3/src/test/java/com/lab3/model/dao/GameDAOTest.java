/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.entity.Game;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
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


    @Inject
    private UserTransaction tx;  
    
    Game game1;
    Game game2;
    
    @Before
    public void init() throws Exception{
        tx.begin();
    }


    @After
    public void tearDown() throws Exception {
        tx.commit();
    }
    
    @Test
    public void create_game() throws Exception{
        game1 = new Game("SnaKe");
        game2 = new Game("Mario Bros");

        gameDAO.create(game1);
        gameDAO.create(game2);
        gameDAO.getEntityManager().flush();
        Game[] games = {game2,game1};
        Assert.assertArrayEquals(games,gameDAO.allGames().toArray());
        

        //Refresh before remve
        gameDAO.getEntityManager().refresh(game2);
        
        gameDAO.remove(game2);

        Game[] games2 = {game1};
        Assert.assertArrayEquals(games2,gameDAO.allGames().toArray());
        
        System.out.println("QUERY RESULT gameDAOtest allGames: " + gameDAO.allGames());
        
        gameDAO.getEntityManager().refresh(game1);
        gameDAO.remove(game1);
    }
    
    
}