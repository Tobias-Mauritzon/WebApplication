package com.awesomeGames.view;

import com.awesomeGames.model.entity.HighScore;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Data;

/**
 * View class for scores on user page
 * @author Joachim Antfolk
 */
@ViewScoped
@Named
@Data
public class UserScoresView implements Serializable{
    private List<HighScore> scoreList;
}
