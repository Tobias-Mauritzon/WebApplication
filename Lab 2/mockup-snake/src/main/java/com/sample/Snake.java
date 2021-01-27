/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample;

import java.awt.Point;
import java.util.LinkedList;
import javax.json.JsonObject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Wille
 */
public class Snake {
    private JsonObject json;
    String playerName;
    int x;
    int y;
    int xDir;
    int yDir;
    boolean hasEaten;
    boolean dead;
    LinkedList<Point> body;
   
    public Snake(String name){
        this.x = (int) Math.floor(Math.random()*21);
        this.y = (int) Math.floor(Math.random()*21);
        this.xDir = 0;
        this.yDir = 0;
        this.playerName = name;
        body = new LinkedList<>();
        body.add(new Point(x,y));
    }
    
    public void eatFruit(){
        //get bigger
    }
    
    public void dirInput(int newXDir, int newYDir){
        if(newXDir == 0 && newYDir != 0 || newXDir != 0 && newYDir == 0)
        {
            switch(newXDir){
                case -1:
                    this.xDir = -1;
                    this.yDir = 0;
                    break;
                case 1:
                    this.xDir = 1;
                    this.yDir = 0;
                    break; 
            }
            
            switch(newYDir){
                case -1:
                    this.xDir = 0;
                    this.yDir = -1;
                    break;
                case 1:
                    this.xDir = 0;
                    this.yDir = 1;
                    break; 
            }
        }
    }
    
    public void move(){
            this.body.addFirst(new Point(this.body.get(0).x + this.xDir, this.body.get(0).y + this.yDir));
            this.body.pop();
    }
    
    public String toJson(){
//        String json = new Gson().toJson(this);
        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for(Point p : this.body) {
            JsonArrayBuilder jab2 = Json.createArrayBuilder();
            jab2.add(p.x);
            jab2.add(p.y);
            jab.add(jab2);
        }
//        String s = "{\"name\":" + playerName + ", \"coords\": {\"x\": " + x + ", \"y\": " + y + "}, \"directions\": {\"xDir\": " + xDir + ", \"yDir\": " + yDir + "}}";
        System.out.println(jab.toString());
        return jab.toString();
    }
}
