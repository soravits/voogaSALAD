package gameeditor.commanddetails;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import objects.GameObject;

// TODO: Refactor this class - duplicated code with CreateDetail
public class MainCharacterDetail extends AbstractCommandDetail {
	private VBox myVBox;
	private ArrayList<ComboBox<String>> myComboBoxes = new ArrayList<ComboBox<String>>();
	private String [] myPropertiesArray = DetailResources.PROPERTIES.getArrayResource();
	
	public static final double MAIN_CHARACTER_INITIAL_X_POSITION = 20;
	public static final double MAIN_CHARACTER_INITIAL_Y_POSITION = 20;
	public static final double MAIN_CHARACTER_HEIGHT = 100;
	public static final double MAIN_CHARACTER_WIDTH = 100;
	
	public MainCharacterDetail() {
		super();
	}
	

	@Override
	public void init() {
		myVBox = new VBox();
		myVBox.setSpacing(myDetailPadding);
		myVBox.setAlignment(Pos.CENTER);
		myContainerPane.setContent(myVBox);	
		createProperties();
		createSave();
	}
	
	public void createSave(){
		Button save = new Button();
		save.setText("Save New Type");
		save.setMinWidth(cbWidth);
		save.setMinHeight(cbHeight);
		save.setOnAction((e) -> {handleSave();});
		myVBox.getChildren().add(save);
	}
	
	public void handleSave(){
		if (verifySave()){
			// TODO: Create this for saving maincharacter
			// TODO: Input file path when creating pane
			ResourceBundle geprops =  ResourceBundle.getBundle("GameEditorProperties");
			Enumeration<String> enumKeys = geprops.getKeys();
			Map<String, String> propertiesMap = new HashMap<String, String>();
			for (ComboBox<String> cb : myComboBoxes){
				propertiesMap.put(enumKeys.nextElement(), cb.getValue());
			}
			
			myDataStore.addMainCharacter(MAIN_CHARACTER_INITIAL_X_POSITION, MAIN_CHARACTER_INITIAL_Y_POSITION, MAIN_CHARACTER_WIDTH, MAIN_CHARACTER_HEIGHT, propertiesMap);
		} else {
			
		}
	}
	
	public boolean verifySave(){
		// TODO: FINISH VERIFICATION METHOD
		// Check all of the following:
		// Type Name != null or TypeName or ""
		// Destructible/Damage/Points/Time/Random/Health/Movable != null
		// SpriteImage != null/unfindable
		return true;
	}
	
	public void createProperties(){
		for (String label : myPropertiesArray){
			BorderPane bp = new BorderPane();
			bp.setMinWidth(paddedPaneWidth);
			bp.setMaxWidth(paddedPaneWidth);
			Label labl = createPropertyLbl(label);
			ComboBox<String> cb = createPropertyCB(label);
			myComboBoxes.add(cb);
			bp.setLeft(labl);
			bp.setRight(cb);
			BorderPane.setAlignment(labl, Pos.CENTER_LEFT);
			myVBox.getChildren().add(bp);
		}
	}
	
	public Label createPropertyLbl(String property){
		Label labl = new Label (property);
		return labl;
	}
	
	public ComboBox<String> createPropertyCB(String property){
		DetailResources resourceChoice = DetailResources.valueOf(property.toUpperCase(Locale.ENGLISH));
		String [] optionsArray = resourceChoice.getArrayResource();
		ComboBox<String> cb = createComboBox(optionsArray);
		return cb;
	}
	
	public TextArea createInputField(String label, double hboxSpacing){
		TextArea inputField = new TextArea(label);
		inputField.setMinWidth(paddedDetailWidth);
		inputField.setMaxWidth(paddedDetailWidth);
		inputField.setMinHeight(cbHeight);
		inputField.setMaxHeight(cbHeight);
		inputField.setOnMouseClicked(e -> handleClick(inputField));
		return inputField;
	}
	
	public ComboBox<String> createComboBox(String [] boxOptions){
		ComboBox<String> cb = new ComboBox<String>();
		cb.getItems().addAll(boxOptions);
		cb.setMinWidth(cbWidth);
		cb.setMaxWidth(cbWidth);
		cb.setMinHeight(cbHeight);
		cb.setMaxHeight(cbHeight);
		return cb;
	}
	
	public void handleClick(TextArea field){
		field.setText("");
	}

	public void createTextField(){
		
	}

}