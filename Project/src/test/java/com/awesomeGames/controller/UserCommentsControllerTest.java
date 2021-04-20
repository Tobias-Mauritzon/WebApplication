package com.awesomeGames.controller;

import com.awesomeGames.model.dao.CommentDAO;
import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
import com.awesomeGames.model.entity.key.CommentPK;
import com.awesomeGames.resource.ContextMocker;
import com.awesomeGames.view.UserCommentsView;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
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
import static org.mockito.Mockito.when;

/**
 * Test class for UserCommentsController
 * @author Joachim Antfolk
 */
@RunWith(Arquillian.class)
public class UserCommentsControllerTest {
   
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(CommentDAO.class, UserAccountDAO.class, GameDAO.class, 
                        Game.class, UserAccount.class, Rating.class, Comment.class, HighScore.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Inject
    UserTransaction tx;  //transaction needed for testing
    
    @EJB
    private CommentDAO commentDAO;
    
    @EJB
    private UserAccountDAO userAccountDAO;
    
    @EJB
    private GameDAO gameDAO;
    
    private String userName;
    
    private UserAccount user;
    
    private Game game;
    
    private Comment comment1;
    
    private Comment comment2;
    
    private UserCommentsView userCommentsView;
    
    private UserCommentsController userCommentsController;
    
    private FacesContext facesContext; 
    
     /**
     * Init before tests
     *
     * @throws Exception
     */
    @Before
    public void setup() throws Exception { 
        userName = "user";
        
        facesContext = ContextMocker.mockServletRequest();
        when(facesContext.getExternalContext().getUserPrincipal().getName()).thenReturn(userName);
        
        userCommentsView = new UserCommentsView();
        userCommentsController = new UserCommentsController();
        
        userCommentsController.setFacesContext(facesContext);
        userCommentsController.setCommentDAO(commentDAO);
        userCommentsController.setUserAccountDAO(userAccountDAO);
        userCommentsController.setUserCommentsView(userCommentsView);
        userCommentsController.setUtx(tx);
        
        tx.begin();
        user = new UserAccount("user@mail.com", userName, "USER", "password1");       
        game = gameDAO.createGame("Game", "Author1", "Game Description", "gamepath.js", "imagepath.png");
        comment1 = commentDAO.createComment(game, user, "Comment1");
        comment2 = commentDAO.createComment(game, user, "Comment2");

        userAccountDAO.create(user);

        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
        commentDAO.getEntityManager().flush();
    }
    
    /**
     * Test for init of UserCommentsController
     */
    @Test
    public void userCommentsControllerInitTest(){
        Assert.assertEquals(facesContext, userCommentsController.getFacesContext());
        Assert.assertEquals(commentDAO, userCommentsController.getCommentDAO());
        Assert.assertEquals(userAccountDAO, userCommentsController.getUserAccountDAO());
        Assert.assertEquals(userCommentsView, userCommentsController.getUserCommentsView());
        Assert.assertEquals(tx, userCommentsController.getUtx());
    
        commentDAO.getEntityManager().refresh(comment1);
        commentDAO.getEntityManager().refresh(comment2);
        gameDAO.getEntityManager().refresh(game);
        userAccountDAO.getEntityManager().refresh(user);
    }
    
    /**
     * Test for findComments success expected
     */
    @Test
    public void findCommentsSuccessTest(){
        userCommentsController.findComments();
        List<Comment> list = userCommentsView.getCommentList();
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.contains(comment1));
        Assert.assertTrue(list.contains(comment2));
    
        commentDAO.getEntityManager().refresh(comment1);
        commentDAO.getEntityManager().refresh(comment2);
        gameDAO.getEntityManager().refresh(game);
        userAccountDAO.getEntityManager().refresh(user);
        
    }
    
    /**
     * Test for findComments failure expected
     */
    @Test
    public void findCommentsFailureTest(){
        Assert.assertFalse(commentDAO.findCommentsWithUserDESC(user).isEmpty());
        
        commentDAO.getEntityManager().refresh(comment1);
        commentDAO.getEntityManager().refresh(comment2);
        commentDAO.remove(comment1);
        commentDAO.remove(comment2);
        
        userCommentsController.findComments();
        List<Comment> list = userCommentsView.getCommentList();
        Assert.assertTrue(list.isEmpty());
        
        /*Restore Data*/
        comment1 = commentDAO.createComment(game, user, "Comment1");
        comment2 = commentDAO.createComment(game, user, "Comment2");
        commentDAO.getEntityManager().flush();
    
        commentDAO.getEntityManager().refresh(comment1);
        commentDAO.getEntityManager().refresh(comment2);
        gameDAO.getEntityManager().refresh(game);
        userAccountDAO.getEntityManager().refresh(user);
    }
    
