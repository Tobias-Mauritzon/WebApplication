/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import jdk.internal.jline.internal.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Exclude;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Matteus
 */
@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Users implements Serializable {

    @NonNull
    @Id 
    private String mail;
    
    @NonNull
    @Column(nullable=false, unique=true)
    private String name;
    
    @NonNull
    @Column(nullable=false, unique=false)
    private String password;  
    
    @OneToMany(orphanRemoval=true,mappedBy = "users")
    @Exclude
    private List<HighScore> highScore;
    
    @OneToMany(orphanRemoval=true,mappedBy = "users")
    @Exclude
    private List<Comment> comment;
}
