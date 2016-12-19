package gameeditor.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.text.html.ObjectView;
import frontend.util.FileOpener;
import frontend.util.GameEditorException;
import gameeditor.commanddetails.DetailResources;
import gameeditor.controller.GameEditorData;
import gameeditor.controller.interfaces.IGameEditorData;
import gameeditor.objects.GameObjectView;
import gameeditor.rpg.GridDesignArea;
import gameeditor.view.interfaces.IDesignArea;
import gameeditor.view.interfaces.IDetailPane;
import gameeditor.view.interfaces.IEditorToolbar;
import gameeditor.view.interfaces.IGameEditorView;
import gameeditor.view.interfaces.IToolbarParent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import objects.GameObject;
import objects.Player;
import objects.interfaces.IGame;
import objects.interfaces.ILevel;
/**
 * @author pratikshasharma, John, Delia
 */
public class GameEditorView implements IGameEditorView, IToolbarParent {
    private static final String IMAGE_FILE_TYPE = ViewResources.IMAGE_FILE_TYPE.getResource();
    private static final String MUSIC_FILE_TYPE = ViewResources.MUSIC_FILE_TYPE.getResource();
    private static final String BG_IMAGE_LOCATION = ViewResources.BG_FILE_LOCATION.getResource();
    private static final String AVATAR_IMAGE_LOCATION = ViewResources.AVATAR_IMAGE_LOCATION.getResource();
    private static final String MUSIC_FILE_LOCATION = ViewResources.MUSIC_FILE_LOCATION.getResource();
    private static final String BACKGROUND_IMAGE_ID = ViewResources.BACKGROUND_IMAGE_ID.getResource();
    private static final String FILE_PREFIX = ViewResources.FILE_PREFIX.getResource();
    private static final String IMAGES_LOCATION = ViewResources.IMAGES_LOCATION.getResource();
    public static final  double SCENE_WIDTH = ViewResources.SCENE_WIDTH.getDoubleResource();
    public static final double SCENE_HEIGHT = ViewResources.SCENE_HEIGHT.getDoubleResource();
    public static final String MAIN_CHAR="MainCharacter";

    private BorderPane myRoot;
    private ScrollPane myScrollPane;
    private HBox myLeftBox;
    private VBox myCenterBox;
    private IEditorToolbar myToolbar;
    private CommandPane myCommandPane;
    private IDesignArea myDesignArea;
    private IGameEditorData myDataStoreInterface;
    private IDetailPane myDetailPane;
    private ILevel myLevelSettings;
    private IGame myGameInterface;
    private BooleanProperty closeLevelWindow;
    public static final String DEFAULT_MAIN_CHARACTER = "bird2.gif";
    public static final String SCORE_PROPERTY="score";
    private String myGameType;

    public GameEditorView(ILevel levelSettings, IGame myGameInterface, String gameType){
        this.myLevelSettings = levelSettings;
        this.myGameInterface = myGameInterface;
        myGameType = gameType;
        myRoot = new BorderPane();  
        closeLevelWindow = new SimpleBooleanProperty(false);
    }

    public Parent createRoot(){
        myRoot.setCenter(createCenter());
        myRoot.setLeft(createLeftAlt());
        addStuffFromOtherFiles();
        return myRoot;
    }

    public void addStuffFromOtherFiles(){
        // try{ 
        addBackground();
        addAvatar();
        addSprites();
        //}catch(NullPointerException e){
        //GameEditorException exception = new GameEditorException();
        //exception.showError(e.getMessage());
        // }
    }

    private void addSprites(){
        if(myLevelSettings.getGameObjects().size()>0){
            for(GameObject object: myLevelSettings.getGameObjects()){
                double height = object.getHeight();
                double width = object.getWidth();
                double x=object.getXPosition();
                double y = object.getYPosition();
                String type = object.getTypeName();
                String userDirectoryString = "file:" + System.getProperty("user.dir") + "/images/";
                String fileName = userDirectoryString+"Sprite/" + object.getImageFileName();

                Map<String,String> propertiesMap = new HashMap<String,String>();
                propertiesMap.put(DetailResources.IMAGE_PATH.getResource(), fileName);
                propertiesMap.putAll(object.getProperties());
                // Store type in data storage
                if(myDataStoreInterface.getType(object.getTypeName())==null){
                    myDataStoreInterface.storeType(propertiesMap);
                }
                ArrayList<GameObjectView> myAvatars =  myDetailPane.getCurrentAvatars();
                //                System.out.println(myAvatars.size());

                GameObjectView objectView = new GameObjectView(fileName,x,y,width,height,type,false,false,myDesignArea,myDataStoreInterface);

                myDesignArea.addSprite(objectView);
            } 
        }
    }


