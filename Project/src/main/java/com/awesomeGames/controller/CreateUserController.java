package com.awesomeGames.controller;

import com.awesomeGames.model.dao.UserAccountDAO;
import com.awesomeGames.model.entity.UserAccount;
import com.awesomeGames.view.CreateUserView;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.Data;
import org.omnifaces.util.Messages;

/**
 * Controller for creating users
 *
 * @author Tobias Mauritzon
 * @author Joachim Antfolk
 * @author William Jönsson
 */
@RequestScoped
@Named
@Data
public class CreateUserController implements Serializable {

    @EJB
    private UserAccountDAO userAccountDAO;

    @Inject
    private CreateUserView createUserView;

    @Inject
    Pbkdf2PasswordHash passwordHasher;

    @Inject
    private FacesContext facesContext;

    /**
     * Creates and adds a user account to userAccountDAO
     *
     * @return true if user created and redirects to login.xhtml, false if
     * failed
     */
    public boolean create() {
        boolean res = true;

        //confirm password validation
        if (!createUserView.getPassword().equals(createUserView.getConfirmPassword())) {
            Messages.addError("createUser:password", "Password does not match Confirm Password");
            return false;
        }
        try {
            UserAccount u = new UserAccount(createUserView.getMail().toLowerCase(), createUserView.getUserName().toLowerCase(), "USER", passwordHasher.generate(createUserView.getPassword().toCharArray()));
            if (userAccountDAO.findUsersWithUser(u).isEmpty() && !userAccountDAO.isUserNameUsed(u.getName())) {
                userAccountDAO.create(u);
                getExternalContext().getFlash().setKeepMessages(true);
                facesContext.addMessage("account-growl",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Account created successfully", null));
                getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/login.xhtml");
            } else {
                throw new IllegalArgumentException("Email or username already taken");
            }
        } catch (Exception e) {
            res = false;
            if (userAccountDAO.findUserWithName(createUserView.getUserName().toLowerCase()) != null) {
                Messages.addError("createUser:username", "Username already taken");
            }
            if (!userAccountDAO.findUsersWithUsermail(createUserView.getMail()).isEmpty()) {
                Messages.addError("createUser:email", "Email already taken");
            }
        }

        return res;
    }

    private ExternalContext getExternalContext() {
        return facesContext.getExternalContext();
    }
}
