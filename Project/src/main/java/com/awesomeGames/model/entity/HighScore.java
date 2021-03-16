package com.awesomeGames.model.entity;

import com.awesomeGames.model.entity.key.HighScorePK;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The HighScore Entity
 *
 * @author David
 */
@Data
@Entity
@IdClass(HighScorePK.class)
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
    private UserAccount userAccount;

    @NonNull
    private int highScore;
}
