package com.lab3.controller;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.entity.Game;
import com.lab3.view.GameCardsView;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Messages;

/**
 *
 * @author Matteus
 */
@RequestScoped
@Named
public class GameCardsController implements Serializable{
    
    @EJB
    private GameDAO gameDAO;
    
    @Inject
    private GameCardsView gameCardsView;
    
    /**Finds all games in the database and adds them to a 
     * list of games
     * 
     */
    public void findGames(){
        try{

            List<Game> list = gameDAO.allGames();
            gameCardsView.setGameList(list);
           
            
        }catch(Exception e){
            Messages.addGlobalError("No Games");
        }
    }
}
