/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.omnifaces.util.Faces;

/**
 *
 * @author lerbyn
 */
@RequestScoped
@Named
public class GameController {
    
    public String getJavaScriptPath() {
        String str = Faces.getViewId();
        str = str.split("\\.")[0];
        str = str.substring(1);
        System.out.println("spelnamn:" + str);
        return "js/" + str + "_script.js";  
    }
}

