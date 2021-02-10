package com.lab3.model.entity;

import com.lab3.model.entity.key.CommentPK;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Lerbyn
 */



@Data
@Entity
@IdClass(CommentPK.class)
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {

    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int commentId;
    
    @Id
    @OneToOne
    private Users users;
    
    @Id
    @OneToOne
    private Game game;
    
    @Column(nullable=false, unique=false)
    private String commentText;
    
    @Column(nullable=false, unique=false)
    private Timestamp tstamp;
}