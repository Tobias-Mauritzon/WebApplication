
package com.lab3.view;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.*;
import jdk.jfr.Registered;
import lombok.Data;



@ViewScoped
@Named
@Data
public class CreateUserView implements Serializable{
    @Size(min = 2,max = 30) private String userName;
    @Size(min = 8,max = 20) private String password;
    @Size(min = 8,max = 20) private String confirmPassword;
    @Email private String mail;
}
