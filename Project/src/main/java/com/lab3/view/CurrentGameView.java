
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
public class CurrentGameView implements Serializable{
    private String game;
}
