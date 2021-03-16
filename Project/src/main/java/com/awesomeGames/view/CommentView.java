package com.awesomeGames.view;

import com.awesomeGames.model.entity.Comment;
import com.awesomeGames.model.entity.Game;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Tobias
 */
@ViewScoped
@Named
@Data
public class CommentView implements Serializable {

    @Size(min = 2, max = 200)
    private String text;
    private String gameName;
    private Game game;
    private List<Comment> commentList;

    @Inject
    private CurrentGameView currentGameView;

    private Boolean descending;

    /**
     * Method runs on page load to set game
     */
    @PostConstruct
    private void init() {
        gameName = currentGameView.getGame();
        descending = true;
    }

    /**
     * Method to run init from public scope, is only supposed to be run from
     * tests
     */
    public void testInit() {
        init();
    }
}
