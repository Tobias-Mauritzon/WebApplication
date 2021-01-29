
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
        String conString =  System.getenv("Firebase_login");
        
        System.out.println(conString);
        
        //JSONObject json = new JSONObject(conString);  
        
        try( FileInputStream serviceAccount = new FileInputStream("conString");){

            FirebaseOptions options = new FirebaseOptions.Builder()
              .setCredentials(GoogleCredentials.fromStream(serviceAccount))
              .setDatabaseUrl("https://webb-application-default-rtdb.firebaseio.com")
              .build();

            FirebaseApp.initializeApp(options); 
            
            res = "Ok";
        }
        return res;
    }
    

}
