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
    
    public List<Users>findUsersMatchingName(String uid) {
        return entityManager.createQuery("SELECT u FROM Users u WHERE u.mail LIKE :username").setParameter("username",uid).getResultList();
    }
}
