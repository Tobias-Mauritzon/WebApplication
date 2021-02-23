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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
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
    
    @Inject
    Pbkdf2PasswordHash passwordHasher;
    
    public boolean create(){
        boolean res = true;
  
        try{
//            UserAccount u = new UserAccount(createUserView.getMail(), createUserView.getUserName(), PasswordHandler.hashPassword(createUserView.getPassword()));
            UserAccount u = new UserAccount(createUserView.getMail(), createUserView.getUserName(), passwordHasher.generate(createUserView.getPassword().toCharArray()));
            userAccountDAO.create(u);
        
        }catch(Exception e){
            res = false;
            Messages.addGlobalError("User exists");
        }
        
        return res;
    }
    
    public boolean verifyUser(){
        List userList;
        userList = userAccountDAO.findUsersWithUsermail(createUserView.getMail());
        if(userList.size() == 1){
            UserAccount u = (UserAccount)userList.get(0);
           
            //doesn't work since the salt isn't accessable. Once the salt is stored in the server, finnish method
            try{
//                System.out.println("From server: " + u.getPassword());
//                System.out.println("From user: " + PasswordHandler.hashPassword(createUserView.getPassword()));
            if(u.getPassword() == PasswordHandler.hashPassword(createUserView.getPassword())){
                return true;
//                System.out.println("Password match, proceed with login");
            }else{
                //DO NOTHING
//                System.out.println("Passwords missmatch");
            }
            }catch(Exception e){
                System.out.println("ERROR" + e.getMessage());
                System.out.println("CHECK VERIFYUSER (METHOD) IN CREATEUSERCONTROLLER.JAVA");
            }
        }
        return false;
    }
}
