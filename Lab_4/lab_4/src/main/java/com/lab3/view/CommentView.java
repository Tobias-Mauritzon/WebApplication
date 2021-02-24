/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.view;

import com.lab3.model.entity.Comment;
import com.lab3.model.entity.UserAccount;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.Size;
import lombok.Data;
import org.omnifaces.util.Faces;

/**
 *
 * @author Tobias
 */
@ViewScoped
@Named
@Data
public class CommentView implements Serializable {

    @Size(min = 2) private String text;
    private String game;
    private UserAccount userAccount = new UserAccount("mail1@gmail.com", "namaefe1", "passworfed1"); // This should be grabbed when you make a comment
    private List<Comment> commentList;

    @PostConstruct
    private void init() {
        String str = Faces.getViewId();
        str = str.split("\\.")[0];
        game = str.substring(1);
    }
}
