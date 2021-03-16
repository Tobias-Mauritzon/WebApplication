package com.awesomeGames.controller;

import com.awesomeGames.controller.CommentController;
import com.awesomeGames.model.dao.CommentDAO;
import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.HighScoreDAO;
import com.awesomeGames.model.dao.RatingDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
import com.awesomeGames.resource.ContextMocker;
import com.awesomeGames.view.CommentView;
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
import org.mockito.Mockito;

/**
 *
 * @author David
 * @author William
 */
@RunWith(Arquillian.class)
public class CommentControllerTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class,
                        CommentDAO.class, Comment.class, HighScoreDAO.class, HighScore.class, RatingDAO.class, Rating.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    private CommentController commentController;
    private CommentView commentView;
    private FacesContext facesContext;
    private UserTransaction utx;

    private Game game;
    private UserAccount user;

    @EJB
    private CommentDAO commentDAO;

    @EJB
    private UserAccountDAO userAccountDAO;

    @EJB
    private GameDAO gameDAO;

    @Inject
    private UserTransaction tx;

    @Before
    public void init() throws Exception {
        commentController = new CommentController();
        commentView = new CommentView();
        commentController.setCommentDAO(commentDAO);
        commentController.setGameDAO(gameDAO);
        commentController.setUserAccountDAO(userAccountDAO);
        commentController.setCommentView(commentView);
        commentView.setDescending(true);

        facesContext = ContextMocker.mockServletRequest();
        commentController.setFacesContext(facesContext);
        utx = Mockito.mock(UserTransaction.class);
        commentController.setUtx(utx);

        tx.begin();
        user = new UserAccount("mail1", "name1", "USER", "password1");
        userAccountDAO.create(user);
        game = gameDAO.createGame("Game", "author", "description", "javaScriptPath", "imagePath");
        userAccountDAO.getEntityManager().flush();
        gameDAO.getEntityManager().flush();
    }

    @Test
    public void getDAOs() throws Exception {
        Assert.assertEquals(commentDAO, commentController.getCommentDAO());
        Assert.assertEquals(userAccountDAO, commentController.getUserAccountDAO());
        Assert.assertEquals(gameDAO, commentController.getGameDAO());
    }

    @Test
    public void getViews() throws Exception {
        Assert.assertEquals(commentView, commentController.getCommentView());
    }

    @Test
    public void getSetFacesContextTest() {
        commentController.setFacesContext(facesContext);
        Assert.assertEquals(facesContext, commentController.getFacesContext());
    }

    @Test
    public void getSetUtxTest() {
        commentController.setUtx(utx);
        Assert.assertEquals(utx, commentController.getUtx());
    }

    /**
     * Tests 'create' with all conditions for a successful comment creation
     * fullfilled
     *
     * @throws Exception
     */
    @Test
    public void createTestAllConditionsFullfilled() throws Exception {
        commentView.setGameName(game.getName());
        commentView.setText("createTestAllConditionsFullfilled");

        Assert.assertTrue(commentDAO.findCommentsWithGameASC(game).isEmpty());

        Assert.assertTrue(commentController.create(user.getName()));

        Assert.assertEquals("createTestAllConditionsFullfilled", commentDAO.findCommentsWithGameASC(game).get(0).getText());
    }

    /**
     * Tests 'create' with invalid user name, making an unsuccessful create
     *
     * @throws Exception
     */
    @Test
    public void createTestInvalidUserName() throws Exception {
        commentView.setGameName(game.getName());
        commentView.setText("createTestInvalidUserName");

        Assert.assertTrue(commentDAO.findCommentsWithGameASC(game).isEmpty());

        Assert.assertFalse(commentController.create("noneExistingUser"));

        Assert.assertTrue(commentDAO.findCommentsWithGameASC(game).isEmpty());
    }

    /**
     * Tests 'create' with invalid game name, making an unsuccessful create
     *
     * @throws Exception
     */
    @Test
    public void createTestInvalidGameName() throws Exception {
        commentView.setGameName("noneExistingGame");
        commentView.setText("createTestInvalidGameName");

        Assert.assertTrue(commentDAO.findCommentsWithGameASC(game).isEmpty());

        Assert.assertFalse(commentController.create(user.getName()));

        Assert.assertTrue(commentDAO.findCommentsWithGameASC(game).isEmpty());
    }

    @Test
    public void findCommentsTestAscending() {
        commentView.setDescending(false);
        commentView.setGameName(game.getName());

        Comment older = commentDAO.createComment(game, user, "findCommentsTestAscendingOlder");
        Comment newer = commentDAO.createComment(game, user, "findCommentsTestAscendingNewer");
        Assert.assertEquals(null, commentView.getCommentList());

        commentController.findComments();
        Assert.assertEquals(newer, commentView.getCommentList().get(0));
        Assert.assertEquals(older, commentView.getCommentList().get(1));
    }

    @Test
    public void findCommentsTestDescending() {
        commentView.setDescending(true);
        commentView.setGameName(game.getName());

        Comment older = commentDAO.createComment(game, user, "findCommentsTestAscendingOlder");
        Comment newer = commentDAO.createComment(game, user, "findCommentsTestAscendingNewer");
        Assert.assertEquals(null, commentView.getCommentList());

        commentController.findComments();
        Assert.assertEquals(older, commentView.getCommentList().get(0));
        Assert.assertEquals(newer, commentView.getCommentList().get(1));
    }

    @Test
    public void findCommentsTestNonExistingGame() {
        commentView.setGameName("nonExistingGame");

        commentDAO.createComment(game, user, "findCommentsTestInvalidGameName");
        Assert.assertEquals(null, commentView.getCommentList());

        commentController.findComments();
        Assert.assertTrue(commentView.getCommentList().isEmpty());
    }

    @Test
    public void flipDescendingTest() {
        commentView.setDescending(true);
        Assert.assertTrue(commentView.getDescending());

        commentController.flipDescending();
        Assert.assertFalse(commentView.getDescending());

        commentController.flipDescending();
        Assert.assertTrue(commentView.getDescending());
    }

    @Test
    public void findGameTest() {
        commentView.setGameName(game.getName());
        Assert.assertEquals(null, commentView.getGame());

        commentController.findGame();
        Assert.assertEquals(game, commentView.getGame());
    }

    @Test
    public void findGameTestInvalidGameName() {
        commentView.setGameName("noneExistingGame");
        Assert.assertEquals(null, commentView.getGame());

        commentController.findGame();
        Assert.assertEquals(null, commentView.getGame());
    }

    @Test
    public void deleteCommentTest() {
        Comment c = commentDAO.createComment(game, user, "deleteCommentTest");

        Assert.assertEquals(c.getText(), commentDAO.findCommentsWithGameASC(game).get(0).getText());

        commentController.deleteComment(c);

        Assert.assertTrue(commentDAO.findCommentsWithGameASC(game).isEmpty());
    }

    @Test
    public void deleteCommentTestInvalidUserTransaction() {
        commentController.setUtx(null);
        Comment c = commentDAO.createComment(game, user, "deleteCommentTestInvalidUserTransaction");

        Assert.assertEquals(c.getText(), commentDAO.findCommentsWithGameASC(game).get(0).getText());

        commentController.deleteComment(c);

        Assert.assertEquals(c.getText(), commentDAO.findCommentsWithGameASC(game).get(0).getText());
    }

    @After
    public void tearDown() throws Exception {
        gameDAO.getEntityManager().refresh(game);
        userAccountDAO.getEntityManager().refresh(user);

        gameDAO.remove(game);
        userAccountDAO.remove(user);
        userAccountDAO.remove(user);
        tx.commit();

        facesContext.release();
    }

}
