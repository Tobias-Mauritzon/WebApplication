
package com.lab3.view;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.dao.UserAccountDAO;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.UserAccount;
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

    @Inject 
    private UserAccountDAO userAccountDAO;
    
    @Inject 
    private GameDAO gameDAO;
    private String game;
    private Game gameObject;
    private UserAccount user;
    
    public void setUser(String user) {
        if(user != null) {
            this.user = userAccountDAO.findUsersWithName(user);
        }
    }
    
    public void setGameObject(String game) {
        if(game != null) {
            this.gameObject = gameDAO.findGameMatchingName(game);        
        }
    }
}