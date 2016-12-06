/**
 * 
 */
package gameengine.view;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import gameengine.view.interfaces.IToolbar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Noel Moon (nm142)
 *
 */
public class Toolbar implements IToolbar {
	
	private ResourceBundle myResources;
	private HBox myToolbar;
	private EventHandler<ActionEvent> myPauseEvent;
	private EventHandler<ActionEvent> myResetEvent;
	private EventHandler<ActionEvent> myMuteEvent;
	private Button myPauseButton;
	private Button myMuteButton;
	
	public Toolbar(ResourceBundle resources, EventHandler<ActionEvent> loadLevel, EventHandler<ActionEvent> pause, 
			EventHandler<ActionEvent> reset, EventHandler<ActionEvent> mute) {
		myResources = resources;
		myPauseEvent = pause;
		myResetEvent = reset;
		myMuteEvent = mute;
		myToolbar = new HBox();
		myToolbar.setPrefHeight(40);
		addButtons();
	}

	@Override
	public HBox getToolbar() {
		return myToolbar;
	}
	
	@Override
	public void resume() {
		myPauseButton.setText(myResources.getString("PauseButton"));
	}

	@Override
	public void pause() {
		myPauseButton.setText(myResources.getString("ResumeButton"));
	}
	
	public void mute() {
		myMuteButton.setText(myResources.getString("UnmuteButton"));
	}
	
	public void unmute() {
		myMuteButton.setText(myResources.getString("MuteButton"));
	}
	
	private void addButtons() {
		Button resetButton = makeButton("ResetButton", myResetEvent);
		myMuteButton = makeButton("MuteButton", myMuteEvent);
		resetButton.setPrefWidth(75);
		myMuteButton.setPrefWidth(75);
		myToolbar.getChildren().add(resetButton);
		myToolbar.getChildren().add(myMuteButton);
	}

	private Button makeButton (String property, EventHandler<ActionEvent> handler) {
        Button result = new Button();
        String label = myResources.getString(property);
        result.setText(label);
        result.setOnAction(handler);
        result.setFocusTraversable(false);
        return result;
    }
	
}