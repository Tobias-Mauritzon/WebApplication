package com.awesomeGames.view;

import com.awesomeGames.model.dao.GameDAO;
import com.awesomeGames.model.entity.Game;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Data;

/**
 * View for the GameCards template
 *
 * @author Matteus
 */
@ViewScoped
@Named
@Data
public class GameCardsView implements Serializable {

    private List<Game> gameList;

    @EJB
    private GameDAO gameDAO; 

    /**
     * Finds all games in the database and adds them to a list of games
     */
    public void findGames() {

        List<Game> list = gameDAO.findAllGames();
        setGameList(list);
    }
}
