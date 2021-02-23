/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.security;

import java.util.Arrays;
import java.util.HashSet;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

/**
 *
 * @author lerbyn
 */
@ApplicationScoped
public class CustomInMemoryIdentityStore implements IdentityStore {
 
    @Override
    public CredentialValidationResult validate(Credential credential) {
 
        //Dummy implementation, should implement @DatabaseIdentityStoreDefinition according to https://rieckpil.de/howto-simple-form-based-authentication-for-jsf-2-3-with-java-ee-8-security-api/
        
        UsernamePasswordCredential login = (UsernamePasswordCredential) credential;
 
        if (login.getCaller().equals("admin@mail.com") 
                       && login.getPasswordAsString().equals("ADMIN1234")) {
            return new CredentialValidationResult("admin", new HashSet<>(Arrays.asList("ADMIN")));
        } else if (login.getCaller().equals("user@mail.com") 
                       && login.getPasswordAsString().equals("USER1234")) {
            return new CredentialValidationResult("user", new HashSet<>(Arrays.asList("USER")));
        } else {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
    }
}