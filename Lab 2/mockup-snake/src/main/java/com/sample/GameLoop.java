/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;

/**
 *
 * @author lerbyn
 */
public class GameLoop implements Runnable {

    private boolean running = true;
    private Map<Session, Snake> snakes;
    private SnakeEndpoint se;
    
    public GameLoop(Map<Session, Snake> snakes, SnakeEndpoint se) {
        this.snakes = snakes;
        this.se = se;
        System.out.println("start gameloop");
    }
    
    @Override
    public void run() {
        while(running) {
            se.broadcast(snakes.keySet());
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
