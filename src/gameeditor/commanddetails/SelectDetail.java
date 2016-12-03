package gameeditor.commanddetails;

import java.util.ArrayList;

import gameeditor.objects.GameObject;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SelectDetail extends AbstractCommandDetail implements ISelectDetail {
	
	private static final String X_LABEL = "X: ";
	private static final String Y_LABEL = "Y: ";
	private static final String WIDTH_LABEL = "W: ";
	private static final String HEIGHT_LABEL = "H: ";
	
	private VBox myVBox = new VBox();
	
	private Label mySelectLabel;
		
	private TextArea myXTextArea = new TextArea();
	private TextArea myYTextArea = new TextArea();
	private TextArea myWidthTextArea = new TextArea();
	private TextArea myHeightTextArea = new TextArea();
	private ImageView myIV;
	private GameObject myGO;
	private Pane myImagePane;

	private String myType;
	public static final String X_POSITON_KEY = "xPosition";
	public static final String y_POSITION_KEY = "yPosition";
	

	public SelectDetail() {
		super();
	}

	@Override
	public void init() {
		myVBox = new VBox();
		myVBox.setSpacing(myDetailPadding);
		myVBox.setAlignment(Pos.CENTER);
		myContainerPane.setContent(myVBox);	
		myDesignArea.enableClick(this);
		addSelectLabel();
	}
	
	public void initLevel2(GameObject sprite){
		init();
		myGO = sprite;
		mySelectLabel.setTextFill(Color.LIGHTGREY);
		createTypeLabel();
		createPos();
		createUpdate();
	}
	
	public void clearSelect(){
		init();
	}
	
	public void updateSpritePosition(double x, double y){
		myXTextArea.setText(X_LABEL + Double.toString(x));
		myYTextArea.setText(Y_LABEL + Double.toString(y));
	}
	
	public void updateSpriteDimensions(double width, double height){
		myWidthTextArea.setText(WIDTH_LABEL + Double.toString(width));
		myHeightTextArea.setText(HEIGHT_LABEL + Double.toString(height));
	}
	
	public void createUpdate(){
		Button update = new Button();
		update.setText(DetailResources.UPDATE_BUTTON_TEXT.getResource());
		update.setMinWidth((paddedPaneWidth - hboxSpacing)/2);
		update.setMaxWidth((paddedPaneWidth - hboxSpacing)/2);
		update.setMinHeight(cbHeight);
		update.setOnAction((e) -> {handleUpdate();});
		myVBox.getChildren().add(update);
	}
	
	private void handleUpdate() {
		String xString = myXTextArea.getText();
		String yString = myYTextArea.getText();
		String widthString = myWidthTextArea.getText();
		String heightString = myHeightTextArea.getText();
		double x = Double.parseDouble(xString.substring(X_LABEL.length()));
		double y = Double.parseDouble(yString.substring(Y_LABEL.length()));
		double width = Double.parseDouble(widthString.substring(WIDTH_LABEL.length()));
		double height = Double.parseDouble(heightString.substring(HEIGHT_LABEL.length()));
		myGO.update(x, y, width, height);
	}

	private void addSelectLabel(){
		BorderPane bp = new BorderPane();
		mySelectLabel = new Label(DetailResources.SELECT_LABEL_TEXT.getResource());
		bp.setCenter(mySelectLabel);
		bp.setMinWidth(paddedPaneWidth);
		bp.setMaxWidth(paddedPaneWidth);
		myVBox.getChildren().add(bp);
	}	
	
	public void createPos(){
		createInfoBP(myXTextArea, X_LABEL, myGO.getX(), myYTextArea, Y_LABEL, myGO.getY());
		createInfoBP(myWidthTextArea, WIDTH_LABEL, myGO.getWidth(), myHeightTextArea, HEIGHT_LABEL, myGO.getHeight());
	}
	
	public void createInfoBP(TextArea ta1, String label1, double value1, TextArea ta2, String label2, double value2){
		BorderPane bp = new BorderPane();
		bp.setMinWidth(paddedPaneWidth);
		bp.setMaxWidth(paddedPaneWidth);
		ta1 = createTextArea(label1, value1, ta1);
		ta2 = createTextArea(label2, value2, ta2);
		bp.setLeft(ta1);
		bp.setRight(ta2);
		myVBox.getChildren().add(bp);
		System.out.println("added");
		System.out.println(myVBox.getChildren().size());
	}
	
	public TextArea createTextArea(String label, double value, TextArea ta){
		ta.setText(label + Double.toString(value));
		ta.setMinWidth(cbWidth); ta.setMaxWidth(cbWidth);
		ta.setMinHeight(cbHeight); ta.setMaxHeight(cbHeight);
		ta.setOnKeyReleased((e) -> handleKeyRelease(e.getCode(), e.getCharacter(), ta, label));
//		ta.setOnMouseClicked((e) -> handleClick(ta));
		return ta;
	}
	
	public void createTypeLabel(){
		myType = myGO.getType();
		mySelectLabel.setText(myType);
	}
	
	public void handleClick(TextArea field){
		field.setText("");
	}
	
	public void handleKeyRelease(KeyCode kc, String character, TextArea field, String label){
//		if (kc == KeyCode.BACK_SPACE){
		if (field.getText().length() < label.length() && kc.isDigitKey()){
			field.setText(label + character);
			field.positionCaret(field.getText().length());
		} else if (field.getText().length() < label.length()){
			field.setText(label);
			field.positionCaret(field.getText().length());
		} else if (kc.isDigitKey()){
			field.setText(label + field.getText().substring(label.length()));
			field.positionCaret(field.getText().length());
		} else if (kc.isLetterKey() || kc.isWhitespaceKey()){
			field.setText(label + field.getText().substring(label.length(), field.getText().length()-1));
			field.positionCaret(field.getText().length());
		}
		
	}

	public void createTextField(){
		
	}

}
