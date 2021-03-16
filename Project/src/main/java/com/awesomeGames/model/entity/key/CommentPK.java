package com.awesomeGames.model.entity.key;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author Simon
 */
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CommentPK implements Serializable {

    @Id
    private int commentId;
    @Id
    private String userAccount;
    @Id
    private String game;
}
