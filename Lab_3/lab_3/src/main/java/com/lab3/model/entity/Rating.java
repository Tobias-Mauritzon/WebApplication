/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.entity;

import com.lab3.model.entity.key.RatingPK;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * The Rating Entity
 * @author Matteus
 * @author Tobias
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RatingPK.class)
public class Rating implements Serializable {

    @Id
    @NonNull
    @ManyToOne(optional = false)
    private Game game;
    
    @Id
    @NonNull
    @ManyToOne(optional = false)
    private UserAccount userAccount;
    
    @Min(0)
    @Max(10)
    @Column(nullable=false, unique=false)
    private int rating;
       
}