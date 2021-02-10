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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 *
 * @author Matteus
 * @author Tobias
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Rating implements Serializable {

    @Id
    @NonNull
    @ManyToOne(optional = false)
    private Game game;
    
    @Id
    @NonNull
    @ManyToOne(optional = false)
    private Users users;
    
    @Min(0)
    @Max(10)
    @Column(nullable=false, unique=false)
    private int rating;
       
}