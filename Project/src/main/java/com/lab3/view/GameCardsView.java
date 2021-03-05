package com.lab3.view;

import com.lab3.model.dao.GameDAO;
import com.lab3.model.entity.Game;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Data;
import org.omnifaces.util.Messages;

/**
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
        try {
            List<Game> list = gameDAO.findAllGames();
            setGameList(list);

        } catch (Exception e) {
            Messages.addGlobalError("No Games");
        }
    }
}
