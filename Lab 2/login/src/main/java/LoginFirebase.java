
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tobias
 */
public class LoginFirebase {

    public String login() throws FileNotFoundException, IOException{
        
        String res = "fail";
        
        //JSONObject json = new JSONObject(conString);  
        
        FirebaseDatabase db;
       
        String conString =  System.getenv("fire_json2");
        
        
        System.out.println(conString);
        
        try( FileInputStream serviceAccount = new FileInputStream(conString)){

            FirebaseOptions options = new FirebaseOptions.Builder()
              .setCredentials(GoogleCredentials.fromStream(serviceAccount))
              .setDatabaseUrl("https://webb-application-default-rtdb.firebaseio.com")
              .build();

            FirebaseApp.initializeApp(options); 
            
            db = FirebaseDatabase.getInstance();
            
            res = "Ok";
        }
        return res;
    }

}
