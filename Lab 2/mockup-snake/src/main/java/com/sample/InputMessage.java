package com.sample;

import java.awt.Point;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lerbyn
 */
public class InputMessage {
    String playerName;
    Point p;
    public InputMessage(String playerName, int x, int y){
        this.playerName = playerName;
        this.p = new Point(x,y);
    }
}
