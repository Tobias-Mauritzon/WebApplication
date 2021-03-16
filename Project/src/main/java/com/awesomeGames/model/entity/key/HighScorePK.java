package com.awesomeGames.model.entity.key;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author lerbyn
 */
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class HighScorePK implements Serializable {

    @Id
    private int id;

    @Id
    private String game;

    @Id
    private String userAccount;

}
