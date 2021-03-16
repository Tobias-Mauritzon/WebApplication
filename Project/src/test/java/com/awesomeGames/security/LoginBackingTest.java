package com.awesomeGames.security;

import com.awesomeGames.security.LoginBacking;
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
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

/**
 * Test class for login back end 
 * 
 * @author Joachim Antfolk
 */
@RunWith(Arquillian.class)
public class LoginBackingTest {

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

    String mail;

    String name;

    String password;

    LoginBacking loginBacking;

    FacesContext facesContext;

    SecurityContext securityContext;

    @EJB
    UserAccountDAO userAccountDAO;

    UserAccount userAccount;

    /**
     * Init before tests
     *
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        securityContext = Mockito.mock(SecurityContext.class);
        facesContext = ContextMocker.mockServletRequest();
        loginBacking = new LoginBacking();

        mail = "login@backing.com";
        name = "LoginBackingTest";
        password = passwordHasher.generate("Password".toCharArray());

        userAccount = new UserAccount(mail, name, "USER", password);

        loginBacking.setFacesContext(facesContext);
        loginBacking.setSecurityContext(securityContext);
        loginBacking.setName(name);
        loginBacking.setPassword(password);

        tx.begin();
        userAccountDAO.create(userAccount);
        userAccountDAO.getEntityManager().flush();
    }

    /**
     * Checks that LoginBacking has been initiated successfully
     */
    @Test
    public void setupTest() {
        Assert.assertEquals(facesContext, loginBacking.getFacesContext());
        Assert.assertEquals(securityContext, loginBacking.getSecurityContext());
        Assert.assertEquals(name, loginBacking.getName());
        Assert.assertEquals(password, loginBacking.getPassword());

    }

    /**
     * Checks that submit() acts as expected when continueAuthentication()
     * returns AuthenticationStatus.SUCCESS
     */
    @Test
    public void loginSuccessTest() {
        when(securityContext.authenticate(
                any(HttpServletRequest.class),
                any(HttpServletResponse.class),
                any(AuthenticationParameters.class)))
                .thenReturn(javax.security.enterprise.AuthenticationStatus.SUCCESS);

        loginBacking.setName(name);
        loginBacking.setPassword(password);

        Assertions.assertDoesNotThrow(() -> {
            loginBacking.submit();
        });

        List<FacesMessage> messages = facesContext.getMessageList();
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals("<html><div>Login successful</div><div>Welcome back " + name + "</div></html>", messages.get(0).getSummary());
    }

    /**
     * Checks that submit() acts as expected when continueAuthentication()
     * returns AuthenticationStatus.NOT_DONE
     */
    @Test
    public void loginNotDoneTest() {
        when(securityContext.authenticate(
                any(HttpServletRequest.class),
                any(HttpServletResponse.class),
                any(AuthenticationParameters.class)))
                .thenReturn(javax.security.enterprise.AuthenticationStatus.NOT_DONE);

        Assertions.assertDoesNotThrow(() -> {
            loginBacking.submit();
        });
    }

    /**
     * Checks that submit() acts as expected when continueAuthentication()
     * returns AuthenticationStatus.SEND_CONTINUE
     */
    @Test
    public void loginSendContinueTest() {
        when(securityContext.authenticate(
                any(HttpServletRequest.class),
                any(HttpServletResponse.class),
                any(AuthenticationParameters.class)))
                .thenReturn(javax.security.enterprise.AuthenticationStatus.SEND_CONTINUE);

        loginBacking.setName(name);
        loginBacking.setPassword(password);

        Assertions.assertDoesNotThrow(() -> {
            loginBacking.submit();
        });
    }

    /**
     * Checks that submit() acts as expected when continueAuthentication()
     * returns AuthenticationStatus.SEND_FAILURE
     */
    @Test
    public void loginSendFailureTest() {
        when(securityContext.authenticate(
                any(HttpServletRequest.class),
                any(HttpServletResponse.class),
                any(AuthenticationParameters.class)))
                .thenReturn(javax.security.enterprise.AuthenticationStatus.SEND_FAILURE);

        loginBacking.setName(name);
        loginBacking.setPassword(password);

        Assertions.assertDoesNotThrow(() -> {
            loginBacking.submit();
        });

        List<FacesMessage> messages = facesContext.getMessageList();
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals("Login failed", messages.get(0).getSummary());
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
