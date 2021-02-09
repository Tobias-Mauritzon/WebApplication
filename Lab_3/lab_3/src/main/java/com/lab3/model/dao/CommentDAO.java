/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.Comment;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;

/**
 *
 * @author Lerbyn
 */
@Stateless
public class CommentDAO extends AbstractDAO<Comment> {
    @Getter @PersistenceContext(unitName = "lab3")
    private EntityManager entityManager;

    public CommentDAO() {
        super(Comment.class);
    }
    
     public List findCommentsWithUsername(String uid) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.users.mail LIKE :username").setParameter("username",uid).getResultList();
//        return entityManager.createQuery("SELECT c.users.mail FROM Comment c").getResultList();
    }
    
//    public List findHighScoreWithName(String name) {
//       // return entityManager.createQuery("SELECT g.highScore FROM Game g WHERE g.name LIKE :gameName ORDER BY e.highScore DESC").setParameter("gameName",name).getResultList();
//       return entityManager.createQuery("SELECT g.highScore FROM Game g WHERE g.name LIKE :gameName").setParameter("gameName",name).getResultList();
//    }
    
}