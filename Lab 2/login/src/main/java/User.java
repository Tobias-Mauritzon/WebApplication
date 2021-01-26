/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tobias
 */
class User {
    private int id;
    private String name;
    private String pass;
    
    public User(String userName, String password){
        this.id = 2;
        this.name = userName;
        this.pass = password;
    }
	
    public int getId() {
            return id;
    }
    public void setId(int id) {
            this.id = id;
    }
    public String getName() {
            return name;
    }
    public void setName(String name) {
            this.name = name;
    }
    public String getPass() {
            return pass;
    }
    public void setPass(String pass) {
            this.pass = pass;
    }
  
}
