/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.entity;

import com.lab3.model.dao.CommentDAO;
import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.HighScore;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.UserAccount;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 *
 * @author David
 */
@RunWith(Arquillian.class)
public class CommentTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(CommentDAO.class, Comment.class, UserAccountDAO.class, UserAccount.class, GameDAO.class, Game.class,
                        Rating.class, HighScore.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    
    
    @Test
    public void setGetCommentIdTest() {
       Comment comment = new Comment();
       comment.setCommentId(9);
       Assert.assertEquals(9,comment.getCommentId());
        
    }
}