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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for the UserAccount DAO
 * @author Matteus
 */
@RunWith(Arquillian.class)
public class UserAccountDAOTest {
    @Deployment
    public static WebArchive createDeployment() {
            return ShrinkWrap.create(WebArchive.class)
                    .addClasses(UserAccountDAO.class, UserAccount.class, 
                            Rating.class, Comment.class, HighScore.class, Game.class)
                    .addAsResource("META-INF/persistence.xml")
                    .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }


    
    @EJB
    private UserAccountDAO userAccountDAO;
    
    @Inject
    private UserTransaction tx;
    

    @Test
    public void createAndRemoveUsers() throws Exception{
        //starts transaction
        tx.begin();
        
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        UserAccount user2 = new UserAccount("mail2", "name2", "USER", "password2");
        
        Assert.assertEquals(0, userAccountDAO.findUsersWithUsermail("mail1").toArray().length);
        Assert.assertEquals(0, userAccountDAO.findUsersWithUsermail("mail2").toArray().length);
        
        userAccountDAO.create(user1);
        userAccountDAO.getEntityManager().flush();
        
        Assert.assertEquals(1, userAccountDAO.findUsersWithUsermail("mail1").toArray().length);
        Assert.assertEquals(0, userAccountDAO.findUsersWithUsermail("mail2").toArray().length);
        
        userAccountDAO.create(user2);
        userAccountDAO.getEntityManager().flush();
        
        Assert.assertEquals(1, userAccountDAO.findUsersWithUsermail("mail1").toArray().length);
        Assert.assertEquals(1, userAccountDAO.findUsersWithUsermail("mail2").toArray().length);
        
        userAccountDAO.getEntityManager().refresh(user1);
        userAccountDAO.getEntityManager().refresh(user2);
        
        userAccountDAO.remove(user1);
        userAccountDAO.remove(user2);
        
        //end transaction
        tx.commit();
    }
    
    @Test
    public void findUsersByMail() throws Exception{
        //starts transaction
        tx.begin();
        
        UserAccount user1 = new UserAccount("mail1", "name1", "USER", "password1");
        UserAccount user2 = new UserAccount("mail2", "name2", "USER", "password2");
        
        userAccountDAO.create(user1);
        userAccountDAO.create(user2);
        userAccountDAO.getEntityManager().flush();
        
        UserAccount[] expected1 = {user1};
        Assert.assertArrayEquals(expected1, userAccountDAO.findUsersWithUsermail("mail1").toArray());
        UserAccount[] expected2 = {user2};
        Assert.assertArrayEquals(expected2, userAccountDAO.findUsersWithUsermail("mail2").toArray());
        
        userAccountDAO.getEntityManager().refresh(user1);
        userAccountDAO.getEntityManager().refresh(user2);
        
        userAccountDAO.remove(user1);
        userAccountDAO.remove(user2);
        
        //end transaction
        tx.commit();
    }
    
    @Test
    public void isUsernameUsed() throws Exception{
        //starts transaction
        tx.begin();
        
        UserAccount user5 = new UserAccount("mail5", "name5", "USER", "password5");

          
        userAccountDAO.create(user5);
        userAccountDAO.getEntityManager().flush();
        
        
        Assert.assertTrue(userAccountDAO.isUserNameUsed("name5"));
        Assert.assertFalse(userAccountDAO.isUserNameUsed("testName"));
        
        userAccountDAO.getEntityManager().refresh(user5);
        userAccountDAO.remove(user5);
        
        //end transaction
        tx.commit();
    }
    
    @Test
    public void userExcist() throws Exception{
        //starts transaction
        tx.begin();
        
        UserAccount user5 = new UserAccount("mail5", "name5", "USER", "password5");

          
        userAccountDAO.create(user5);
        userAccountDAO.getEntityManager().flush();
        
        
        Assert.assertTrue(userAccountDAO.exists("mail5"));
        Assert.assertFalse(userAccountDAO.exists("mail4"));
        
        userAccountDAO.getEntityManager().refresh(user5);
        userAccountDAO.remove(user5);
        
        //end transaction
        tx.commit();
    }
    
}
