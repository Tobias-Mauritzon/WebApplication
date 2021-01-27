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
        
        
        
        
        long lastTime = System.nanoTime();
        double amountOfTicks = 20.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        for(Snake s: snakes.values()) {
            s.addBodyPart();
            s.addBodyPart();
            s.addBodyPart();
        }
        se.broadcast(snakes.keySet());
        while(running) {
            
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                for(Snake s: snakes.values()) {
                    s.move();
                }
                delta--;
            }
            se.broadcast(snakes.keySet());
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
    } 
}
