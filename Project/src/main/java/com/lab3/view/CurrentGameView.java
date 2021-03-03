
package com.lab3.view;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.*;
import jdk.jfr.Registered;
import lombok.Data;



@RequestScoped
@Named
@Data
public class CurrentGameView implements Serializable{
    
    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    private String game;
    private Map<String, String> allGames;

    @PostConstruct
    public void init() {
        allGames = new HashMap<String, String>();
        allGames.put("chat", "chat");
        allGames.put("matchstick", "matchstick");
    }
    
    public void setGame(String game) {
        System.out.println("gameset: " + game);
        this.game = game;
    }
}
