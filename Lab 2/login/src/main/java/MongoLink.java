

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClients;
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
    
    public String login(String UserName, String Password){
        
        User user = new User(UserName, Password);
        Document doc = createDBObject(user);
        
        
        String res = "fail";
     
       
        String conString =  System.getenv("Mongo_connection_login");
        
        try(MongoClient client = MongoClients.create(conString)){
            
            MongoDatabase db = client.getDatabase("MongoLogin");
            
            String name = db.getName();
            
            //MongoCollection col = database.getCollection("Cred");
            
            //col.insertOne(doc);
            
            
            res = name;
        }
        
        

       

        return res;
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
