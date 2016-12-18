package gameeditor.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import gameeditor.view.interfaces.IEditorToolbar;
import gameeditor.view.interfaces.IToolbarParent;
import general.NodeFactory;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
/**
 * 
 * @author John Martin, Pratiksha Sharma
 *
 */
public class EditorToolbar implements IEditorToolbar {

	// TODO: Remove hardcoding of the following values
	// Min Width, Max Width, Min Height
	
	private IToolbarParent myOutput;
    private NodeFactory myFactory = new NodeFactory();
	private Pane myPane;
	private ImageView myBackgroundImageView;
	private ImageView myAvatarImageView;
	private ImageView myMusicImageView;
	private ImageView myLoadGameImageView;

    public EditorToolbar(IToolbarParent toolOut) {
        myOutput = toolOut;
//        myLevelData = new HashMap<String,String>();
        myPane = new Pane();
        myPane.setMinWidth(TOOLBAR_WIDTH); myPane.setMaxWidth(TOOLBAR_WIDTH);
        myPane.setMinHeight(TOOLBAR_HEIGHT); myPane.setMaxHeight(TOOLBAR_HEIGHT);
        myPane.setBackground(new Background(new BackgroundFill(Color.GHOSTWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        createButton(myBackgroundImageView, "/Background.png", BG_IMAGE_WIDTH, BG_IMAGE_XOFFSET, myFactory.makeTooltip("Background"), e -> myOutput.setBackground());
        createButton(myAvatarImageView, "/Avatar.png", AVATAR_IMAGE_WIDTH, AVATAR_IMAGE_XOFFSET, myFactory.makeTooltip("Avatar"), e -> myOutput.setAvatar());
        createButton(myMusicImageView, "/eighthNote.png", MUSIC_IMAGE_WIDTH, MUSIC_IMAGE_XOFFSET, myFactory.makeTooltip("Music"), e -> myOutput.setMusic());
        createButton(myLoadGameImageView, "/Save.png", LOAD_GAME_IMAGE_WIDTH, LOAD_GAME_IMAGE_XOFFSET, myFactory.makeTooltip("Save"), e-> sendLevelData());
    }

    // TODO: REFACTOR THIS METHOD TO WORK GENERALLY, USE image.getWidth();
    private void createButton(ImageView myImageView, String fileLocation, double imageWidth, double imageXOffset,
                              Tooltip tooltip, EventHandler<MouseEvent> handler){
        Image buttonImage;
        try {
            buttonImage = new Image(new FileInputStream(IMAGE_FILE_LOCATION + fileLocation));
            myImageView = new ImageView(buttonImage);
            myImageView.setPreserveRatio(true);
            myImageView.setFitHeight(BUTTON_IMAGE_HEIGHT);
            myImageView.setFitWidth(imageWidth);
            myImageView.setLayoutX(imageXOffset);
            myImageView.setLayoutY(BUTTON_IMAGE_YOFFSET);
            Tooltip.install(myImageView, tooltip);
            myImageView.setOnMouseClicked(handler);
            //myImageView.setOnMousePressed(handler);
            //myImageView.setOnMouseReleased(null);
            myPane.getChildren().add(myImageView);
        } catch (FileNotFoundException e) {
        }
    }
	
	private void sendLevelData(){    
	    myOutput.saveLevelData();
	}
	
	public Label createLbl(String labelText){
		Label labl = new Label (labelText);
		return labl;
	}
	
	public void handleClick(TextArea field){
		field.setText("");
	}

	public Pane getPane(){
		return myPane;
	}	
}
