/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sample;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

/**
 *
 * @author Wille
 */
public class SnakeEncoder implements Encoder.Text<ConcurrentHashMap<Session, Snake>>{


    @Override
    public void init(EndpointConfig ec) {
        System.out.println("init");
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }   

    @Override
    public String encode(ConcurrentHashMap<Session, Snake> snakes) throws EncodeException {
        //System.out.println("encoding");
        JsonObjectBuilder job = Json.createObjectBuilder();
        String json = "{ \"name\":\"hej\" }";
        //StringBuilder sb = new StringBuilder();
        List<String> snakesJson = new ArrayList<>();
        
        boolean first = true;
        for(Snake s : snakes.values()){
            json = new Gson().toJson(s);
            snakesJson.add(json);
//            if(first) {
//                sb.
//                sb.append(json);
//                first = false;
//            } else {
//                sb.append("," + json);
//            }  
            //System.out.println(json);
        }
        json = new Gson().toJson(snakesJson);
        //System.out.println(json);
        //System.out.println("json");
//        JsonObject object = job.build();
//        String jsonAsString = object.toString();
//        Gson gson = new Gson();
        return json;
    }

}
