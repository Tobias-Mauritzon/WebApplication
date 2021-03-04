
package com.lab3.view;

import com.lab3.model.dao.GameDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.metamodel.EntityType;
import javax.validation.constraints.*;
import jdk.jfr.Registered;
import lombok.Data;



@SessionScoped
@Named
@Data
public class CurrentGameView implements Serializable{

    private String game;
}
