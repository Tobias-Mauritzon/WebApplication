package com.awesomeGames.security;

import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.HighScore;
import com.awesomeGames.model.entity.Rating;
import com.awesomeGames.model.entity.UserAccount;
import com.awesomeGames.resource.ContextMocker;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;

/**
 * Tests for PasswordChangeBacking
 * @author Joachim Antfolk
 */
@RunWith(Arquillian.class)
public class PasswordChangeBackingTest {
        
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(UserAccountDAO.class, UserAccount.class, Rating.class, Comment.class, HighScore.class, Game.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Inject
    UserTransaction tx;  //transaction needed for testing
    
    @Inject
    Pbkdf2PasswordHash passwordHasher;
    
    @EJB
    private UserAccountDAO userAccountDAO;  
    
    private String userName;
    
    private String currentPassword;
    
    private String newPassword;
    
    private String confirmPassword;
    
    private PasswordChangeBacking passwordChangeBacking;
    
    private FacesContext facesContext;   
    
    private UserAccount userAccount;
    
    /**
     * Init before tests
     *
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        userName = "user1";
        currentPassword = "password1";
        newPassword = "newPassword1";
        confirmPassword = "newPassword1";
        
        facesContext = ContextMocker.mockServletRequest();
        when(facesContext.getExternalContext().getUserPrincipal().getName()).thenReturn(userName);
        
        passwordChangeBacking = new PasswordChangeBacking();

        userAccount = new UserAccount("user1@mail.com", userName, "USER", passwordHasher.generate(currentPassword.toCharArray()));

        passwordChangeBacking.setCurrentPassword(currentPassword);
        passwordChangeBacking.setNewPassword(newPassword);
        passwordChangeBacking.setConfirmPassword(confirmPassword);
        passwordChangeBacking.setFacesContext(facesContext);
        passwordChangeBacking.setUserAccountDAO(userAccountDAO);
        passwordChangeBacking.setPasswordHasher(passwordHasher);
        
        tx.begin();
        userAccountDAO.create(userAccount);
        userAccountDAO.getEntityManager().flush();
    }
    
    /**
     * Test for init of PasswordChangeBacking 
     */
    @Test
    public void passwordChangeInitTest(){
        Assert.assertTrue(currentPassword == passwordChangeBacking.getCurrentPassword());
        Assert.assertTrue(newPassword == passwordChangeBacking.getNewPassword());
        Assert.assertTrue(confirmPassword == passwordChangeBacking.getConfirmPassword());
        Assert.assertTrue(facesContext == passwordChangeBacking.getFacesContext());
        Assert.assertTrue(userAccountDAO == passwordChangeBacking.getUserAccountDAO());
        Assert.assertTrue(passwordHasher == passwordChangeBacking.getPasswordHasher());   
    }
    
    /**
     * Test for successfully changed password 
     */
    @Test
    public void passwordChangeSuccessTest(){
        String pass = userAccountDAO.findPasswordForUserWithUserName(userName);
        Assert.assertTrue(passwordHasher.verify(currentPassword.toCharArray(), pass));
        
        passwordChangeBacking.submit();
        
        pass = userAccountDAO.findPasswordForUserWithUserName(userName);
        Assert.assertTrue(passwordHasher.verify(newPassword.toCharArray(), pass)); 
        
        List<FacesMessage> messages = facesContext.getMessageList();
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals("Password changed successfully!", messages.get(0).getSummary()); 
    }
    
    /**
     * Test for checking correct behaviour for changing password for user that does not exist
     */
    @Test
    public void passwordChangeFailUserNotExistingTest(){
        when(facesContext.getExternalContext().getUserPrincipal().getName()).thenReturn("DoesNotExist");

        passwordChangeBacking.submit();
        
        List<FacesMessage> messages = facesContext.getMessageList();
        Assert.assertEquals(1, messages.size());
        /*Checking this message since there isn't a special one for the user not existing*/
        Assert.assertEquals("Current password is invalid!", messages.get(0).getSummary()); 
    }
    
    /**
     * Test for checking correct behaviour when current password is incorrect
     */
    @Test
    public void passwordChangeFailWrongCrurrentPassTest(){
        passwordChangeBacking.setCurrentPassword("WrongPassword");
        passwordChangeBacking.submit();
        
        List<FacesMessage> messages = facesContext.getMessageList();
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals("Current password is invalid!", messages.get(0).getSummary()); 
    }
    
    /**
     * Test for checking correct behaviour when new and confirm passwords don't match
     */
    @Test
    public void passwordChangeFailDiffNewConfPassesTest(){
        passwordChangeBacking.setConfirmPassword("WrongPassword");
        passwordChangeBacking.submit();
        
        List<FacesMessage> messages = facesContext.getMessageList();
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals("New and confirm passwords must match!", messages.get(0).getSummary()); 
    }
    
    /**
    * TearDown after tests
    *
    * @throws Exception
    */
    @After
    public void tearDown() throws Exception {
        userAccountDAO.getEntityManager().refresh(userAccount);
        userAccountDAO.remove(userAccount); //Remove account 
        tx.commit();

        facesContext.release();
    }
}
