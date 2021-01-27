/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sample;

import java.awt.Point;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Wille
 */
@ServerEndpoint(value="/endpoint", encoders = {SnakeEncoder.class}, decoders = {SnakeDecoder.class})//,JoinGameDecoder.class})
public class SnakeEndpoint{
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    
    private Map<String, Snake> snakes = new HashMap<>();
    private boolean gameRunning = false;
    private int fruitX;
    private int fruitY;
    private int players = 0;
    
    
    @OnMessage
    public void onMessage(InputMessage im, Session session) {
        
        if(!snakes.containsKey(im.playerName)) {
            System.out.println("player added");
            snakes.put(im.playerName,new Snake(im.playerName));
            players++;
        } else {
            snakes.get(im.playerName).dirInput(im.p.x, im.p.y);
            System.out.println("direction added");
        }
        if(players > 0) {
            
        }
    }
    
//    @OnMessage
//    public void onMessage(Point dir, Session session) {
//       
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
        for(Session p: peers) {
            try {
                System.out.println("broadcast");
                //p.getBasicRemote().
                p.getBasicRemote().sendObject(snakes);
            } catch (IOException | EncodeException ex) {
                Logger.getLogger(SnakeEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void startGame(){
        gameRunning = true;
        
        broadcast();
//        while (gameRunning) {
//               
//            
//        }
        
    }  

    @OnOpen
    public void onOpen (Session peer) throws IOException, EncodeException {
        peers.add(peer);
        peer.getBasicRemote().sendObject("Connection-established");
        //if(!gameRunning && peers.size() > 1){
        //}
      
    }

    @OnClose
    public void onClose (Session peer) {
        peers.remove(peer);
    }
    
}
