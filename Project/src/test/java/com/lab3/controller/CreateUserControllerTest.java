/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.controller;

import com.lab3.model.dao.CommentDAO;
import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.HighScoreDAO;
import com.lab3.model.dao.RatingDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.UserAccount;
import com.lab3.resource.ContextMocker;
import com.lab3.view.CreateUserView;
import com.lab3.view.CurrentGameView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
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
import org.omnifaces.util.Messages;

/**
 * Tests for CreateUserController
 * @author Joachim Antfolk
 */
@RunWith(Arquillian.class)
public class CreateUserControllerTest {
    
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(UserAccountDAO.class, UserAccount.class, Rating.class, Comment.class, HighScore.class, Game.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    UserTransaction tx;  //transaction needed for testing
    
    @EJB
    private UserAccountDAO userAccountDAO;
    
    private CreateUserController createUserController;
    
    private CreateUserView createUserView;
    
    private UserAccount account;
    
    @Inject
    Pbkdf2PasswordHash passwordHasher;
    
    private FacesContext facesContext;
    
    /**
     * Init before tests
     * @throws Exception 
     */
    @Before
    public void setup() throws Exception{  
        facesContext = ContextMocker.mockServletRequest();
        
        createUserController = new CreateUserController();
        createUserView = new CreateUserView();
     
        createUserController.setCreateUserView(createUserView);
        createUserController.setFacesContext(facesContext);
        createUserController.setPasswordHasher(passwordHasher);
        createUserController.setUserAccountDAO(userAccountDAO);
        
        String mail = "mail@server.com";
        String name = "name"; 
        account = new UserAccount(mail.toLowerCase(), name.toLowerCase(), "USER", "pa55w0rd");
        
        tx.begin();
    }
    
    /**
     * Tests that setup happens correctly
     */
    @Test
    public void setupTest(){
        userAccountDAO.create(account);
        userAccountDAO.getEntityManager().flush();
        
        Assert.assertEquals(createUserView, createUserController.getCreateUserView());
        Assert.assertEquals(facesContext, createUserController.getFacesContext());
        Assert.assertEquals(passwordHasher, createUserController.getPasswordHasher());
        Assert.assertEquals(userAccountDAO, createUserController.getUserAccountDAO());
    }
    
    @Test
    public void createAccountSuccessTest() throws Exception {
        createUserView.setMail(account.getMail());
        createUserView.setUserName(account.getName());
        createUserView.setPassword(account.getPassword());
        createUserView.setConfirmPassword(account.getPassword());
        
        Assert.assertTrue(createUserController.create());
        
        List<UserAccount> list = (List<UserAccount>)userAccountDAO.findUsersWithUsermail(account.getMail());
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(account.getMail(), list.get(0).getMail());
        
        account = list.get(0);
        userAccountDAO.getEntityManager().flush(); 
    }
    
    @Test
    public void createAccountFailDiffPassTest(){
        userAccountDAO.create(account);
        userAccountDAO.getEntityManager().flush();
        
        createUserView.setMail(account.getMail());
        createUserView.setUserName(account.getName());
        createUserView.setPassword(account.getPassword());
        createUserView.setConfirmPassword("wrong");
        
        Assert.assertFalse(createUserController.create());
    }
    
    @Test
    public void createAccountFailNameTakenTest() throws Exception{
        userAccountDAO.create(account);
        userAccountDAO.getEntityManager().flush();
        Assert.assertFalse(userAccountDAO.findUsersWithUser(account).isEmpty());
        
        createUserView.setMail("jon@mail.com");
        createUserView.setUserName(account.getName());
        createUserView.setPassword("pass");
        createUserView.setConfirmPassword("pass");
        Assert.assertFalse(createUserController.create());
    }
    
    @Test
    public void createAccountFailMailTakenTest() {
        userAccountDAO.create(account);
        userAccountDAO.getEntityManager().flush();
        Assert.assertFalse(userAccountDAO.findUsersWithUser(account).isEmpty());
        
        createUserView.setMail(account.getMail());
        createUserView.setUserName("benny");
        createUserView.setPassword("pass");
        createUserView.setConfirmPassword("pass");

        Assert.assertFalse(createUserController.create());
    }
      
    /**
     * TearDown after tests
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{ 
        userAccountDAO.getEntityManager().refresh(account);
        userAccountDAO.remove(account); //Remove account 
        tx.commit();
        
        facesContext.release();
    }
}
