/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author Wille
 */
public class SnakeEncoder implements Encoder.Text<Snake[]>{

    @Override
    public String encode(Snake[] snakes) throws EncodeException {
        System.out.println("encoding");
        JsonObjectBuilder job = Json.createObjectBuilder();
        for(Snake s : snakes){
            
            job.add("snake", s.toJson());
        }
        JsonObject object = job.build();
        String jsonAsString = object.toString();
        return jsonAsString;
    }

    @Override
    public void init(EndpointConfig ec) {
        System.out.println("init");
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
    
}
