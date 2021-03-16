/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awesomeGames.model.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.eclipse.persistence.annotations.CascadeOnDelete;

/**
 * The UserAccount Entity
 * @author Matteus, Lerbyn
 */
@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@CascadeOnDelete
public class UserAccount implements Serializable {

    @NonNull
    @Id 
    private String mail;
    
    @NonNull
    @Column(nullable=false, unique=true)
    private String name;
    
    @NonNull
    @Column(nullable=false, unique=false)
    private String userGroup;
    
    @NonNull
    @Column(nullable=false, unique=false)
    private String password;  
    
    @OneToMany(orphanRemoval=true,mappedBy = "userAccount")
    @Exclude
    private List<HighScore> highScore;
    
    @OneToMany(orphanRemoval=true,mappedBy = "userAccount")
    @Exclude
    private List<Comment> comment;
    
    @OneToMany(orphanRemoval=true,mappedBy = "userAccount")
    @Exclude
    private List<Rating> rating;
}
