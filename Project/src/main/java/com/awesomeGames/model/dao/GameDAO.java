package com.awesomeGames.model.dao;

import com.awesomeGames.model.entity.Game;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;

/**
 * DAO to the Game entity
 *
 * @author Matteus
 * @author David
 */
@Stateless
public class GameDAO extends AbstractDAO<String, Game> {

    @Getter
    @PersistenceContext(unitName = "awesomeGamesPersistence")

    private EntityManager entityManager;

    public GameDAO() {
        super(Game.class);
    }

    /**
     * Finds and returns gameName object matching specified gameName name from
     * database.
     *
     * @param gameName name
     * @return Game object if found, otherwise null
     */
    public Game findGameMatchingName(String gameName) {

        List<Game> returnGameList = entityManager.createQuery("SELECT g FROM Game g WHERE (g.name LIKE :gameName)")
                .setParameter("gameName", gameName).getResultList();

        if (returnGameList.isEmpty()) {
            return null;
        } else {
            return returnGameList.get(0);
        }
    }

    /**
     * Finds and returns the javascript of the gameName by the provided gameName
     * name
     *
     * @param gameName name of the game
     * @return The path to the javasript file
     */
    public String findJavaScriptPathByName(String gameName) {
        Game g = findGameMatchingName(gameName);
        return g.getJavaScript();
    }

    /**
     * Finds and returns all games from the database
     *
     * @return List of all games
     */
    public List findAllGames() {
        return entityManager.createQuery("SELECT g FROM Game g").getResultList();
    }

    /**
     * Finds and returns the newest game
     *
     * @return Game
     */
    public Game findNewestGame() {
        List<Game> q = entityManager.createQuery("SELECT g1 FROM Game g1 WHERE g1.tstamp = (SELECT MAX(g2.tstamp) as maxtstamp FROM Game g2)", Game.class).getResultList();
        if (q.isEmpty()) {
            return null;
        }
        return q.get(0);
    }

    /**
     * Creates a Game entity and inputs it to the database
     *
     * @param name of the Game
     * @param author of the Game
     * @param description of the Game
     * @param javaScriptPath of the Game
     * @param imagePath of the Game
     * @return Game the game that is created
     */
    public Game createGame(String name, String author, String description, String javaScriptPath, String imagePath) {
        Game game = new Game(name, author, description, javaScriptPath, imagePath, new Timestamp(System.currentTimeMillis()));
        this.create(game);
        return game;
    }
}
