/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
package com.sample;

import java.awt.Point;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Wille
 *//*
@ServerEndpoint(value="/snakeEndpoint", encoders = {SnakeEncoder.class}, decoders = {SnakeDecoder.class})
public class SnakeEndpoint{
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    
    private Map<Session, Snake> snakes = new HashMap<>();
    private boolean gameRunning = false;
    private int fruitX;
    private int fruitY;
    
    
    @OnMessage
    public void snakeInput(Point dir, Session session) throws IOException, EncodeException {
        
        snakes.get(session).dirInput(dir.x, dir.y);
    }
    
    
    
//    @OnMessage
//    public void broadcastCount(int tick, Session session) throws IOException, EncodeException {
//        System.out.println("broadcastCount: " + tick);
//        for (Session peer : peers) {
//            if (!peer.equals(session)) {
//                //peer.getBasicRemote().sendObject(count);
//            }
//        }
//    }
    
    private void eatFruit(){
        fruitX = (int) (Math.floor(Math.random() * 21)*10);
        fruitY = (int) (Math.floor(Math.random() * 21)*10);
    }
    
    private void getDirection(){
        
    }
    
    private void update(){
        
        for(Snake s : snakes.values()){
            s.move();
            if (fruitX == s.x && fruitY == s.y) {

                eatFruit();
                s.eatFruit();
            }
            
        }
        
        
        
    }
    
    private void broadcast(){
        
    }
    
    private void startGame(){
        gameRunning = true;
        
        int UPS = 60;
        int FPS = 60;
        
        long initialTime = System.nanoTime();
        final double timeU = 1000000000 / UPS;
        final double timeF = 1000000000 / FPS;
        double deltaU = 0, deltaF = 0;
        int frames = 0, ticks = 0;
        long timer = System.currentTimeMillis();

        while (gameRunning) {

            long currentTime = System.nanoTime();
            deltaU += (currentTime - initialTime) / timeU;
            deltaF += (currentTime - initialTime) / timeF;
            initialTime = currentTime;

            if (deltaU >= 1) {
                getDirection();
                update();
                ticks++;
                deltaU--;
            }

            if (deltaF >= 1) {
//                render();
                broadcast();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
//                if (RENDER_TIME) {
//                    System.out.println(String.format("UPS: %s, FPS: %s", ticks, frames));
//                }
                frames = 0;
                ticks = 0;
                timer += 1000;
            }
        }
        
    }  

    @OnOpen
    public void onOpen (Session peer) {
        peers.add(peer);
        if(!gameRunning && peers.size() > 1){
            startGame();
        }
    }

    @OnClose
    public void onClose (Session peer) {
        peers.remove(peer);
    }
    
}
*/