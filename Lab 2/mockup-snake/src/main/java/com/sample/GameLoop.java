/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample;

import java.awt.Point;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.Session;

/**
 *
 * @author lerbyn
 */
public class GameLoop implements Runnable {

    private boolean running = true;
    private ConcurrentHashMap<Session, Snake> snakes;
    private SnakeEndpoint se;
    //private Point fruit;
    private boolean fruitEaten;
    
    public GameLoop(ConcurrentHashMap<Session, Snake> snakes, SnakeEndpoint se) {
        this.snakes = snakes;
        this.se = se;
        spawnFruit();
        fruitEaten = false;
        //System.out.println("start gameloop");
    }
    
    private void spawnFruit() {
        Point fruit = new Point((int) Math.floor(Math.random()*21),(int) Math.floor(Math.random()*21));
        for(Snake s: snakes.values()) {
            s.addFruit(fruit);
        }
    }
    
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 8.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        for(Snake s: snakes.values()) {
            s.addBodyPart();
            s.addBodyPart();
            s.addBodyPart();
        }
        //System.out.println(snakes);
        se.broadcast(snakes.keySet());
        while(running) {
            
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                for(Snake s: snakes.values()) {
                    s.move();
                    if(s.body.get(0).x == s.fruit.x && s.body.get(0).y == s.fruit.y && !fruitEaten) {
                        s.addBodyPart();
                        fruitEaten = true;
                    }
                    //System.out.println("game tick");
                }
                se.broadcast(snakes.keySet());
                if(fruitEaten) {
                    spawnFruit();
                    fruitEaten = false; 
                }
                delta--;
                
            }
            
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
    } 
}
