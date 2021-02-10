package com.lab3.model.entity;

import com.lab3.model.entity.key.HighScorePK;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Exclude;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Lerbyn
 */
@IdClass(HighScorePK.class)

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class HighScore implements Serializable {
    
    @Id
    @Exclude
    @GeneratedValue
    private int id;
    
    @Id
    @NonNull
    @ManyToOne(optional = false)
    private Game game;
    
    @Id
    @NonNull
    @ManyToOne(optional = false)
    private Users users;
    
    @NonNull
    private int highScore;
}