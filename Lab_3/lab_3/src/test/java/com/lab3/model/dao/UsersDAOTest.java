package com.lab3.model.dao;


import com.lab3.model.dao.UsersDAO;
import com.lab3.model.entity.Users;
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

@RunWith(Arquillian.class)
public class UsersDAOTest {
    @Deployment
    public static WebArchive createDeployment() {
            return ShrinkWrap.create(WebArchive.class)
                    .addClasses(UsersDAO.class, Users.class)
                    .addAsResource("META-INF/persistence.xml")
                    .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }


    
    @EJB
    private UsersDAO usersDAO;
    
    @Inject
    private UserTransaction tx;
    

    @Test
    public void createAndRemoveUsers() throws Exception{
        //starts transaction
        tx.begin();
        
        Users user1 = new Users("mail1", "name1", "password1");
        Users user2 = new Users("mail2", "name2", "password2");
        
        Assert.assertEquals(0, usersDAO.findUsersWithUsermail("mail1").toArray().length);
        Assert.assertEquals(0, usersDAO.findUsersWithUsermail("mail2").toArray().length);
        
        usersDAO.create(user1);
        usersDAO.getEntityManager().flush();
        
        Assert.assertEquals(1, usersDAO.findUsersWithUsermail("mail1").toArray().length);
        Assert.assertEquals(0, usersDAO.findUsersWithUsermail("mail2").toArray().length);
        
        usersDAO.create(user2);
        usersDAO.getEntityManager().flush();
        
        Assert.assertEquals(1, usersDAO.findUsersWithUsermail("mail1").toArray().length);
        Assert.assertEquals(1, usersDAO.findUsersWithUsermail("mail2").toArray().length);
        
        usersDAO.getEntityManager().refresh(user1);
        usersDAO.getEntityManager().refresh(user2);
        
        usersDAO.remove(user1);
        usersDAO.remove(user2);
        
        //end transaction
        tx.commit();
    }
    
    @Test
    public void findUsersByMail() throws Exception{
        //starts transaction
        tx.begin();
        
        Users user1 = new Users("mail1", "name1", "password1");
        Users user2 = new Users("mail2", "name2", "password2");
        
        usersDAO.create(user1);
        usersDAO.create(user2);
        usersDAO.getEntityManager().flush();
        
        Users[] expected1 = {user1};
        Assert.assertArrayEquals(expected1, usersDAO.findUsersWithUsermail("mail1").toArray());
        Users[] expected2 = {user2};
        Assert.assertArrayEquals(expected2, usersDAO.findUsersWithUsermail("mail2").toArray());
        
        usersDAO.getEntityManager().refresh(user1);
        usersDAO.getEntityManager().refresh(user2);
        
        usersDAO.remove(user1);
        usersDAO.remove(user2);
        
        //end transaction
        tx.commit();
    }
    
    @Test
    public void isUsernameUsed() throws Exception{
        //starts transaction
        tx.begin();
        
        Users user5 = new Users("mail5", "name5", "password5");

          
        usersDAO.create(user5);
        usersDAO.getEntityManager().flush();
        
        
        Assert.assertTrue(usersDAO.isUserNameUsed("name5"));
        Assert.assertFalse(usersDAO.isUserNameUsed("testName"));
        
        usersDAO.getEntityManager().refresh(user5);
        usersDAO.remove(user5);
        
        //end transaction
        tx.commit();
    }
    
    @Test
    public void userExcist() throws Exception{
        //starts transaction
        tx.begin();
        
        Users user5 = new Users("mail5", "name5", "password5");

          
        usersDAO.create(user5);
        usersDAO.getEntityManager().flush();
        
        
        Assert.assertTrue(usersDAO.excist("mail5"));
        Assert.assertFalse(usersDAO.excist("mail4"));
        
        usersDAO.getEntityManager().refresh(user5);
        usersDAO.remove(user5);
        
        //end transaction
        tx.commit();
    }
    
}
