package com.awesomeGames.model.dao;

import com.awesomeGames.model.entity.UserAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import lombok.Getter;

/**
 * DAO to the UserAccount entity
 *
 * @author Matteus
 * @author David
 * @author Tobias
 * @author Joachim Antfolk
 */
@Stateless
public class UserAccountDAO extends AbstractDAO<String, UserAccount> {

    @Getter
    @PersistenceContext(unitName = "awesomeGamesPersistence")
    private EntityManager entityManager;

    public UserAccountDAO() {
        super(UserAccount.class);
    }

    /**
     * Finds and returns the matching user from the database
     *
     * @param user the user to be found
     * @return List of users that match
     */
    public List<UserAccount> findUsersWithUser(UserAccount user) {
        return findUsersWithUsermail(user.getMail());
    }

    /**
     * Finds and returns the matching user with mail from database
     *
     * @param mail the user mail to be found
     * @return List of users that match
     */
    public List<UserAccount> findUsersWithUsermail(String mail) {
        return entityManager.createQuery("SELECT u FROM UserAccount u WHERE u.mail LIKE :mail").setParameter("mail", mail.toLowerCase()).getResultList();
    }

    /**
     * Finds and returns the matching user with name from database
     *
     * @param name the user mail to be found
     * @return List of users that match
     */
    public UserAccount findUserWithName(String name) {
        TypedQuery<UserAccount> q = entityManager.createQuery("SELECT u FROM UserAccount u WHERE u.name LIKE :name", UserAccount.class).setParameter("name", name.toLowerCase());

        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return q.getResultList().get(0);
        }
    }

    /**
     * Checks if a username is used in the database
     *
     * @param userName the userName to be checked
     * @return True if the username is used and False if not
     */
    public boolean isUserNameUsed(String userName) {
        return (entityManager.createQuery("SELECT u FROM UserAccount u WHERE u.name LIKE :username").setParameter("username", userName.toLowerCase()).getResultList().size() > 0);
    }
    
    
    
    
    
    
    
    /**
     * Retrieves hashed password for user
     * @param userName user whose password should be retrieved
     * @return hashed password if password could be retrieved
     *      otherwise null
     */
    public String findPasswordForUserWithUserName(String userName){
        
        List<String> list = entityManager.createQuery("SELECT u.password FROM UserAccount u WHERE u.name LIKE :username").setParameter("username", userName.toLowerCase()).getResultList();
        
        if(list.size() == 1){
            return list.get(0);
        } else {
            return null;
        }
    }
    
    /**
     * 
     * @param userName The userName of the user
     * @param password The new Password
     * @return password change success
     */
    public boolean updatePasswordForUser(String userName, String newPassword){
        Query q = entityManager.createQuery("UPDATE UserAccount u SET u.password = :password WHERE u.name LIKE :username");
        q.setParameter("password", newPassword);
        q.setParameter("username", userName);

        return q.executeUpdate() == 1;
    }
}
