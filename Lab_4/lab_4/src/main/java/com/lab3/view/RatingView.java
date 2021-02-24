/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.view;

import com.lab3.model.entity.UserAccount;
import java.io.Serializable;
import java.util.LinkedList;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Data;
import org.omnifaces.util.Faces;

/**
 *
 * @author Tobias
 */
@ViewScoped
@Named
@Data
public class RatingView implements Serializable {

    private String game;
    private Integer rating;
    
    @PostConstruct
    private void init() {
        String str = Faces.getViewId();
        str = str.split("\\.")[0];
        game = str.substring(1);
    }
}
