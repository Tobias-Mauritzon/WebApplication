/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author wille
 */
public class CountDecoder implements Decoder.Text<Integer>{

    @Override
    public Integer decode(String string) throws DecodeException {
        System.out.println("de: " + string);
        
        JsonObject jsonObject = Json.createReader(new StringReader(string)).readObject();
        System.out.println("de: " + jsonObject);
        return  jsonObject.getInt("count");
    }

    @Override
    public boolean willDecode(String string) {
       try {
           Json.createReader(new StringReader(string)).readObject();
           return true;
       }
       catch (JsonException ex){
           ex.printStackTrace();
           return false;
       }
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
