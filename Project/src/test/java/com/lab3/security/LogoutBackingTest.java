package com.lab3.security;

import com.lab3.resource.ContextMocker;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
 * Test class for logout back end
 * @author Joachim Antfolk
 */
@RunWith(Arquillian.class)
public class LogoutBackingTest {
    
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    FacesContext facesContext;
    
    LogoutBacking logoutBacking;
    
    /**
     * Init before tests
     */
    @Before
    public void setup(){
        facesContext = ContextMocker.mockServletRequest(); 
        logoutBacking = new LogoutBacking();
    }
    
    @Test
    public void logoutTest() {
        logoutBacking.submit();
        
        List<FacesMessage> messages = facesContext.getMessageList();
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals("Logout successful", messages.get(0).getSummary());
    }
    
    /**
     * TearDown after tests
     */
    @After
    public void tearDown() { 
        facesContext.release();
    }
}
