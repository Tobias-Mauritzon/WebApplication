package com.lab3.security;

import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import static javax.security.enterprise.AuthenticationStatus.NOT_DONE;
import static javax.security.enterprise.AuthenticationStatus.SEND_CONTINUE;
import static javax.security.enterprise.AuthenticationStatus.SEND_FAILURE;
import static javax.security.enterprise.AuthenticationStatus.SUCCESS;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author lerbyn
 * Used to authenticate login from frontend
 */
@Named
@RequestScoped
@Data
public class LoginBacking {

    @NotEmpty
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @NotEmpty
    @Size(min = 2,max = 30, message = "Please provide a valid username, must be between 2-30 characters")
    private String name;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private FacesContext facesContext;

    /**
     * Method called to make a login attempt
     * @throws IOException
     */
    public void submit() throws IOException {

        switch (continueAuthentication()) {
            case SEND_CONTINUE:
                facesContext.responseComplete();
                break;
            case SEND_FAILURE:
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", null));
                break;
            case SUCCESS:
                getExternalContext().getFlash().setKeepMessages(true);
                facesContext.addMessage("account-growl",
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "<html><div>Login successful</div><div>Welcome back " + name + "</div></html>", null));
                getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/index.xhtml");
                break;
            case NOT_DONE:
        }
    }

    /**
     * private method used to authenticate against DatabaseIdentityStoreDefinition defined in ApplicationConfig.java
     * @return a message of type AuthenticationStatus which specifies how the authentication went
     */
    private AuthenticationStatus continueAuthentication() {
        ExternalContext externalContext = getExternalContext();
        return securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                AuthenticationParameters.withParams()
                        .credential(new UsernamePasswordCredential(name.toLowerCase(), password))
        );
    }
    
    /**
     * private method used to authenticate against DatabaseIdentityStoreDefinition defined in ApplicationConfig.java
     */
    private ExternalContext getExternalContext() {
        return facesContext.getExternalContext();
    }
}
