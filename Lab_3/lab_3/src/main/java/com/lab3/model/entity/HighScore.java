package com.lab3.model.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Lerbyn
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class HighScore implements Serializable {
    @Id
    @OneToOne
    private Game game;
    
    @Id
    @OneToOne
    private Users users;
    
    @Column(nullable=false, unique=false)
    private int highScore;
}