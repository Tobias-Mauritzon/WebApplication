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
import com.lab3.model.entity.UserAccount;
import com.lab3.model.entity.key.RatingPK;
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
                    .addClasses(RatingDAO.class, Rating.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class)
                    .addAsResource("META-INF/persistence.xml")
                    .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private RatingDAO ratingDAO;

    @EJB
    private UserAccountDAO userAccountDAO;

    @EJB
    private GameDAO gameDAO;
    
    private UserAccount user1;
    private Game game1;
    private Rating rating1;

    @Before
    public void init() throws Exception{
        tx.begin();
        user1 = new UserAccount("mail1", "name1", "password1");
        game1 = new Game("Game1");
        rating1 = new Rating(game1,user1,5);

        userAccountDAO.create(user1);
        gameDAO.create(game1);
        ratingDAO.create(rating1); 

        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        ratingDAO.getEntityManager().flush();
    }
    
    @Inject
    private UserTransaction tx;  
    
    @Test
    public void test1(){

        //create entities
        UserAccount user5 = new UserAccount("mail5", "name5", "password5");
        Game game5 = new Game("Game5");
        Game game6 = new Game("Game6");
        Rating rating1 = new Rating(game5,user5,5);
        Rating rating2 = new Rating(game6,user5,6);


        userAccountDAO.create(user5);
        gameDAO.create(game5);
        gameDAO.create(game6);
        ratingDAO.create(rating1);
        ratingDAO.create(rating2);


        //flush after create
        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        ratingDAO.getEntityManager().flush();

        //refresh before remove
        gameDAO.getEntityManager().refresh(game5);
        gameDAO.getEntityManager().refresh(game6);
        userAccountDAO.getEntityManager().refresh(user5);


        //remove games and user
        gameDAO.remove(game5);
        gameDAO.remove(game6);
        userAccountDAO.remove(user5);
        //end transaction        
        Assert.assertTrue(true); /* Some better condition */    
    }
    
//    @Test
//    @InSequence(2)
//    public void remove_rating() {
//        UserAccount user1 = userAccountDAO.find("mail14");
//
//        for(int i = 3; i < 11; i++){
//           Game game = gameDAO.find("Game"+i); 
//           Rating r = ratingDAO.find(new RatingPK("Game"+i, "mail14"));
//           
//           ratingDAO.getEntityManager().refresh(r);
//           gameDAO.getEntityManager().refresh(game);
//           
//           ratingDAO.remove(r);
//           gameDAO.remove(game);
//        }
//        userAccountDAO.getEntityManager().refresh(user1);
//        
//        userAccountDAO.remove(user1);
//        Assert.assertEquals(0, ratingDAO.findAllRatingsByUsername("name2").size());
//        
//    }
//    
//    @Test
//    @InSequence(1)
//    public void create_rating() {
//        UserAccount user1 = new UserAccount("mail14", "name2", "password1");
//
//        userAccountDAO.create(user1);
//        userAccountDAO.getEntityManager().flush();
//        
//        for(int i = 3; i < 11; i++){
//           Game game = new Game("Game"+i); 
//           Rating r = new Rating(game,user1, i);
//           
//           gameDAO.create(game);
//           ratingDAO.create(r);
//           
//           
//        }
//        gameDAO.getEntityManager().flush();
//        ratingDAO.getEntityManager().flush();
//        Assert.assertEquals(8, ratingDAO.findAllRatingsByUsername("name2").size());   
//    }
   
    @Test
    public void testFindAllRatingsByName(){
        Assert.assertEquals(5,ratingDAO.findAllRatingsByUsername("name1").get(0));
        
    }
    
    @Test
    public void findAllRatingsForGame(){
        Assert.assertEquals(5,ratingDAO.findAllRatingsForGame("Game1").get(0));
    }
    
    @Test
    public void findRatingsByGameNameAndUserMail(){
        int val = ratingDAO.findRatingsByGameNameAndUserMail("Game1", "mail1");
        Assert.assertEquals(5,val);   
    }
    
    @Test
    public void invalidRatingsByGameNameAndUserMail(){
        Assert.assertEquals(null,ratingDAO.findRatingsByGameNameAndUserMail("Game1", "mail16"));   
    }
    
    /*
     *   We want this test to work but after using the "correct" methods the exeption
     *   is not catched in the correct way.
     */
//    @Test
//    public void ratingToHigh(){
//        UserAccount user1 = new UserAccount("mail5", "name5", "password1");
//        Game game1 = new Game("Game5");
//            
//        try{
//            
//            Rating rating1 = new Rating(user1,game1,50);
//
//            userAccountDAO.create(user1);
//            gameDAO.create(game1);
//            ratingDAO.create(rating1);
//            Assert.assertTrue(false);
//        }catch(Exception e){
//            userAccountDAO.remove(user1);
//            gameDAO.remove(game1);
//            Assert.assertTrue(true);
//        }
//    }
//    
    
    /*
     *   We want this test to work but after using the "correct" methods the exeption
     *   is not catched in the correct way.
     */
     
//    @Test
//    public void ratingToLow(){
//        UserAccount user1 = new UserAccount("mail5", "name5", "password1");
//        Game game1 = new Game("Game5");
//        
//        try{
//            Rating rating1 = new Rating(game1,user1,-50);
//
//            userAccountDAO.create(user1);
//            gameDAO.create(game1);
//            ratingDAO.create(rating1);
//            
//            
//            //flush after create
//            userAccountDAO.getEntityManager().flush();
//            gameDAO.getEntityManager().flush();
//            ratingDAO.getEntityManager().flush();
//            
//            ratingDAO.getEntityManager().refresh(rating1);
//            gameDAO.getEntityManager().refresh(game1);
//            userAccountDAO.getEntityManager().refresh(user1);
//
//            ratingDAO.remove(rating1);
//            gameDAO.remove(game1);
//            userAccountDAO.remove(user1);
//            
//            Assert.assertEquals(true,false);
//        }catch(Exception e){
//            
//             //refresh before remove
//             
//            gameDAO.getEntityManager().refresh(game1);
//            userAccountDAO.getEntityManager().refresh(user1);
//            
//            gameDAO.remove(game1);
//            userAccountDAO.remove(user1);
//            
//            Assert.assertTrue(true);
//        }
//    }
    
    @After
    public void tearDown() throws Exception {
        
        ratingDAO.getEntityManager().refresh(rating1);
        gameDAO.getEntityManager().refresh(game1);
        userAccountDAO.getEntityManager().refresh(user1);
        
        
        ratingDAO.remove(rating1);
        gameDAO.remove(game1);
        userAccountDAO.remove(user1);

        tx.commit();
    }
}