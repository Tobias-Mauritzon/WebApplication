/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.controller;

import com.lab3.model.dao.CommentDAO;
import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Comment;
import com.lab3.model.entity.UserAccount;
import com.lab3.view.CommentView;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Messages;

/**
 *
 * @author Tobias
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
    
    public boolean create(){
        boolean res = true;
        
        try{         
            commentDAO.createComment(gameDAO.findGameMatchingName(commentView.getGame()), userAccountDAO.find("davids@mail.com"), commentView.getText());
        }catch(Exception e){
            res = false;
            Messages.addGlobalError("Comment could not be created");
        }
        
        return res;
    }
    
    public void findComments(){
        try{

            List<Comment> list = commentDAO.findCommentsWithGamename(commentView.getGame());
            commentView.setCommentList(list);
            

            
        }catch(Exception e){
            Messages.addGlobalError("No comments");
        }
    }
}
