/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awesomeGames.view;

import com.awesomeGames.view.CreateUserView;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for CreateUserView
 * @author Joachim Antfolk
 */
@RunWith(Arquillian.class)
public class CreateUserViewTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(CreateUserView.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private CreateUserView createUserView;
    
    /**
     * Setup before tests
     */
    @Before
    public void setup(){
        createUserView = new CreateUserView();
    }
    
    /**
     * Tests that mail can be set and gotten
     */
    @Test
    public void mailTest(){
        String mail = "mail@mail.com";
        createUserView.setMail(mail);
        Assert.assertEquals(mail, createUserView.getMail());
    }
    
    /**
     * Tests that user name can be set and gotten
     */
    @Test
    public void nameTest(){
        String name = "Name Nameson";
        createUserView.setUserName(name);
        Assert.assertEquals(name, createUserView.getUserName());
    }
    
    /**
     * Tests that password can be set and gotten
     */
    @Test
    public void passwordTest(){
        String pass = "pa55w0rd";
        createUserView.setPassword(pass);
        Assert.assertEquals(pass, createUserView.getPassword());
    }
    
    /**
     * Tests that confirm password can be set and gotten
     */
    @Test
    public void confirmPasswordTest(){
        String cpass = "pa55w0rd";
        createUserView.setConfirmPassword(cpass);
        Assert.assertEquals(cpass, createUserView.getConfirmPassword());
    }
}
