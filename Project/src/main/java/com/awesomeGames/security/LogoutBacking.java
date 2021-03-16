package com.awesomeGames.security;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Used to logout the user from the front end
 * @author David 
 */
@Named
@RequestScoped
public class LogoutBacking {

    /**
     * method used to logout the current user
     *
     * @return a redirect message
     */
    public String submit() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.invalidateSession();
        externalContext.getFlash().setKeepMessages(true);
        facesContext.addMessage("account-growl",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout successful", null));
        return "/login.xhtml?faces-redirect=true";
    }
}
