package com.awesomeGames.controller;

import com.awesomeGames.model.dao.CommentDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.key.CommentPK;
import com.awesomeGames.view.UserCommentsView;
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

/**
 * Controller class for user comments component
 * @author Joachim Antfolk
 */
@RequestScoped
@Named
@Data
public class UserCommentsController implements Serializable{
    
    @EJB
    private CommentDAO commentDAO;
    
    @Resource
    UserTransaction utx;
    
    @Inject
    private UserAccountDAO userAccountDAO;
    
    @Inject
    private UserCommentsView userCommentsView;
    
    @Inject
    private FacesContext facesContext;
        
    /**
     * Finds all comments for the current user sorted in a descending order and adds them to view. 
     */
    public void findComments() {
        String user = facesContext.getExternalContext().getUserPrincipal().getName();
        List<Comment> list = commentDAO.findCommentsWithUserDESC(userAccountDAO.findUserWithName(user));
        
        userCommentsView.setCommentList(list);
    }
    
    /**
     * Deletes the given comment if it exists, otherwise it adds an error message to FacesContext
     * @param comment the comment to be deleted
     */
    public void deleteComment(Comment comment) {
        try {
            utx.begin();
            Comment c = commentDAO.find(new CommentPK(comment.getCommentId(), comment.getUserAccount().getMail(), comment.getGame().getName()));
            commentDAO.remove(c);
            utx.commit();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment deleted!", null));
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Can't delete comment!", null));
        }
    }
    
    /**
     * Deletes all comments for the current user.
     */
    public void deleteAllUserComments() {
        String user = facesContext.getExternalContext().getUserPrincipal().getName();
        if(commentDAO.deleteUserCommentsWithUserName(user)){
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Comments Deleted!", null));
        }else{
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Can't delete comments!", null));
        }
    }
}
