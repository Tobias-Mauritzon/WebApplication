/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;

/**
 *
 * @author Matteus
 */
@Stateless
public class UsersDAO extends AbstractDAO<String,Users> {
    @Getter @PersistenceContext(unitName = "lab3")
    private EntityManager entityManager;
    
    public UsersDAO() {
        super(Users.class);
    }
    
    public List findUsersWithUser(Users user) {
        return findUsersWithUsermail(user.getMail());
    }
    
    public List<Users> findUsersWithUsermail(String mail) {
        return entityManager.createQuery("SELECT u FROM Users u WHERE u.mail LIKE :mail").setParameter("mail",mail).getResultList();
    }
    
    public boolean isUserNameUsed(String userName) {
        return (entityManager.createQuery("SELECT u FROM Users u WHERE u.name LIKE :username").setParameter("username",userName).getResultList().size() > 0);
    }
    
}
