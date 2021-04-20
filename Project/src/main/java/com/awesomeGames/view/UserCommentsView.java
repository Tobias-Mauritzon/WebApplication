package com.awesomeGames.view;

import com.awesomeGames.model.entity.Comment;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Data;

/**
 * View class for comments on user page
 * @author Joachim Antfolk
 */
@ViewScoped
@Named
@Data
public class UserCommentsView implements Serializable{
    private List<Comment> commentList;
}
