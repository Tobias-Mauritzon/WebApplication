package com.lab3.model.dao;


import com.lab3.model.dao.UsersDAO;
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
public class UsersDAOTest {
    @Deployment
    public static WebArchive createDeployment() {
            return ShrinkWrap.create(WebArchive.class)
                    .addClasses(UsersDAO.class, Users.class)
                    .addAsResource("META-INF/persistence.xml")
                    .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private UsersDAO usersDAO;

    @Before
    public void init() {
    }

    @Test
    public void create_users() {
        Users user2 = new Users("mail2", "name2", "password2");
        Users user3 = new Users("mail3", "name3", "password3");
        usersDAO.create(user2);
        usersDAO.create(user3);
        Assert.assertTrue(true); /* Some better condition */
        usersDAO.remove(user3);
        usersDAO.remove(user2);
    }
}
