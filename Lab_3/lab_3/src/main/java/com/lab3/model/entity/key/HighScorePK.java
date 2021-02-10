/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.entity.key;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;



/**
 *
 * @author lerbyn
 */

@EqualsAndHashCode
@NoArgsConstructor
public class HighScorePK implements Serializable{
    @Id 
    private int id;
    
    @Id
    private String game;
    
    @Id
    private String users;
    
    
}
