/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.UserAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;

/**
 * DAO to the UserAccount entity
 * @author Matteus
 */
@Stateless
public class UserAccountDAO extends AbstractDAO<String,UserAccount> {
    @Getter @PersistenceContext(unitName = "lab3")
    private EntityManager entityManager;
    
    public UserAccountDAO() {
        super(UserAccount.class);
    }
    
    /**
     * Finds and returns the matching user from the database
     * @param user the user to be found
     * @return List of users that match
     */
    public List<UserAccount> findUsersWithUser(UserAccount user) {
        return findUsersWithUsermail(user.getMail());
    }
    
    /**
     * Finds and returns the matching user mail from the database
     * @param mail the user mail to be found
     * @return List of users that match
     */
    public List<UserAccount> findUsersWithUsermail(String mail) {
        return entityManager.createQuery("SELECT u FROM UserAccount u WHERE u.mail LIKE :mail").setParameter("mail",mail).getResultList();
    }
    
    /**
     * Checks if a username is used in the database
     * @param userName the userName to be checked
     * @return True if the username is used and False if not
     */
    public boolean isUserNameUsed(String userName) {
        return (entityManager.createQuery("SELECT u FROM UserAccount u WHERE u.name LIKE :username").setParameter("username",userName).getResultList().size() > 0);
    }
    
}
