

import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.net.UnknownHostException;
import org.bson.Document;
import org.bson.types.ObjectId;

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
    
    public String login(String UserName, String Password) throws UnknownHostException{
        String con = System.getenv("Mongo_connection_login");
        System.out.println(con);        
        
        MongoClientURI uri = new MongoClientURI(
            System.getenv("Mongo_connection_login2"));

        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("MongoLogin");

        //MongoClient mongoClient = new MongoClient(uri);
        //DB database = mongoClient.getDB("MongoLogin");

        User user = new User(UserName, Password);
        Document doc = createDBObject(user);
        
        MongoCollection col = database.getCollection("Cred");
        
        System.out.println(database.getName());
        
        
        col.insertOne(doc);

        return "Inserted";
    }
    
    private static Document createDBObject(User user) {
		//BasicDBObjectBuilder docBuilder = BasicDBObjectBuilder.start();
		
                Document userDoc = new Document("_id", new ObjectId());
                userDoc.append("bad_id", user.getId());
		userDoc.append("name", user.getName());
		userDoc.append("role", user.getPass());
                					
		//docBuilder.append("bad_id", user.getId());
		//docBuilder.append("name", user.getName());
		//docBuilder.append("role", user.getPass());
                
		return userDoc;
	}
}
