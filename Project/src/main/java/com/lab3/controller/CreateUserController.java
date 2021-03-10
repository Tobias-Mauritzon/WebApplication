/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.controller;

import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.UserAccount;
import com.lab3.view.CreateUserView;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import org.omnifaces.util.Messages;



/**
 *
 * @author Tobias Mauritzon
 * @author Joachim Antfolk
 * @author William Jönsson
 */
@RequestScoped
@Named
public class CreateUserController implements Serializable{
    
    @EJB
    private UserAccountDAO userAccountDAO;
    
    @Inject
    private  CreateUserView createUserView;
    
    @Inject
    Pbkdf2PasswordHash passwordHasher;
    
    @Inject
    private FacesContext facesContext;
    
    public boolean create(){
        boolean res = true;
        
        //confirm password validation
        if(!createUserView.getPassword().equals(createUserView.getConfirmPassword())) {
            Messages.addError("createUser:password", "Password does not match Confirm Password");
            return false;
        }
        try{
            UserAccount u = new UserAccount(createUserView.getMail().toLowerCase(), createUserView.getUserName().toLowerCase(), "USER", passwordHasher.generate(createUserView.getPassword().toCharArray()));        
            userAccountDAO.create(u);
            getExternalContext().getFlash().setKeepMessages(true);
            facesContext.addMessage("account-growl",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Account created successfully", null));
            getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/login.xhtml");
        
        }catch(Exception e){
            res = false;
            try{
                userAccountDAO.findUserWithName(createUserView.getUserName().toLowerCase());
                Messages.addError("createUser:username", "Username already taken");
            } catch(Exception e1) {
                Messages.addError("createUser:email", "Email already taken");
            }
            
        }
        
        return res;
    }
    
    private ExternalContext getExternalContext() {
        return facesContext.getExternalContext();
    }
}