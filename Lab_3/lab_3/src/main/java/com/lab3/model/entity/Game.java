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
public class Game implements Serializable {

    @Id 
    private String name;
    
    @Column(nullable=true, unique=false)
    private String averageRating;
    
    @Column(nullable=true, unique=false)
    private String highScore;
    
}
