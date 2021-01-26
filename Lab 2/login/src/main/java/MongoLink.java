
import com.mongodb.*;
import java.net.UnknownHostException;

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
        System.out.println(System.getenv("Mongo_connection_login"));
        MongoClientURI uri = new MongoClientURI(
        System.getenv("Mongo_connection_login"));

        MongoClient mongoClient = new MongoClient(uri);
        DB database = mongoClient.getDB("MongoLogin");

        User user = new User(UserName, Password);
        DBObject doc = createDBObject(user);
        
        DBCollection col = database.getCollection("Cred");
        
        WriteResult result = col.insert(doc);
        System.out.println(result.getN());

        
        
        return result.getN() + "";
    }
    
    private static DBObject createDBObject(User user) {
		BasicDBObjectBuilder docBuilder = BasicDBObjectBuilder.start();
								
		docBuilder.append("_id", user.getId());
		docBuilder.append("name", user.getName());
		docBuilder.append("role", user.getPass());
		return docBuilder.get();
	}
}