    /**
     * Test for deleteComment success expected
     */
    @Test
    public void deleteCommentSuccessTest() throws Exception {
        tx.commit();
        
        List<Comment> list = commentDAO.findCommentsWithUserDESC(user);
        Assert.assertTrue(list.contains(comment1));

        userCommentsController.deleteComment(comment1);
        
        list = commentDAO.findCommentsWithUserDESC(user);
        Assert.assertFalse(list.contains(comment1));
        Assert.assertEquals("Comment deleted!", facesContext.getMessageList().get(0).getSummary());
        
        tx.begin();
        comment1 = commentDAO.createComment(game, user, "Comment1");
        commentDAO.getEntityManager().flush(); 
        commentDAO.getEntityManager().refresh(comment1);
        
        comment2 = commentDAO.find(new CommentPK(comment2.getCommentId(), comment2.getUserAccount().getMail(), comment2.getGame().getName()));
        game = gameDAO.find("Game");
        user = userAccountDAO.find("user@mail.com");
    }
    
    /**
    * Test for deleteComment Failure expected
    */
    @Test
    public void deleteCommentFailureTest() throws Exception {
        commentDAO.getEntityManager().refresh(comment1);
        commentDAO.remove(comment1);
        
        List<Comment> list = commentDAO.findCommentsWithUserDESC(user);
        Assert.assertFalse(list.contains(comment1));
        
        userCommentsController.deleteComment(comment1);
        
        Assert.assertEquals("Can't delete comment!", facesContext.getMessageList().get(0).getSummary());
        
        /*Restore Data*/
        comment1 = commentDAO.createComment(game, user, "Comment1");
        commentDAO.getEntityManager().flush();
        
        commentDAO.getEntityManager().refresh(comment1);
        commentDAO.getEntityManager().refresh(comment2);
        gameDAO.getEntityManager().refresh(game);
        userAccountDAO.getEntityManager().refresh(user);
    }
    
    /**
     * Test for deleteAllUserComments success
     */
    @Test
    public void userCommentsControllerDeleteAllSuccessTest(){
        Assert.assertFalse(commentDAO.findCommentsWithUserDESC(user).isEmpty());
        userCommentsController.deleteAllUserComments();
        Assert.assertTrue(commentDAO.findCommentsWithUserDESC(user).isEmpty());
        Assert.assertEquals("Comments Deleted!", facesContext.getMessageList().get(0).getSummary());
        
        /*Restore Data*/
        comment1 = commentDAO.createComment(game, user, "Comment1");
        comment2 = commentDAO.createComment(game, user, "Comment2");
        commentDAO.getEntityManager().flush();
    
        commentDAO.getEntityManager().refresh(comment1);
        commentDAO.getEntityManager().refresh(comment2);
        gameDAO.getEntityManager().refresh(game);
        userAccountDAO.getEntityManager().refresh(user);
    }
    
    /**
     * Test for deleteAllUserComments failure
     */
    @Test
    public void userCommentsControllerDeleteAllFailureTest(){
        Assert.assertFalse(commentDAO.findCommentsWithUserDESC(user).isEmpty());
        
        commentDAO.getEntityManager().refresh(comment1);
        commentDAO.getEntityManager().refresh(comment2);
        commentDAO.remove(comment1);
        commentDAO.remove(comment2);
        
        Assert.assertTrue(commentDAO.findCommentsWithUserDESC(user).isEmpty());
        
        userCommentsController.deleteAllUserComments();
        Assert.assertTrue(commentDAO.findCommentsWithUserDESC(user).isEmpty());
        Assert.assertEquals("Can't delete comments!", facesContext.getMessageList().get(0).getSummary());
        
        /*Restore Data*/
        comment1 = commentDAO.createComment(game, user, "Comment1");
        comment2 = commentDAO.createComment(game, user, "Comment2");
        commentDAO.getEntityManager().flush();

        commentDAO.getEntityManager().refresh(comment1);
        commentDAO.getEntityManager().refresh(comment2);
        gameDAO.getEntityManager().refresh(game);
        userAccountDAO.getEntityManager().refresh(user);
    }
    
    /**
    * TearDown after tests
    *
    * @throws Exception
    */
    @After
    public void tearDown() throws Exception {
        commentDAO.remove(comment1);
        commentDAO.remove(comment2);
        gameDAO.remove(game);
        userAccountDAO.remove(user);
        tx.commit();

        facesContext.release();
    }
}
