
import com.mongodb.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tobias
 */
class MongoLink {
    
    public String login(){
        MongoClientURI uri = new MongoClientURI(
        System.getenv("Mongo_connection_login"));

        MongoClient mongoClient = new MongoClient(uri);
        DB database = mongoClient.getDB("test");

        User user = new User();
        return "";
    }
    

}
