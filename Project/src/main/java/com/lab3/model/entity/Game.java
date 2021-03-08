/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
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
 * The Game Entity
 *
 * @author Matteus
 * @author Lerbyn
 */
@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@CascadeOnDelete
public class Game implements Serializable {

    @NonNull
    @Id
    private String name;

    @NonNull
    @Column(nullable = false, unique = false)
    private String author;

    @OneToMany(orphanRemoval = true, mappedBy = "game")
    @Exclude
    private List<HighScore> highScore;

    @OneToMany(orphanRemoval = true, mappedBy = "game")
    @Exclude
    private List<Comment> comment;

    @OneToMany(orphanRemoval = true, mappedBy = "game")
    @Exclude
    private List<Rating> rating;

    @NonNull
    @Column(nullable = false, unique = false)
    private String description;

    @NonNull
    @Column(nullable = false, unique = false)
    private String javaScript;

    @NonNull
    @Column(nullable = false, unique = false)
    private String image;

    @NonNull
    @Column(nullable = false, unique = false)
    private Timestamp tstamp;
}
