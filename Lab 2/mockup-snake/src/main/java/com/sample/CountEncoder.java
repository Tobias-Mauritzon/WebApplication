/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample;

import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author wille
 */
public class CountEncoder implements Encoder.Text<Integer>{

    @Override
    public String encode(Integer t) throws EncodeException {
        System.out.println("encoding");
        JsonObject object = Json.createObjectBuilder().add("count", t).build();
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