    private HBox createLeftAlt(){
        myDataStoreInterface = new GameEditorData(myLevelSettings, myGameInterface);
        DetailPane dp = new DetailPane(myDesignArea, myDataStoreInterface);
        myDetailPane = dp;
        myCommandPane = new CommandPane(dp);
        myLeftBox = new HBox();
        myLeftBox.getChildren().add(myCommandPane.getPane());
        myLeftBox.getChildren().add(myDetailPane.getPane());
        return myLeftBox;
    }

    private VBox createCenter(){
        myCenterBox = new VBox();
        if (myGameType.equals("Scrolling")){
            myDesignArea = new DesignArea();
        } else if (myGameType.equals("RPG")){
            myDesignArea = new GridDesignArea();
        }
        myScrollPane = myDesignArea.getScrollPane();
        myToolbar = new EditorToolbar(this);
        myCenterBox.getChildren().add(myToolbar.getPane());
        myCenterBox.getChildren().add(myScrollPane);
        return myCenterBox;
    }

    private void addBackground(){
        if(myLevelSettings.getBackgroundFilePath()!=null){
            String filePath = FILE_PREFIX + getUserDirectory()
            + IMAGES_LOCATION + myLevelSettings.getBackgroundFilePath();
            displayBackgroundOnScreen(filePath);
        }
    }

    private boolean playerIsActive(GameObject player, List<GameObjectView> listOfActiveAvatars)
    {
        for(GameObjectView activeAvatar : listOfActiveAvatars)
        {
            String avatarType = activeAvatar.getType().replaceAll("\\s+","");
            String playerType = player.getTypeName();


            if(playerType.equals(avatarType))
            {
                return true;
            }
        }
        return false;
    }


    @SuppressWarnings("unused")
    private void addAvatar(){
        if(myGameInterface!=null && myGameInterface.getCurrentLevel()!=null){
            for(GameObject player: myGameInterface.getCurrentLevel().getPlayers()){ 
                ArrayList<GameObjectView> listOfPlayer = myDetailPane.getCurrentAvatars();
                if(!playerIsActive(player,listOfPlayer))
                {             
                    String filePath = FILE_PREFIX+getUserDirectory()+AVATAR_IMAGE_LOCATION+ File.separator+player.getImageFileName();
                    myDetailPane.setAvatar(filePath);
                }
            }
        }
    }

    public void setBackground(){
        String filePath = getFilePath(IMAGE_FILE_TYPE, BG_IMAGE_LOCATION);
        displayBackgroundOnScreen(filePath);
    }

    private void displayBackgroundOnScreen(String filePath){
        if(filePath != null){
            ImageView backgroundImage = new ImageView(new Image(filePath));
            backgroundImage.setFitHeight(ViewResources.SCROLL_PANE_HEIGHT.getDoubleResource());
            backgroundImage.setFitWidth(ViewResources.SCROLL_PANE_WIDTH.getDoubleResource());
            backgroundImage.setId(BACKGROUND_IMAGE_ID);

            myScrollPane.setPrefSize(ViewResources.SCROLL_PANE_WIDTH.getDoubleResource(), ViewResources.SCROLL_PANE_HEIGHT.getDoubleResource()); 
            myDesignArea.setBackground(backgroundImage); 

            String file = filePath.substring(filePath.lastIndexOf("/") + 1);
            myLevelSettings.setBackgroundImage("Background/" + file);
        }
    }

    @Override
    public void setAvatar(){
        String filePath = getFilePath(IMAGE_FILE_TYPE, AVATAR_IMAGE_LOCATION);
        if(filePath!=null){
            myDetailPane.setAvatar(filePath);
        }   
    }

    public void setMusic(){
        try {
            String musicFilePath = getFilePath(MUSIC_FILE_TYPE,MUSIC_FILE_LOCATION);
            String file = musicFilePath.substring(musicFilePath.lastIndexOf("/") +1);
            myLevelSettings.setBackgroundMusic(file);

        }catch (NullPointerException e){
            System.out.println("Music was not added");
        }
    }

    private String getFilePath(String fileType, String fileLocation){
        FileOpener myFileOpener = new FileOpener();
        File file =(myFileOpener.chooseFile(fileType, fileLocation));
        if(file != null){
            return file.toURI().toString();
        }
        return null;
    }

    public Parent getRoot(){
        return this.myRoot;
    }

    @Override
    public void saveLevelData () {
        //if(myLevelSettings.getMainCharacter()==null){
        //myDataStoreInterface.addMainCharacter(0, 0, IGameEditorData.MAIN_CHAR_WIDTH, IGameEditorData.MAIN_CHAR_HEIGHT,null);
        // }
        // add Game Objects to level
        myDataStoreInterface.storeMainCharToXML();
        myDataStoreInterface.addGameObjectsToLevel();
        myDataStoreInterface.addRandomGenerationFrame();

        closeLevelWindow.set(true);
    }


    public BooleanProperty getSaveLevelProperty(){
        return this.closeLevelWindow;
    }

    public void setSaveProperty(Boolean bool){
        closeLevelWindow.set(bool);
    }

    private String getUserDirectory(){
        return System.getProperty("user.dir") + "/" ;
    }
}