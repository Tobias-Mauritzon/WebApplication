/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.dao.RatingDAO;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.Rating;
import com.lab3.model.entity.Users;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class RatingDAOTest {
	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
			.addClasses(RatingDAO.class, Rating.class)
			.addAsResource("META-INF/persistence.xml")
			.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@EJB
	private RatingDAO ratingDAO;

	@Before
	public void init() {
        Users user1 = new Users("mail1", "name1", "password1");
        Game game1 = new Game("Game1", "4", "15");
          
        ratingDAO.create(new Rating(user1,game1,"54"));
	}

	@Test
	public void checkThatFindRatingMatchingNameMatchesCorrectly() {
		Assert.assertTrue(true); /* Some better condition */
	}
}