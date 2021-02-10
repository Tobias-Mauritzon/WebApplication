/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.key;

import com.lab3.model.entity.Game;
import com.lab3.model.entity.Users;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author Simon
 */
@EqualsAndHashCode
public class CommentPK implements Serializable{
    private long commentId;
    private String users;
    private String game;
    
    public CommentPK(){
        
    }
    
    public CommentPK(long commentId, String users, String game){
     this.commentId = commentId;
     this.users = users;
     this.game = game;
    }
    
}
