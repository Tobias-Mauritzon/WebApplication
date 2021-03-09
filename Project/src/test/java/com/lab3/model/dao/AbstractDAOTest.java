package com.lab3.model.dao;

import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.UserAccount;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 * Tests the AbstractDAO Because the AbstractDAO is abstract the testing is
 * going to be done through UserAccountDAO which exctends AbstractDAO
 *
 * @author Matteus
 */
@RunWith(Arquillian.class)
public class AbstractDAOTest {

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
    public void setUp() throws Exception {
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
     * Test for the abstract methods create And Remove method.
     */
    @Test
    public void createAndRemove() {

        UserAccount user3 = new UserAccount("mail3", "name3", "USER", "password3");
        userAccountDAO.create(user3);
        userAccountDAO.getEntityManager().flush();

        Assert.assertEquals(user3, userAccountDAO.find(user3.getMail()));

        userAccountDAO.getEntityManager().refresh(user3);
        userAccountDAO.remove(user3);
    }

    /**
     * Test for the abstract method count method.
     */
    @Test
    public void count() {

        Assert.assertTrue(userAccountDAO.count() == 2);
    }

    /**
     * Test for the abstract method findAll method.
     */
    @Test
    public void findAll() {
        Assert.assertTrue(userAccountDAO.findAll().size() == 2);
    }

    /**
     * Test for the abstract method find method.
     */
    @Test
    public void find() {
        Assert.assertEquals(user1, userAccountDAO.find(user1.getMail()));
    }

    /**
     * Test for the abstract method exists method.
     */
    @Test
    public void exists() {

        Assert.assertTrue(userAccountDAO.exists("mail1"));
        Assert.assertFalse(userAccountDAO.exists("Fail Mail"));
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
