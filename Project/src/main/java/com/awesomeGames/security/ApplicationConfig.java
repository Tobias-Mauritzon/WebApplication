
package com.awesomeGames.security;


import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;

import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

/**
 *
 * @author David
 * Used to setup an authentication link to the database
 */


//establish connection with database (name = lab4)
//create query to be used when authenticating looks for password and group aka role based on inmputed username in LoginBacking
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
