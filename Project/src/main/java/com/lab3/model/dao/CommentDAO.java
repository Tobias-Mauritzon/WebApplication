package com.lab3.model.dao;

import com.lab3.model.entity.Comment;
import com.lab3.model.entity.Game;
import com.lab3.model.entity.UserAccount;
import com.lab3.model.entity.key.CommentPK;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lombok.Getter;

/**
 * DAO to the Comment entity
 *
 * @author Matteus
 * @author David
 * @author Tobias
 */
@Stateless
public class CommentDAO extends AbstractDAO<CommentPK, Comment> {

    @Getter
    @PersistenceContext(unitName = "lab3")
    private EntityManager entityManager;

    public CommentDAO() {
        super(Comment.class);
    }

    /**
     * Finds and returns the Comments of the inputed User from the database
     *
     * @param user the users comments to be found
     * @return List of comments from that user
     */
    public List<Comment> findCommentsWithUserASC(UserAccount user) {
        return findCommentsWithUsermailASC(user.getMail());
    }

    /**
     * Finds and returns the Comments of the inputed user mail from the database
     *
     * @param mail the user mails comments to be found
     * @return List of comments from that user mail
     */
    public List<Comment> findCommentsWithUsermailASC(String mail) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.userAccount.mail LIKE :mail ORDER BY c.commentId ASC").setParameter("mail", mail).getResultList();
    }

    /**
     * Finds and returns the Comments of the inputed User from the database
     *
     * @param user the users comments to be found
     * @return List of comments from that user
     */
    public List<Comment> findCommentsWithUserDESC(UserAccount user) {
        return findCommentsWithUsermailDESC(user.getMail());
    }

    /**
     * Finds and returns the Comments of the inputed user mail from the database
     *
     * @param mail the user mails comments to be found
     * @return List of comments from that user mail
     */
    public List<Comment> findCommentsWithUsermailDESC(String mail) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.userAccount.mail LIKE :mail ORDER BY c.commentId DESC").setParameter("mail", mail).getResultList();
    }

    /**
     * Finds and returns the Comments of the inputed game from the database
     *
     * @param game the games comments to be found
     * @return List of comments from that game
     */
    public List<Comment> findCommentsWithGameASC(Game game) {
        return findCommentsWithGamenameASC(game.getName());
    }

    /**
     * Finds and returns the Comments of the inputed game name from the database
     *
     * @param gamename the gamenames comments to be found
     * @return List of comments from that gamename
     */
    public List<Comment> findCommentsWithGamenameASC(String gamename) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.game.name LIKE :gamename ORDER BY c.commentId ASC").setParameter("gamename", gamename).getResultList();
    }

    /**
     * Finds and returns the Comments of the inputed game from the database
     *
     * @param game the games comments to be found
     * @return List of comments from that game
     */
    public List<Comment> findCommentsWithGameDESC(Game game) {
        return findCommentsWithGamenameDESC(game.getName());
    }

    /**
     * Finds and returns the Comments of the inputed game name from the database
     *
     * @param gamename the gamenames comments to be found
     * @return List of comments from that gamename
     */
    public List<Comment> findCommentsWithGamenameDESC(String gamename) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.game.name LIKE :gamename ORDER BY c.commentId DESC").setParameter("gamename", gamename).getResultList();
    }

    /**
     * Finds and returns the GameName with the most comments
     *
     * @return GameName of the most commented game else null if there are no
     * comments
     */
    public String findsGameNameWithMostComments() {

        Query nativeQuery = entityManager.createNativeQuery("SELECT GAME_NAME FROM (SELECT GAME_NAME, COUNT(COMMENTID) as comCount FROM USER1.COMMENT GROUP BY GAME_NAME) as subq1 WHERE comCount = (SELECT MAX(comCount) as comMax FROM (SELECT GAME_NAME, COUNT(COMMENTID) as comCount FROM USER1.COMMENT GROUP BY GAME_NAME) as subq2)");

        List list = nativeQuery.getResultList();

        if (list.isEmpty()) {
            return null;
        } else {
            return nativeQuery.getResultList().get(0).toString();
        }
    }

    /**
     * Creates a Comment enitity and inputs it to the database
     *
     * @param game the game the comment is on
     * @param user the user that wrote the comment
     * @param commmentText the comment text
     * @return the comment that is created or null if it cant be created
     */
    public Comment createComment(Game game, UserAccount user, String commmentText) {

        Comment comment = new Comment(user, game, commmentText, new Timestamp(System.currentTimeMillis()));
        this.create(comment);
        return comment;
    }
}
