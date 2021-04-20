package com.awesomeGames.security;

import com.awesomeGames.model.dao.UserAccountDAO;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * Backing bean for changing password
 * @author Joachim Antfolk
 */
@Named
@RequestScoped
@Data
public class PasswordChangeBacking {
    @NotEmpty
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String currentPassword;
    
    @NotEmpty
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String newPassword;
    
    @NotEmpty
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String confirmPassword;
    
    @Inject
    private UserAccountDAO userAccountDAO;  

    @Inject
    private FacesContext facesContext;
    
    @Inject
    Pbkdf2PasswordHash passwordHasher;
        
    /**
     * Method called to change password
     */
    public void submit() {   
        boolean verified = authenticate();
        boolean passwordsMatch = newPassword.equals(confirmPassword);
        
        if(verified && passwordsMatch){
            String user = facesContext.getExternalContext().getUserPrincipal().getName();
            if(userAccountDAO.updatePasswordForUser(user, passwordHasher.generate(newPassword.toCharArray()))){
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed successfully!", null)); 
            }
        } else {
            if(!verified){
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Current password is invalid!", null));
            }
            if(!passwordsMatch){
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "New and confirm passwords must match!", null));
            }
        }
    }

    /**
     * private method used to authenticate against
     * DatabaseIdentityStoreDefinition defined in ApplicationConfig.java
     *
     * @return a message of type AuthenticationStatus which specifies how the
     * authentication went
     */
    private boolean authenticate() {
        String user = facesContext.getExternalContext().getUserPrincipal().getName(); 
        String hashedPass = userAccountDAO.findPasswordForUserWithUserName(user);
        
        if (hashedPass != null){
            return passwordHasher.verify(currentPassword.toCharArray(), hashedPass);
        } else {
            return false;
        }
    }
}
