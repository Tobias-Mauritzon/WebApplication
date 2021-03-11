///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.lab3.security;
//
//import javax.ejb.EJB;
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.WebArchive;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
///**
// *
// * @author David
// */
//@RunWith(Arquillian.class)
//public class LoginBackingTest {
//    @Deployment
//    public static WebArchive createDeployment() {
//        return ShrinkWrap.create(WebArchive.class)
//                .addClasses(LoginBacking.class)
//                .addAsResource("META-INF/persistence.xml")
//                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//    }
//    
////    @EJB
////    private LoginBacking loginBacking; // problem since login will authenticate against the lab4 database
//
//    @Test
//    public void login() {
//        Assert.assertTrue(false);
//    }
//    
//}
