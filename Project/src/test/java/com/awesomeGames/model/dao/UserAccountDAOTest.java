package com.awesomeGames.model.dao;

import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
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
 * Test class for the UserAccount DAO
 *
 * @author Matteus
 * @author Joachim Antfolk
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

    UserAccount user1;
    UserAccount user2;

    /**
     * Init for tests
     *
     * @throws Exception if UserTransaction logging fails
     */
    @Before
    public void init() throws Exception {
        //starts transaction
        tx.begin();

        //create entities
        user1 = new UserAccount("mail1", "name1", "USER", "password1");
        user2 = new UserAccount("mail2", "name2", "USER", "password2");

        userAccountDAO.create(user1);
        userAccountDAO.create(user2);

        //flush after create
        userAccountDAO.getEntityManager().flush();
    }

    /**
     * Test for the method findUsersWithUser.
     */
    @Test
    public void findUsersWithUser() {

        Assert.assertEquals(user1, userAccountDAO.findUsersWithUser(user1).get(0));
        Assert.assertEquals(user2, userAccountDAO.findUsersWithUser(user2).get(0));
    }

    /**
     * Test for the method findUserWithName.
     */
    @Test
    public void findUserWithName() {

        Assert.assertEquals(user1, userAccountDAO.findUserWithName(user1.getName()));
        Assert.assertEquals(user2, userAccountDAO.findUserWithName(user2.getName()));
    }

    /**
     * Test for the method findUserWithName when there is no user.
     */
    @Test
    public void findUserWithNameNoUser() {

        Assert.assertEquals(null, userAccountDAO.findUserWithName("test"));
    }

    /**
     * Test for the method isUserNameUsed.
     */
    @Test
    public void isUserNameUsed() {
        Assert.assertTrue(userAccountDAO.isUserNameUsed(user1.getName()));
        Assert.assertFalse(userAccountDAO.isUserNameUsed("test isUserNameUsed"));
    }
    
    
    
    
    /**
     * Test for the method findPasswordForUserWithUserName.
     */
    @Test
    public void findPasswordForUserWithUserNameTest() {
        Assert.assertEquals("password1", userAccountDAO.findPasswordForUserWithUserName(user1.getName()));
        Assert.assertNull(userAccountDAO.findPasswordForUserWithUserName("test userNameUnused"));
    }
    
    /**
     * Test for the method updatePasswordForUser.
     */
    @Test
    public void updatePasswordForUserTest() {
        Assert.assertTrue(userAccountDAO.updatePasswordForUser(user1.getName(), "newPassword1"));
        Assert.assertEquals("newPassword1", userAccountDAO.findPasswordForUserWithUserName(user1.getName()));
        Assert.assertFalse(userAccountDAO.updatePasswordForUser("test userNameUnused", "newPassword1"));
    }
    
    
    

    /**
     * TearDown for tests
     *
     * @throws Exception Throws if UserTransaction "commit" fails
     */
    @After
    public void tearDown() throws Exception {
        //refresh before delete
        userAccountDAO.getEntityManager().refresh(user1);
        userAccountDAO.getEntityManager().refresh(user2);

        userAccountDAO.remove(user1);
        userAccountDAO.remove(user2);

        //end transaction
        tx.commit();
    }

}
