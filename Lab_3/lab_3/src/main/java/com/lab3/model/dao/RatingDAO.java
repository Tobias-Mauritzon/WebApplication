/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.model.dao;

import com.lab3.model.entity.Rating;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;

/**
 *
 * @author Matteus
 */
@Stateless
public class RatingDAO extends AbstractDAO<Rating> {
    @Getter @PersistenceContext(unitName = "lab3")
    private EntityManager entityManager;
    
    public RatingDAO() {
        super(Rating.class);
    }
    public List<Rating>findRatingMatchingName() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}