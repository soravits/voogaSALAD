package gameengine.view.interfaces;

import frontend.util.ButtonTemplate;
import gameengine.controller.interfaces.CommandInterface;
import general.NodeFactory;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import viewformatter.ViewFormatter;
import side.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Delia on 12/11/2016.
 */
public abstract class ScoreScreen {

    private static final String COVER_SPLASH_STYLE = "default.css";
    protected double myAppWidth = 400;
    protected double myAppHeight = 340;
    protected NodeFactory myFactory = new NodeFactory();
    private Scene myScene;
    private IGameEngineUI myGameEngine;
    private int myTime;
    private Map<Long, Integer> myScoreMapping;
    private List<Integer> highScores;
    private BorderPane root;

    public ScoreScreen(List<Integer> highScores, int time, Map<Long,
            Integer> scoreMapping, IGameEngineUI iGameEngine) {
        this.highScores = highScores;
        this.myTime = time;
        this.myScoreMapping = scoreMapping;
        this.myGameEngine = iGameEngine;
        this.myScene = new Scene(makeRoot(), myAppWidth, myAppHeight);
        myScene.getStylesheets().add(COVER_SPLASH_STYLE);
    }

    private BorderPane makeRoot() {
        root = new BorderPane();
//        Text score = new Text(50, 50, "Your Score: " + Integer.toString(myLevel.getScore()));
//        score.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        ViewFormatter formatter = new ViewFormatter();

        Text highScoreText = new Text (makeScreenText() + " \nHigh Scores");
        highScoreText.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        highScoreText.setFill(Color.RED);
        formatter.addView(highScoreText, "High Score Text")
        	.setXAsFractionOfWidth(.05, "Back Drop")
        	.setYAsFractionOfHeight(.15, "Back Drop")
        	.centerXInScreen();
        
        ImageView background = makeBackground();
        background.setFitHeight(myAppHeight);
        background.setFitWidth(myAppWidth);
        formatter.addView(background,"Background")
        		.setZ(-1);
        
        Rectangle backdrop = myFactory.makeBackdrop(20, 20, 350, 300, Color.WHITE);
        backdrop.setOnMouseClicked(e -> {
            getMyGameEngine().getMyLevelStage().close();
            getMyGameEngine().pause();
        });
        root = new BorderPane();
//        Text score = new Text(50, 50, "Your Score: " + Integer.toString(myLevel.getScore()));
//        score.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        highScoreText.setFill(Color.RED);
        highScoreText.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        root.getChildren().addAll(background, backdrop, highScoreText);
        formatter.addView(backdrop,"Back Drop");
        
        VBox vbox = new VBox();
        int index = 0;
        for (Integer highScore : highScores) {

            Text text = new Text (50, 120 + index * 20, " " + (index + 1) + ".\t" + Integer.toString(highScore));
            text.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            text.setFill(Color.BLACK);
            vbox.getChildren().add(text);
            index++;
        }
        formatter.addView(vbox,"High Scores")
        	.position(Side.BOTTOM, "High Score Text", 10);
        
        root.getChildren().add(formatter.renderView());
        return root;
    }

    public Scene getScene(){
        return myScene;
    }

    public abstract String getStageTitle();

    protected abstract String makeScreenText();

    protected abstract void addButtons();

    protected abstract ImageView makeBackground();

    protected IGameEngineUI getMyGameEngine(){
        return myGameEngine;
    }

    protected BorderPane getRoot(){
        return root;
    }
}

