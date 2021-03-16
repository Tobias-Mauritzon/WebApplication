package com.lab3.controller;

import com.lab3.model.dao.CommentDAO;
import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.UserAccount;
import com.lab3.model.entity.key.CommentPK;
import com.lab3.view.CommentView;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.UserTransaction;
import lombok.Data;
import org.omnifaces.util.Messages;

/**
 * Controller Bean for the Comment view
 * @author Tobias
 * @author Matteus
 * @author David
 */
@RequestScoped
@Named
@Data
public class CommentController implements Serializable {

    @EJB
    private CommentDAO commentDAO;

    @Resource
    UserTransaction utx;
    
    @EJB
    private GameDAO gameDAO;

    @EJB
    private UserAccountDAO userAccountDAO;

    @Inject
    private CommentView commentView;
    
    @Inject
    private FacesContext facesContext;

    /**
     * Creates a comment for the given user on the game page
     *
     * @param userName
     * @return false if comment could not be created otherwise true
     */
    public boolean create(String userName) {
        boolean res = false;
        boolean signedIn = false;
        boolean gameFound = false;

        UserAccount user = new UserAccount();
        Game game = new Game();

        if(userAccountDAO.findUserWithName(userName) != null) {
            user = userAccountDAO.findUserWithName(userName);
            signedIn = true;
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not found or logged in", null));
        }

        if(gameDAO.findGameMatchingName(commentView.getGameName()) != null) {
            game = gameDAO.findGameMatchingName(commentView.getGameName());
            gameFound = true;
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Game not found", null));
        }

        if (signedIn && gameFound) {
//            try {
            commentDAO.createComment(game, user, commentView.getText());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment created", null));
            res = true;
//            } catch (Exception e) {
//                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Comment could not be created", null));
//            }
        }
        return res;
    }

    /**
     * Finds all comments for the current game in a descending order
     */
    public void findComments() {
        List<Comment> list;
        if (commentView.getDescending()) {
            list = commentDAO.findCommentsWithGamenameASC(commentView.getGameName());
        } else {
            list = commentDAO.findCommentsWithGamenameDESC(commentView.getGameName());
        }
        
//        if(list != null) {
          commentView.setCommentList(list);
//        } else {
//            System.out.println("LSIT IS NULL " + commentView.getCommentList());
//            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No comments", null));
//        }
    }

    /**
     * Flips the boolean flag that dictates which direction the comments are
     * rendered in
     */
    public void flipDescending() {
        if (commentView.getDescending()) {
            commentView.setDescending(Boolean.FALSE);
        } else {
            commentView.setDescending(Boolean.TRUE);
        }
    }

    /**
     * Finds and sets the game entity with the given gameName
     *
     */
    public void findGame() {
        Game game = gameDAO.findGameMatchingName(commentView.getGameName());
        if(game != null) {
            commentView.setGame(game);
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Can't find game", null));
        }
    }
    
    /**
     * Deletes the given comment
     *
     * @param comment the comment to be deleted
     */
    public void deleteComment(Comment comment) {
        try {
            utx.begin();
            
            Comment foundComment = commentDAO.find(new CommentPK(comment.getCommentId(), comment.getUserAccount().getMail(), comment.getGame().getName()));
            
            commentDAO.remove(foundComment);
            
            utx.commit();
            
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment Deleted", null));
        } catch (Exception ex) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Can't delete comment", null));
        }
    }

}
