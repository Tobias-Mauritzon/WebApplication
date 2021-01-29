
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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
        
        String dirName = "../applications/__internal/login-1.0-SNAPSHOT/";

        Files.list(new File(dirName).toPath())
                .limit(20)
                .forEach(path -> {
                    System.out.println(path);
                });
       
        try( FileInputStream serviceAccount = new FileInputStream("../resources/application-firebase-admin.json");){

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
