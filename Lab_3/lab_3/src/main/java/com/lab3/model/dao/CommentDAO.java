/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.Users;
import com.lab3.model.entity.key.CommentPK;
import java.sql.Timestamp;
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
public class CommentDAO extends AbstractDAO<CommentPK,Comment> {
    @Getter @PersistenceContext(unitName = "lab3")
    private EntityManager entityManager;

    public CommentDAO() {
        super(Comment.class);
    }
    
    public List findCommentsWithUsermail(String mail) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.users.mail LIKE :mail ORDER BY c.commentId").setParameter("mail",mail).getResultList();
    }
    
    public List findCommentsWithUser(Users user) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.users.mail LIKE :mail ORDER BY c.commentId").setParameter("mail",user.getMail()).getResultList();
    }
     
    public List findCommentsWithGame(Game game) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.game.name LIKE :gamename ORDER BY c.commentId").setParameter("gamename",game.getName()).getResultList();
    }
    
    public Comment createComment(Game game, Users user, String commmentText) {
        try { // This try catch dosnt seem to catch sql exceptions
            Comment comment = new Comment(user, game, commmentText, new Timestamp(System.currentTimeMillis()));
            this.create(comment);
            return comment;
        } catch (Exception e) {
            return null;
        }
    }
    
}