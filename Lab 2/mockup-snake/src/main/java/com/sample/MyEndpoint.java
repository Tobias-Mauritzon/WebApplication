/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
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
 */
@ServerEndpoint(value="/endpoint", encoders = {CountEncoder.class}, decoders = {CountDecoder.class})
public class MyEndpoint {
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    private static int count = 0;
    
    /*@OnMessage
    public String onMessage(String message) {
        System.out.println("WTF");
        return null;
    }*/
    
    public void broadcastCount(int tick, Session session) throws IOException, EncodeException {
        System.out.println("broadcastCount: " + tick);
        count += tick;
        for (Session peer : peers) {
            if (!peer.equals(session)) {
                peer.getBasicRemote().sendObject(count);
            }
        }
    }
    

    @OnOpen
    public void onOpen (Session peer) {
        peers.add(peer);
    }

    @OnClose
    public void onClose (Session peer) {
        peers.remove(peer);
    }
    
}
