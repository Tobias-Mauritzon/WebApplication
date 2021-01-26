
import java.io.IOException;
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
                
		String greetings = "Hello " + userName + " Pass: "+ passWord;
		
		response.setContentType("text/plain");
		response.getWriter().write(greetings);
	}

}