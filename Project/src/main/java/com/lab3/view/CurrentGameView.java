
package com.lab3.view;

import com.lab3.model.dao.GameDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.metamodel.EntityType;
import javax.validation.constraints.*;
import jdk.jfr.Registered;
import lombok.Data;



@SessionScoped
@Named
@Data
public class CurrentGameView implements Serializable{
    
    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    private String game;
    private Map<String, String> allGames;

    @Inject 
    private GameDAO gameDAO;
    
    @PostConstruct
    public void init() {
//        allGames = gameDAO.allGames();
    }
    
    public void setGame(String game) {
        System.out.println("gameset: " + game);
        this.game = game;
    }
    
    public void setGameAndRedirect(String game) throws IOException {
        this.game = game;
        System.out.println("game set to " + game);
        FacesContext.getCurrentInstance().getExternalContext().redirect("game.xhtml");
    }
    
    public String getGame() {
        return game;
    }
 
}
