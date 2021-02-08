/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.Comment;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;

/**
 *
 * @author Lerbyn
 */
@Stateless
public class CommentDAO extends AbstractDAO<Comment> {
    @Getter @PersistenceContext(unitName = "lab3")
    private EntityManager entityManager;

    public CommentDAO() {
        super(Comment.class);
    }
}