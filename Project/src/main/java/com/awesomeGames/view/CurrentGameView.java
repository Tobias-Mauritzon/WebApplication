package com.awesomeGames.view;

import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.Game;
import com.awesomeGames.model.entity.UserAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Data;

/**
 *
 * @author David
 */
@SessionScoped
@Named
@Data
public class CurrentGameView implements Serializable {

    @Inject
    private UserAccountDAO userAccountDAO;

    @Inject
    private GameDAO gameDAO;

    private String game;
    private List<Game> gameList;
    private Game gameObject;
    private ArrayList games = new ArrayList();
    private UserAccount user;

    /**
     * Method runs on page load to set games
     */
    @PostConstruct
    private void init() {
        //load games on page load
        setGameList(gameDAO.findAllGames());
        for (Game g : gameList) {
            games.add(new SelectItem(g.getName(), g.getName()));
        }
    }
    
    /**
     * Setter for variable user 
     */
    public void setUser(String user) {
        if (user != null) {
            this.user = userAccountDAO.findUserWithName(user);
        }
    }
    
    /**
     * Setter for variable GameObject 
     */
    public void setGameObject(String game) {
        if (game != null) {
            this.gameObject = gameDAO.findGameMatchingName(game);
        }
    }

    /**
     * Method to run init from public scope, is only supposed to be run from
     * tests
     */
    public void testInit() {
        init();
    }
}
