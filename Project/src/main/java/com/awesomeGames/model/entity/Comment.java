package com.awesomeGames.model.entity;

import com.awesomeGames.model.entity.key.CommentPK;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The Comment Entity
 *
 * @author David
 */
@Data
@Entity
@IdClass(CommentPK.class)
@NoArgsConstructor
@RequiredArgsConstructor
public class Comment implements Serializable {

    @Id
    @Exclude
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int commentId;

    @Id
    @NonNull
    @ManyToOne(optional = false)
    private UserAccount userAccount;

    @Id
    @NonNull
    @ManyToOne(optional = false)
    private Game game;

    @NonNull
    @Column(nullable = false, unique = false)
    private String text;

    @NonNull
    @Column(nullable = false, unique = false)
    private Timestamp tstamp;
}
