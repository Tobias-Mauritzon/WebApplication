/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.UserAccount;
import java.util.ArrayList;
import java.util.List;
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

/**
 * Tests for GameDAO
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
    public void init() throws Exception{
        tx.begin();
    }

    /**
     * 
     * @throws Exception Throws if UserTransaction "commit" fails
     */
    @After
    public void tearDown() throws Exception {
        tx.commit();
    }
    
    private boolean checkElements(List<Game> excpected, List<Game> actual){
        int found = 0;
        if(excpected.size() != actual.size()){
            return false;
        }
        
        for(int i = 0; i < excpected.size(); i++){
            for(int j = 0; j < actual.size(); j++){
                if(excpected.get(i) == actual.get(j)){
                    ++found;
                 
                }
            }
        }
        
        if(found == excpected.size()){
            return true;
        }
        return false;
    }
    
    /**
     * Testing to create, fetch and delete game entities
     * @throws Exception 
     */
    @Test
    public void create_game(){
        game1 = new Game("SnaKe", "go brrr");
        game2 = new Game("Mario Bros", "NoKia");

        gameDAO.create(game1);
        gameDAO.create(game2);
        gameDAO.getEntityManager().flush();
        
        List<Game> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);
        
        boolean isArrayContentSame;
        isArrayContentSame = checkElements(games, gameDAO.allGames());
        Assert.assertTrue(isArrayContentSame);

        gameDAO.getEntityManager().refresh(game2);
        gameDAO.remove(game2);

        Game[] games2 = {game1};
        Assert.assertArrayEquals(games2,gameDAO.allGames().toArray());
        
        System.out.println("QUERY RESULT gameDAOtest allGames: " + gameDAO.allGames());
        
        gameDAO.getEntityManager().refresh(game1);
        gameDAO.remove(game1);
    }
    
    /**
     * Test for the method findGameMathingName.
     */
    @Test
    public void findGameWithName(){
        game1 = new Game("SnaKe", "slither");

        gameDAO.create(game1);
        gameDAO.getEntityManager().flush();
        
        Assert.assertEquals(game1, gameDAO.findGameMatchingName("SnaKe"));
        Assert.assertEquals("slither", game1.getDescription());
        
        gameDAO.getEntityManager().refresh(game1);
        gameDAO.remove(game1);
    }
    //implement
//    /**
//     * Add the same game multiple times
//     */
//    @Test
//    public void addSameGame(){
//        
//    }
}