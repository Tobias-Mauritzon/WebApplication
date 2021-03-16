package com.awesomeGames.model.entity.key;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author David
 */
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RatingPK implements Serializable {

    @Id
    private String game;

    @Id
    private String userAccount;

}
