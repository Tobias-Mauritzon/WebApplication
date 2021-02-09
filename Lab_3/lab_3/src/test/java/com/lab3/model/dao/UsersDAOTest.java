package com.lab3.model.dao;


import com.lab3.model.dao.UsersDAO;
import com.lab3.model.entity.Users;
import javax.ejb.EJB;
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
public class UsersDAOTest {
    @Deployment
    public static WebArchive createDeployment() {
            return ShrinkWrap.create(WebArchive.class)
                    .addClasses(UsersDAO.class, Users.class)
                    .addAsResource("META-INF/persistence.xml")
                    .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    Users user1;
    Users user2;
    
    @EJB
    private UsersDAO usersDAO;

    @Before
    public void init() {
        user1 = new Users("mail1", "name1", "password1");
        user2 = new Users("mail2", "name2", "password2");
    }

    @Test
    public void createUsers() {
        Assert.assertEquals(0, usersDAO.findUsersMatchingName("mail1").toArray().length);
        Assert.assertEquals(0, usersDAO.findUsersMatchingName("mail2").toArray().length);
        usersDAO.create(user1);
        Assert.assertEquals(1, usersDAO.findUsersMatchingName("mail1").toArray().length);
        Assert.assertEquals(0, usersDAO.findUsersMatchingName("mail2").toArray().length);
        usersDAO.create(user2);
        Assert.assertEquals(1, usersDAO.findUsersMatchingName("mail1").toArray().length);
        Assert.assertEquals(1, usersDAO.findUsersMatchingName("mail2").toArray().length);
    }
    
    @Test
    public void findUsers(){
        usersDAO.create(user1);
        usersDAO.create(user2);
        
        Users[] expected1 = {user1};
        Assert.assertArrayEquals(expected1, usersDAO.findUsersMatchingName("mail1").toArray());
        Users[] expected2 = {user2};
        Assert.assertArrayEquals(expected2, usersDAO.findUsersMatchingName("mail2").toArray());
    }
    
    @Test
    public void removeUsers(){
        usersDAO.create(user1);
        usersDAO.create(user2);
        
        usersDAO.remove(user1);
        Assert.assertEquals(0, usersDAO.findUsersMatchingName("mail1").toArray().length);
        Assert.assertEquals(1, usersDAO.findUsersMatchingName("mail2").toArray().length);
        usersDAO.remove(user2);
        Assert.assertEquals(0, usersDAO.findUsersMatchingName("mail1").toArray().length);
        Assert.assertEquals(0, usersDAO.findUsersMatchingName("mail2").toArray().length);
    }
    
    @After
    public void end(){
        usersDAO.remove(user1);
        usersDAO.remove(user2);
    }
}
