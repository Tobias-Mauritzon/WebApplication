
package com.lab3.view;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.UserAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Data;



@SessionScoped
@Named
@Data
public class CurrentGameView implements Serializable{

    @Inject 
    private UserAccountDAO userAccountDAO;
    
    @Inject 
    private GameDAO gameDAO;
    
    private String game;
    private Game gameObject;
    private ArrayList games = new ArrayList();
    private UserAccount user;
    
    @PostConstruct
    private void init() {
        //load games on page load
        List<Game> temp = gameDAO.findAllGames();
        for(Game g: temp) {
            games.add(new SelectItem(g.getName(),g.getName()));
        }
    }
    
    public void setUser(String user) {
        if(user != null) {
            this.user = userAccountDAO.findUserWithName(user);
        }
    }
    
    public void setGameObject(String game) {
        if(game != null) {
            this.gameObject = gameDAO.findGameMatchingName(game);        
        }
    }
}
