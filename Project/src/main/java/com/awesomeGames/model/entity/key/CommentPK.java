/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
