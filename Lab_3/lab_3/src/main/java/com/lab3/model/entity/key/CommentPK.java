/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.entity.key;

import com.lab3.model.entity.Game;
import com.lab3.model.entity.Users;
import java.io.Serializable;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author Simon
 */
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CommentPK implements Serializable{
    @Id
    private long commentId;
    @Id
    private String users;
    @Id
    private String game;    
}
