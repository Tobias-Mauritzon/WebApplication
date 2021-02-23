/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab3.view;

import com.lab3.model.entity.UserAccount;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Tobias
 */
@ViewScoped
@Named
@Data
public class CommentView implements Serializable{
    private String text;
    private String game = "rotation";   // This should be grabebd on page load
    private UserAccount userAccount = new UserAccount("mail1@gmail.com", "namaefe1", "passworfed1"); // This should be grabbed when you make a comment
}
