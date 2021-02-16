/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedbean;

import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.UserAccount;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.UserTransaction;





/**
 *
 * @author Tobias
 */
@RequestScoped
@Named("createUser")
public class CreateUser implements Serializable{
    
    @EJB
    private UserAccountDAO userAccountDAO;
    
    @Inject
    private UserTransaction tx;
    
    public boolean create(){
        System.out.println("\nPrint Print\n");
        boolean res = true;
        UserAccount u = new UserAccount("tob", "joc", "pass");
        
        try{

            userAccountDAO.create(u);
        
        }catch(Exception e){
            res = false;
            System.out.println("\nFail Fail\n");
        }
        
        return res;
    }
}
