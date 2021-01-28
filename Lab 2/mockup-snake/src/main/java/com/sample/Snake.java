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
    int xDir;
    int yDir;
    boolean dirChange;
    LinkedList<Point> body;
    Point fruit;
   
    public Snake(String name){
        this.xDir = 1;
        this.yDir = 0;
        this.playerName = name;
        body = new LinkedList<>();
        body.add(new Point((int) Math.floor(Math.random()*21),(int) Math.floor(Math.random()*21)));
        this.dirChange = false;
    }
    
    public void changeDir(int newXDir, int newYDir){
        if((newXDir == 0 && newYDir != 0 || newXDir != 0 && newYDir == 0) && !dirChange)
        {
            if(this.xDir == 0) {
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
            }
            
            if(this.yDir == 0) {
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
            dirChange = true;
        }
    }
    
    public void move(){
            //System.out.println("move: " + this.xDir + " " + this.yDir);
            this.body.addFirst(new Point(this.body.get(0).x + this.xDir, this.body.get(0).y + this.yDir));
            this.body.pollLast();
            dirChange = false;
    }
    
//    public String toJson(){
//        JsonObjectBuilder job = Json.createObjectBuilder();
//        JsonArrayBuilder jab = Json.createArrayBuilder();
//        for(Point p : this.body) {
//            JsonArrayBuilder jab2 = Json.createArrayBuilder();
//            jab2.add(p.x);
//            jab2.add(p.y);
//            jab.add(jab2);
//        }
//        return jab.toString();
//    }

    void addBodyPart() {
        int length = this.body.size();
        this.body.add(new Point(this.body.get(length-1).x,this.body.get(length-1).y));
    }

    void addFruit(Point fruit) {
        this.fruit = fruit;
    }
}
