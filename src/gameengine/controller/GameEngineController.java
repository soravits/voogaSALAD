package gameengine.controller;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import com.sun.javafx.scene.traversal.Direction;
import gameengine.controller.interfaces.CommandInterface;
import gameengine.controller.interfaces.RGInterface;
import gameengine.controller.interfaces.RuleActionHandler;
import gameengine.model.*;
import gameengine.model.interfaces.Scrolling;
import gameengine.view.GameEngineUI;
import gameengine.view.HighScoreScreen;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Duration;
import objects.*;

import javax.swing.*;

/**
 * @author Soravit Sophastienphong, Eric Song, Brian Zhou, Chalena Scholl, Noel
 *         Moon
 */

public class GameEngineController implements RuleActionHandler, RGInterface, CommandInterface {
    public static final double FRAMES_PER_SECOND = 30;
    public static final double MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1 / FRAMES_PER_SECOND;

	private List<RandomGenFrame> RGFrames;
    private List<Integer> highScores;
    private String xmlData;
	private GameParser parser;
	private CollisionChecker collisionChecker;
	private MovementChecker movementChecker;
	private Game currentGame;
	private GameEngineUI gameEngineView;
	private Timeline animation;
	private MovementController movementController;
	private Scrolling gameScrolling;

	public GameEngineController() {
		parser = new GameParser();
		collisionChecker = new CollisionChecker(this);
		movementChecker = new MovementChecker();
		movementController = new MovementController(this);
		gameEngineView = new GameEngineUI(movementController, event -> reset());
		RGFrames = new ArrayList<>();
        highScores = new ArrayList<>();
    }

    public Scene getScene() {
        return gameEngineView.getScene();
    }

	public boolean startGame(String xmlData) {
        this.xmlData = xmlData;
		currentGame = parser.convertXMLtoGame(xmlData);
        if(currentGame.getCurrentLevel() == null || currentGame.getCurrentLevel().getMainCharacter() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Cannot start game.");
            alert.setContentText("You must create a level with a main character to start a game.");
            alert.showAndWait();
            return false;
        }
		movementController.setGame(currentGame);
        gameEngineView.initLevel(currentGame.getCurrentLevel());
		gameEngineView.mapKeys(currentGame.getCurrentLevel().getControls());
        addRGFrames();
        setScrolling();
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
			try {
				updateGame();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		});
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
        return true;
	}

	/**
	 * Applies gravity and scrolls, checks for collisions
	 *
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void updateGame() throws ClassNotFoundException, InstantiationException, IllegalArgumentException,
			InvocationTargetException, IllegalAccessException, NoSuchMethodException, SecurityException {
		GameObject mainChar = currentGame.getCurrentLevel().getMainCharacter();
		gameScrolling.scrollScreen(currentGame.getCurrentLevel().getGameObjects(), mainChar);
        if(currentGame.getCurrentLevel().getScrollType().getScrollType().equals("ForcedScrolling")) {
            removeOffscreenElements();
        }
		gameEngineView.update(currentGame.getCurrentLevel());
		movementChecker.updateMovement(currentGame.getCurrentLevel().getGameObjects());
		for(RandomGenFrame elem: RGFrames){
            for(RandomGeneration randomGeneration : currentGame.getCurrentLevel().getRandomGenRules()) {
                elem.possiblyGenerateNewFrame(100, randomGeneration, this.getClass().getMethod("setNewBenchmark"));
            }
		}
        Level currLevel = currentGame.getCurrentLevel();
        collisionChecker.checkCollisions(mainChar, currLevel.getGameObjects());
		 LossChecker.checkLossConditions(this,
		 currentGame.getCurrentLevel().getLoseConditions(), currLevel.getGameConditions());
		 WinChecker.checkWinConditions(this,
		 currLevel.getWinConditions(), currLevel.getGameConditions());
	}

    public void setNewBenchmark() {
        List<GameObject> objects = currentGame.getCurrentLevel().getGameObjects();
        for(RandomGenFrame elem: RGFrames){
            elem.setNewBenchmark(new Integer((int) objects.get(objects.size() - 1).getXPosition() / 2));
        }
    }

    @Override
    public void removeObject(GameObject obj) {
        currentGame.getCurrentLevel().removeGameObject(obj);
    }

    @Override
    public void endGame() {
        addHighScore(currentGame.getCurrentLevel().getScore());
        animation.stop();
        HighScoreScreen splash = new HighScoreScreen(currentGame.getCurrentLevel(),
                highScores, this);
        Stage stage = new Stage();
        stage.setScene(splash.getScene());
        stage.getScene().getStylesheets().add("gameEditorSplash.css");
        stage.setTitle("GAME OVER");
        stage.show();
    }

    @Override
    public void reset() {
        animation.stop();
        gameEngineView.resetGameScreen();
        startGame(xmlData);
    }

    public void stop(){
        gameEngineView.stopMusic();
        animation.stop();
    }

    private void addHighScore(int score) {
        if (highScores.size() == 0) {
            highScores.add(score);
        } else if (highScores.size() < 5) {
            highScores.add(score);
            Collections.sort(highScores);
            Collections.reverse(highScores);
        } else {
            Integer lowestHighScore = highScores.get(4);
            if (score > lowestHighScore) {
                highScores.remove(lowestHighScore);
                highScores.add(score);
                Collections.sort(highScores);
                Collections.reverse(highScores);
            }
        }
    }

    private void addRGFrames(){
        List<RandomGeneration> randomGenerations = currentGame.getCurrentLevel().getRandomGenRules();
        for (RandomGeneration randomGeneration : randomGenerations) {
            RGFrames.add(new RandomGenFrame(this, 300, currentGame.getCurrentLevel()));
        }
    }

	private void removeOffscreenElements() {
		List<GameObject> objects = currentGame.getCurrentLevel().getGameObjects();
		if(objects.size() == 0 || objects == null) return;
		for(int i= objects.size()-1; i >= 0; i--){
			if(objects.get(i).getXPosition()> -(2*GameEngineUI.myAppWidth) || objects.get(i) == null) continue;//CHANGE THIS TO PIPE WIDTH
            gameEngineView.removeObject(objects.get(i));
            objects.remove(i);
		}
	}

	@Override
	public void modifyScore(int score) {
		int prevScore = currentGame.getCurrentLevel().getScore();
		int currScore = prevScore+score;
		currentGame.getCurrentLevel().setScore(currScore);
	}
	
	private void setScrolling(){
		ScrollType gameScroll = currentGame.getCurrentLevel().getScrollType();
		Class<?> cl = null;
		try {
			cl = Class.forName("gameengine.scrolling." + gameScroll.getScrollType());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Constructor<?> cons = null;
		try {
			cons = cl.getConstructor(Direction.class, double.class, double.class, double.class);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
 			gameScrolling = (Scrolling) cons.newInstance(gameScroll.getDirections().get(0), 
 					 
 						currentGame.getCurrentLevel().getGameConditions().get("scrollspeed"),
 
 						gameEngineView.getScreenWidth(), gameEngineView.getScreenHeight()); 

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}