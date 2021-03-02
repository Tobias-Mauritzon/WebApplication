package com.lab3.controller;

import com.lab3.model.dao.CommentDAO;
import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.UserAccount;
import com.lab3.view.CommentView;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Tobias, Matteus
 */
@RequestScoped
@Named
public class CommentController implements Serializable{
    
    @EJB
    private CommentDAO commentDAO;
    
    @EJB
    private GameDAO gameDAO;
    
    @EJB
    private UserAccountDAO userAccountDAO;
    
    @Inject
    private CommentView commentView;
    
    /**Creates a comment for the given user on the game page
     * 
     * @param userName
     * @return false if rating could not be created otherwise true
     */
    public boolean create(String userName){
        boolean res = true;
        try{    
            //Hard coded user should be removed
            commentDAO.createComment(gameDAO.findGameMatchingName(commentView.getGameName()), userAccountDAO.findUsersWithName(userName), commentView.getText());
            Messages.addGlobalInfo("Comment created");
        }catch(Exception e){
            res = false;
            Messages.addGlobalError("Comment could not be created");
        }
        
        return res;
    }
    
    /**
     * Finds all comments for the current game in a descending order
     */
    public void findComments(){
        try{
            
            if(commentView.getDescending()){
                List<Comment> list = commentDAO.findCommentsWithGamenameASC(commentView.getGameName());
                commentView.setCommentList(list);
            }
            else{
                List<Comment> list = commentDAO.findCommentsWithGamenameDESC(commentView.getGameName());
                commentView.setCommentList(list);
            }
        }catch(Exception e){
            Messages.addGlobalError("No comments");
        }
    }
    
    /**
     * Flips the boolean flag that dictates which direction the comments are rendered in
     */
    public void flipDescending(){
            if(commentView.getDescending()){
                commentView.setDescending(Boolean.FALSE);
            }
            else{
                commentView.setDescending(Boolean.TRUE);
            }
    }
    
    /**Finds and sets the game entity with the given gameName
     * 
     */
    public void findGame(){
        try{
            Game game = gameDAO.findGameMatchingName(commentView.getGameName());
            commentView.setGame(game);

        }catch(Exception e){
            Messages.addGlobalError("Can't find game");
        }
    }
    
    
}
