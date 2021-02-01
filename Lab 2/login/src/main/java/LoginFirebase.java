
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

    public String login() throws FileNotFoundException, IOException, InterruptedException, ExecutionException{
        
        String res = "fail";
        
        //JSONObject json = new JSONObject(conString);  
        
        Firestore db;
       
        String conString =  System.getenv("fire_json2");
        
        
        System.out.println(conString);
        
        try( FileInputStream serviceAccount = new FileInputStream(conString)){

         
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();
            FirebaseApp.initializeApp(options);

            db = FirestoreClient.getFirestore();

            /*
            DocumentReference docRef = db.collection("users").document();
            // Add document data  with default id using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("Name", "Ada");
            data.put("Password", "Lovelace");
            data.put("Mail", "ada@mailk.com");
            //asynchronously write data
            ApiFuture<WriteResult> result = docRef.set(data);
            // ...
            // result.get() blocks on response
            System.out.println("Update time : " + result.get().getUpdateTime());
            */
            res = "Ok";
        }
        return res;
    }

}
