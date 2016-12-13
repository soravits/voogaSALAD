/**
 *
 */
package gameengine.view;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import gameengine.view.interfaces.IHUD;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import objects.Game;
import objects.Level;

/**
 * @author Noel Moon (nm142)
 *
 *
 * @citation http://stackoverflow.com/questions/9966136/javafx-periodic-background-task
 */
public class HUD {

	private HBox myHUD;
	private Timeline timer;
	private int timeCount;

	public HUD() {
		timeCount = 0;
		timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> incrementTime()));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
		myHUD = new HBox();
	}

	public HBox getHUD() {
		return myHUD;
	}

	public void update(Map<Long, Integer> scores) {
		myHUD.getChildren().clear();
		//game.getCurrentLevel().setTime(timeCount);
		for (Long clientID : scores.keySet()) {
			Text scoreText = new Text("   Player " + Long.toString((clientID+1)) + ": " + Integer.toString(scores.get(clientID)));
			myHUD.getChildren().add(scoreText);
		}
//		if(game.getGameConditions().get("score") != null) {
//			Text scoreText = new Text("Score: " + Integer.toString(level.getScore()));
//			myHUD.getChildren().add(scoreText);
//		}
//		if(level.getPlayers() != null) {
//			Text healthText = new Text("  |  Health: " + level.getPlayers().get(0).getProperty("health"));
//            myHUD.getChildren().add(healthText);
//        }
		Text timeText = new Text("  |  Time: " + Integer.toString(timeCount));
		myHUD.getChildren().add(timeText);
	}

	public void resetTimer() {
		timeCount = 0;
	}

	private void incrementTime() {
		timeCount++;
	}
}