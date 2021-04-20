package com.awesomeGames.view;

import com.awesomeGames.model.entity.HighScore;
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
 * Test class for UserScoresView
 * @author Joachim Antfolk
 */
@RunWith(Arquillian.class)
public class UserScoresViewTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(HighScore.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private UserScoresView userScoresView;
    
    /**
     * Test for getter and setter in the UserScoresView class
     */
    @Test
    public void UserScoresViewTest(){
        userScoresView = new UserScoresView();
        List<HighScore> list = new ArrayList<HighScore>();
        userScoresView.setScoreList(list);
        Assert.assertEquals(list, userScoresView.getScoreList());
    }
}
