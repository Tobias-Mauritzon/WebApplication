/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.security;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author David
 * used to logout the user from the fronend
 */
@Named
@RequestScoped
public class LogoutBacking {

    /**
     *  method used to logout the current user
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
