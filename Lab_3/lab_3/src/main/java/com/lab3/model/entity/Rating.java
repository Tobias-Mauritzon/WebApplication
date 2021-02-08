/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Matteus
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Rating implements Serializable {

    @Id 
    @OneToOne
    private Users users;
    
    @Id 
    @OneToOne
    private Game game;
    
    @Column(nullable=false, unique=false)
    private String Rating;
       
}