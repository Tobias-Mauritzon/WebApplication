
package com.lab3.security;


import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;

import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

/**
 *
 * @author lerbyn
 */
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/lab4",
        callerQuery = "SELECT password FROM useraccount WHERE name = ?",
        groupsQuery = "SELECT userGroup FROM useraccount WHERE name = ?"
        
)
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                errorPage = "",
                useForwardToLogin = false
        )
)
@FacesConfig
@ApplicationScoped
public class ApplicationConfig {
    
}
