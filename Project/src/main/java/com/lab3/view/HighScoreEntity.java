/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.view;

/**
 *
 * @author Simon
 */
public class HighScoreEntity {
    String user;
    int sequence;
    
    public HighScoreEntity(String user, int sequence){
        this.user = user;
        this.sequence = sequence;
    }
    
    public String getUserName(){
        return this.user;
    }
    
    public int getSequence(){
        return this.sequence;
    }
}
