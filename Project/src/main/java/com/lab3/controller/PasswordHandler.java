/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.controller;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Handles hashing of passwords
 * 
 * @author Joachim Antfolk
 * @since 2021-02-21
 */
public class PasswordHandler {
            
    /**
     * Generates random salt
     * @return salt as byte array
     */
    public static byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt); 
        
        return salt;
    }
    
    /**
     * Hashes given password with given salt
     * @param salt salt for hashing
     * @param pass clear-text password
     * @return hashed password as String
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException 
     */
    public static String hashPasswordWithSalt(byte[] salt, String pass) throws InvalidKeySpecException, NoSuchAlgorithmException{
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 65536, 128);
        
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        
        //System.out.println("SALT: " + salt.length + " : " + new String(salt, StandardCharsets.UTF_8));
        //System.out.println("PASS: " + hash.length + " : " + new String(hash, StandardCharsets.UTF_8));
        
        return new String(hash, StandardCharsets.UTF_8);
    }
    
    /**
     * Generates a salt and hashes the password with this salt
     * @param pass clear-text password
     * @return salt and hashed password as String, size of salt followed by special character S then salt after that hashed password
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException 
     */
    public static String hashPassword(String pass) throws InvalidKeySpecException, NoSuchAlgorithmException{
        byte[] salt = generateSalt();
        String hashedPass = hashPasswordWithSalt(salt, pass);
        
        String saltString = new String(salt, StandardCharsets.UTF_8);
        
        String saltBegin = saltString.length() + "S";

        String saltAndHash = saltBegin + saltString + hashedPass;
        System.out.println("Salt and hashed password: " + saltAndHash);
        return saltAndHash;
    }
    
}
