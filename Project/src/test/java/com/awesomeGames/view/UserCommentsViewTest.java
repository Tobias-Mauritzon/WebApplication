package com.awesomeGames.view;

import com.awesomeGames.model.entity.Comment;
import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for UserCommentsView
 * @author Joachim Antfolk
 */
@RunWith(Arquillian.class)
public class UserCommentsViewTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(Comment.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private UserCommentsView userCommentsView;
    
    /**
     * Test for getter and setter in the UserCommentsView class
     */
    @Test
    public void UserCommentsViewTest(){
        userCommentsView = new UserCommentsView();
        List<Comment> list = new ArrayList<Comment>();
        userCommentsView.setCommentList(list);
        Assert.assertEquals(list, userCommentsView.getCommentList());
    }
}
