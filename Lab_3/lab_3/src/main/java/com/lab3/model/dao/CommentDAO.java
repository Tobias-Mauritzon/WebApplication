/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.UserAccount;
import com.lab3.model.entity.key.CommentPK;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;

/**
 * DAO to the Comment entity
 * @author Matteus, Lerbyn
 */
@Stateless
public class CommentDAO extends AbstractDAO<CommentPK,Comment> {
    @Getter @PersistenceContext(unitName = "lab3")
    private EntityManager entityManager;

    public CommentDAO() {
        super(Comment.class);
    }
    
    /**
     * Finds and returns the Comments of the inputed User from the database
     * @param user the users comments to be found
     * @return List of comments from that user
     */
    public List<Comment> findCommentsWithUser(UserAccount user) {
        return findCommentsWithUsermail(user.getMail());
    }
    
    /**
     * Finds and returns the Comments of the inputed user mail from the database
     * @param mail the user mails comments to be found
     * @return List of comments from that user mail
     */
    public List<Comment> findCommentsWithUsermail(String mail) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.userAccount.mail LIKE :mail ORDER BY c.commentId").setParameter("mail",mail).getResultList();
    }
     
    /**
     * Finds and returns the Comments of the inputed game from the database
     * @param game the games comments to be found
     * @return List of comments from that game
     */
    public List<Comment> findCommentsWithGame(Game game) {
        return findCommentsWithGamename(game.getName());
    }
    
    /**
     * Finds and returns the Comments of the inputed game name from the database
     * @param gamename the gamenames comments to be found
     * @return List of comments from that gamename
     */
    public List<Comment> findCommentsWithGamename(String gamename) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.game.name LIKE :gamename ORDER BY c.commentId").setParameter("gamename",gamename).getResultList();
    }
    
    /**
     * Creates a Comment enitity and inputs it to the database
     * @param game the game the comment is on
     * @param user the user that wrote the comment
     * @param commmentText the comment text
     * @return the comment that is created or null if it cant be created
     */
    public Comment createComment(Game game, UserAccount user, String commmentText) {
        try { // This try catch dosnt seem to catch sql exceptions
            Comment comment = new Comment(user, game, commmentText, new Timestamp(System.currentTimeMillis()));
            this.create(comment);
            return comment;
        } catch (Exception e) {
            return null;
        }
    }
    
}