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
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import org.omnifaces.util.Messages;



/**
 *
 * @author Tobias
 */
@RequestScoped
@Named
public class CreateUserController implements Serializable{
    
    @EJB
    private UserAccountDAO userAccountDAO;
    
    @Inject
    private  CreateUserView createUserView;
    
    public boolean create(){
        System.out.println("\nPrint Print\n");
        boolean res = true;
        UserAccount u = new UserAccount(createUserView.getUserName(), createUserView.getMail(), createUserView.getPassword());
        
        try{

            userAccountDAO.create(u);
        
        }catch(Exception e){
            res = false;
            Messages.addGlobalError("User exists");
        }
        
        return res;
    }
}
