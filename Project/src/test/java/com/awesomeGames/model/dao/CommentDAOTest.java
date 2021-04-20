package com.awesomeGames.model.dao;

import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.CommentDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
import java.util.List;
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
 * Test class for the Comment DAO
 *
 * @author Matteus
 * @author Joachim Antfolk
 */
@RunWith(Arquillian.class)
public class CommentDAOTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(CommentDAO.class, Comment.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class,
                        Rating.class, HighScore.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private UserTransaction tx;  //transaction needed for testing

    @EJB
    private CommentDAO commentDAO;

    @EJB
    private UserAccountDAO userAccountDAO;

    @EJB
    private GameDAO gameDAO;

    UserAccount user5;
    Game game5;
    Comment comment1;
    Comment comment2;

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
        user5 = new UserAccount("mail5", "name5", "USER", "password5");
        game5 = gameDAO.createGame("Game5", "author", "description", "javaScriptPath", "imagePath");
        comment1 = commentDAO.createComment(game5, user5, "commentText1");
        comment2 = commentDAO.createComment(game5, user5, "commentText2");

        userAccountDAO.create(user5);

        //flush after create
        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        commentDAO.getEntityManager().flush();
    }

    /**
     * Testing for the method createComment in init.
     */
    @Test
    public void createComment() throws Exception {
        Assert.assertTrue(commentDAO.findAll().size() == 2);
    }

    /**
     * Test for the method findCommentsWithUserASC.
     */
    @Test
    public void findCommentsWithUserASC() throws Exception {

        List<Comment> list = commentDAO.findCommentsWithUserASC(user5);
        Assert.assertTrue(list.get(0).equals(comment1));
        Assert.assertTrue(list.get(1).equals(comment2));
    }

    /**
     * Test for the method findCommentsWithUserDESC.
     */
    @Test
    public void findCommentsWithUserDESC() throws Exception {

        List<Comment> list = commentDAO.findCommentsWithUserDESC(user5);
        Assert.assertTrue(list.get(0).equals(comment2));
        Assert.assertTrue(list.get(1).equals(comment1));
    }

    /**
     * Test for the method findCommentsWithGameASC.
     */
    @Test
    public void findCommentsWithGameASC() throws Exception {

        List<Comment> list = commentDAO.findCommentsWithGameASC(game5);
        Assert.assertTrue(list.get(0).equals(comment1));
        Assert.assertTrue(list.get(1).equals(comment2));
    }

    /**
     * Test for the method findCommentsWithGameDESC.
     */
    @Test
    public void findCommentsWithGameDESC() throws Exception {

        List<Comment> list = commentDAO.findCommentsWithGameDESC(game5);
        Assert.assertTrue(list.get(0).equals(comment2));
        Assert.assertTrue(list.get(1).equals(comment1));
    }

    /**
     * Test for the method findsGameNameWithMostComments.
     */
    @Test
    public void findsGameNameWithMostComments() throws Exception {

        String s = commentDAO.findsGameNameWithMostComments();
        Assert.assertTrue(s.equals("Game5"));
    }

    /**
     * Test for the method findsGameNameWithMostComments when there are no
     * comments.
     */
    @Test
    public void findsGameNameWithMostCommentsEmpty() throws Exception {

        tearDown();

        String s = commentDAO.findsGameNameWithMostComments();
        Assert.assertTrue(s == null);

        init();
    }

    
    
    /**
     * Test for the method deleteUserCommentsWithUserName.
     */
    @Test
    public void deleteUserCommentsWithUserNameTest(){
        List<Comment> list = commentDAO.findCommentsWithUserDESC(user5);
        Assert.assertFalse(list.isEmpty());
        
        boolean b = commentDAO.deleteUserCommentsWithUserName(user5.getName());
        Assert.assertTrue(b);
        
        list = commentDAO.findCommentsWithUserDESC(user5);
        Assert.assertTrue(list.isEmpty());
        
        b = commentDAO.deleteUserCommentsWithUserName(user5.getName());
        Assert.assertFalse(b);
        
        /*Restore data for tear down*/
        comment1 = commentDAO.createComment(game5, user5, "commentText1");
        comment2 = commentDAO.createComment(game5, user5, "commentText2");
        commentDAO.getEntityManager().flush();
        list = commentDAO.findCommentsWithUserDESC(user5);
        Assert.assertFalse(list.isEmpty());
    }
    
    
    
    /**
     * TearDown for tests
     *
     * @throws Exception Throws if UserTransaction "commit" fails
     */
    @After
    public void tearDown() throws Exception {
        //refresh before delete
        userAccountDAO.getEntityManager().refresh(user5);
        gameDAO.getEntityManager().refresh(game5);
        commentDAO.getEntityManager().refresh(comment1);
        commentDAO.getEntityManager().refresh(comment2);

        commentDAO.remove(comment1);
        commentDAO.remove(comment2);
        gameDAO.remove(game5);
        userAccountDAO.remove(user5);

        //ends the transaction
        tx.commit();
    }
}
