package objects.interfaces;
import javafx.scene.input.KeyCode;
import objects.GameObject;
import objects.RandomGeneration;
import objects.ScrollType;
import objects.interfaces.ILevel;
import java.util.*;
import gameengine.model.RandomGenFrame;
public interface ILevel {
    public void setScrollType(ScrollType scrollType);
    public int getLevel();
    public void setLevel(int level) ;
    public void addGameObject(GameObject go);
    public void removeGameObject(GameObject go);
    public void addWinCondition(String type, String action);
    public void removeWinCondition(String type, String action);
    public Map<String, String> getWinConditions();
    public void addLoseCondition(String type, String action) ;
    public void removeLoseCondition(String type, String action);
    public Map<String, String> getLoseConditions();
    public List<GameObject> getGameObjects();
    public void removePlayer(GameObject player);


    // public void setControl(KeyCode key, String action);

    // public Map<KeyCode, String> getControls();


    public void setBackgroundImage(String filePath);
    public void setBackgroundMusic(String musicFilePath);


    //public void addRandomGeneration (RandomGeneration randomGeneration);
    public RandomGenFrame getRandomGenerationFrame();

    public ArrayList<RandomGeneration<Integer>> getRandomGenRules();

    public void setRandomGenerationFrame(RandomGenFrame<Integer> randomGen);
    
    public void addPlayer(GameObject player);
    

    public String getBackgroundFilePath();

    public ScrollType getScrollType();
    


    public String getMusicFilePath();



}