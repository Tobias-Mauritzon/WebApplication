/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.controller;

import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.UserAccount;
import com.lab3.view.CreateUserView;
import com.lab3.controller.PasswordHandler;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.omnifaces.util.Messages;



/**
 *
 * @author Tobias Mauritzon
 * @author Joachim Antfolk
 */
@RequestScoped
@Named
public class CreateUserController implements Serializable{
    
    @EJB
    private UserAccountDAO userAccountDAO;
    
    @Inject
    private  CreateUserView createUserView;
    
    public boolean create(){
        boolean res = true;
  
        try{
            UserAccount u = new UserAccount(createUserView.getMail(), createUserView.getUserName(), PasswordHandler.hashPassword(createUserView.getPassword()));
            userAccountDAO.create(u);
        
        }catch(Exception e){
            res = false;
            Messages.addGlobalError("User exists");
        }
        
        return res;
    }
}
