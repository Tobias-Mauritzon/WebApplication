
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tobias
 */


//@WebServlet("/GetUserServlet")

@WebServlet("/LoginWithMongo")
public class LoginWithMongo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userName = request.getParameter("userName").trim();
                String passWord = request.getParameter("passWord").trim();
		if(userName == null || "".equals(userName)){
			userName = "Guest";
		}
		
                if(passWord == null || "".equals(passWord)){
			passWord = "Guest";
		}
                
                System.out.println("\n"+System.getProperty("java.version")+"\n");
                
                
		String greetings = "Hello " + userName + " Pass: "+ passWord +" From BD:";
		//MongoLink m = new MongoLink();
                //String res = m.login(userName, passWord);
                
                LoginFirebase l = new LoginFirebase();
                String res = "fail";
                
                try {
                    res = l.login();
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoginWithMongo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(LoginWithMongo.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                greetings = greetings + " "+ res;
                
		response.setContentType("text/plain");
		response.getWriter().write(greetings);
	}

}