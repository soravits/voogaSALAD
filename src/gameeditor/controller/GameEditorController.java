package gameeditor.controller;

import java.util.HashMap;
import java.util.Map;

import gameeditor.controller.interfaces.ICreateGame;
import gameeditor.controller.interfaces.ICreateGameObject;
import gameeditor.controller.interfaces.ICreateLevel;
import gameeditor.controller.interfaces.IGameEditorController;
import gameeditor.view.GameEditorView;
import javafx.scene.Parent;
import objects.Game;
import objects.GameObject;
import objects.Level;

/**
 * This is the central class for the Game Editor backend that contains all the methods that can be called
 * by the Game Editor frontend.
 * 
 * @author Ray Song(ys101)
 *
 */
public class GameEditorController implements IGameEditorController, ICreateGame, ICreateLevel, ICreateGameObject{  
    private GameEditorView myGameEditor;
    private LevelManager myLevelManager;
    
    private Game myGame;
    private Level myCurrentLevel;
    private GameObject go;
    private Map<String, String> properties;
    
    public GameEditorController(){
    	myGameEditor = new GameEditorView();
    	myLevelManager = new LevelManager();
    }
    
    public Game getGame(){
    	return myGame;
    }
    
    public Map<String, String> getProperties(){
    	return properties;
    }

	@Override
	public void createGame(String title) {
		Game game = new Game(title);	
		myGame = game;
	}

	@Override
	public void createLevel(int levelNumber) {
		myLevelManager.createLevel(levelNumber);
		myCurrentLevel = myLevelManager.getLevel();
	}

	@Override
	public void addCurrentLevelToGame() {
		myGame.addLevel(myCurrentLevel);
	}

	@Override
	public void createGameObject(double xPos, double yPos, double width, double height, 
			String imageFileName, Map<String, String> properties) {
		GameObject go = new GameObject(xPos, yPos, width, height, imageFileName, properties);
		this.go = go;
	}

	@Override
	public void addToProperties(String key, String value) {
		if(properties==null) createProperties();
		properties.put(key, value);
	}

	@Override
	public void addCurrentGameObjectToLevel() {
		myCurrentLevel.addGameObject(go);
	}
	
	private Map<?, ?> createProperties() {
		Map<String, String> properties = new HashMap<String, String>();
		this.properties = properties;
		return properties;
	}

	@Override
	public Parent startEditor() {
		return myGameEditor.createRoot();
	}

	@Override
	public void addWinConditions(String type, String action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addLoseConditions(String type, String action) {
		// TODO Auto-generated method stub
		
	}
    
}
