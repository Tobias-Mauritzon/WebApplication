/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.dao.RatingDAO;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.Users;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Matteus
 * @author Tobias
 */
@RunWith(Arquillian.class)
public class RatingDAOTest {
    @Deployment
    public static WebArchive createDeployment() {
            return ShrinkWrap.create(WebArchive.class)
                    .addClasses(RatingDAO.class, Rating.class, UsersDAO.class, Users.class, GameDAO.class, Game.class)
                    .addAsResource("META-INF/persistence.xml")
                    .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private RatingDAO ratingDAO;

    @EJB
    private UsersDAO usersDAO;

    @EJB
    private GameDAO gameDAO;

    @Before
    public void init() {
//        Users user1 = new Users("mail1", "name1", "password1");
//        Game game1 = new Game("Game1");
//        Rating rating1 = new Rating(user1,game1,5);
//
//        usersDAO.create(user1);
//        gameDAO.create(game1);
//        ratingDAO.create(rating1);   
    }
    
    @Inject
    private UserTransaction tx;  
    
    @Test
    public void test1() throws Exception {
        //starts transaction
        tx.begin();

        //create entities
        Users user5 = new Users("mail5", "name5", "password5");
        Game game5 = new Game("Game5");
        Game game6 = new Game("Game6");
        Rating rating1 = new Rating(game5,user5,5);
        Rating rating2 = new Rating(game6,user5,6);


        usersDAO.create(user5);
        gameDAO.create(game5);
        gameDAO.create(game6);
        ratingDAO.create(rating1);
        ratingDAO.create(rating2);


        //flush after create
        usersDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        ratingDAO.getEntityManager().flush();

        //refresh before remove
        gameDAO.getEntityManager().refresh(game5);
        gameDAO.getEntityManager().refresh(game6);
        usersDAO.getEntityManager().refresh(user5);


        //remove games and user
        gameDAO.remove(game5);
        gameDAO.remove(game6);
        usersDAO.remove(user5);
        //end transaction
        tx.commit();
        Assert.assertTrue(true); /* Some better condition */    }

//    @Test
//    @InSequence(2)
//    public void remove_rating() {
//        Users user1 = new Users("mail14", "name2", "password1");
//
//       
//        
//        for(int i = 3; i < 11; i++){
//           Game game = new Game("Game"+i); 
//           Rating r = new Rating(user1, game, i);
//           ratingDAO.remove(r);
//           gameDAO.remove(game);
//        }
//        
//        usersDAO.remove(user1);
//        System.out.println("remove_rating Name "+ ratingDAO.findAllRatingsByUserName("name2"));
//        Assert.assertEquals(0, ratingDAO.findAllRatingsByUserName("name2").size());
//    }
//    
//    @Test
//    @InSequence(1)
//    public void create_rating() {
//        Users user1 = new Users("mail14", "name2", "password1");
//
//        usersDAO.create(user1);
//        
//        for(int i = 3; i < 11; i++){
//           Game game = new Game("Game"+i); 
//           Rating r = new Rating(user1, game, i);
//           
//           gameDAO.create(game);
//           ratingDAO.create(r);
//        }
//        System.out.println("create_rating Name "+ ratingDAO.findAllRatingsByUserName("name2"));
//        Assert.assertEquals(8, ratingDAO.findAllRatingsByUserName("name2").size());
//    }
//    
//    @Test
//    public void testFindAllRatingsByName(){
//        System.out.println("RatingTest Name "+ ratingDAO.findAllRatingsByUserName("name1"));
//        Assert.assertEquals(5,ratingDAO.findAllRatingsByUserName("name1").get(0));
//        
//    }
//    
//    @Test
//    public void findAllRatingsForGame(){
//        System.out.println("RatingTest Game "+ ratingDAO.findAllRatingsForGame("Game1"));
//        Assert.assertEquals(5,ratingDAO.findAllRatingsForGame("Game1").get(0));
//    }
//    
//    @Test
//    public void ratingToHigh(){
//        Users user1 = new Users("mail5", "name5", "password1");
//        Game game1 = new Game("Game5");
//            
//        try{
//            
//            Rating rating1 = new Rating(user1,game1,50);
//
//            usersDAO.create(user1);
//            gameDAO.create(game1);
//            ratingDAO.create(rating1);
//            Assert.assertTrue(false);
//        }catch(Exception e){
//            usersDAO.remove(user1);
//            gameDAO.remove(game1);
//            Assert.assertTrue(true);
//        }
//    }
//    
//    @Test
//    public void ratingToLow(){
//        Users user1 = new Users("mail5", "name5", "password1");
//        Game game1 = new Game("Game5");
//        
//        try{
//            Rating rating1 = new Rating(user1,game1,-50);
//
//            usersDAO.create(user1);
//            gameDAO.create(game1);
//            ratingDAO.create(rating1);
//            Assert.assertTrue(false);
//        }catch(Exception e){
//            usersDAO.remove(user1);
//            gameDAO.remove(game1);
//            Assert.assertTrue(true);
//        }
//    }
    
    @After
    public void tearDown()  {
//        Users user1 = new Users("mail1", "name1", "password1");
//        Game game1 = new Game("Game1");
//        Rating rating1 = new Rating(user1,game1,5);
//        
//        ratingDAO.remove(rating1);
//        gameDAO.remove(game1);
//        usersDAO.remove(user1);
    }
}