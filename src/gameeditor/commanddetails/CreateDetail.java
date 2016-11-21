package gameeditor.commanddetails;

import gameeditor.view.ViewResources;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateDetail extends AbstractCommandDetail  {

	private double cbWidth = 7*ViewResources.AVATAR_ZONE_WIDTH.getDoubleResource()/15 - myDetailPadding;
	private double cbHeight = 30;
	private VBox myVBox;
	
	public CreateDetail() {
		super();
		myVBox = new VBox();
		myVBox.setSpacing(ViewResources.DETAIL_CONTENT_PADDING.getDoubleResource());
		myVBox.setAlignment(Pos.CENTER);
		myContainerPane.setContent(myVBox);
		String [] array = new String [] {"Up", "Down", "Left", "Right", "Shoot", "Interact"};
		for (String string : array){
			createSelectDirectionsControl("Input", array);
		}
	}
	
	public void createSelectDirectionsControl(String label, String [] optionsArray){
		HBox innerContainer = new HBox();
		double hboxSpacing = 10;
		innerContainer.setSpacing(hboxSpacing);
		innerContainer.setAlignment(Pos.CENTER);
		Label labl = new Label ("PROPERTY");
		ComboBox<String> cb = createComboBox(optionsArray);
		innerContainer.getChildren().add(labl);
		innerContainer.getChildren().add(cb);
		myVBox.getChildren().add(innerContainer);
	}
	
	public TextArea createInputField(String label, double hboxSpacing){
		TextArea inputField = new TextArea(label);
		inputField.setMinWidth(myPaneWidth-2*myDetailPadding-cbWidth-hboxSpacing);
		inputField.setMaxWidth(myPaneWidth-2*myDetailPadding-cbWidth-hboxSpacing);
		inputField.setMinHeight(cbHeight);
		inputField.setMaxHeight(cbHeight);
		inputField.setOnMouseClicked(e -> handleClick(inputField));
		inputField.setOnKeyPressed(e -> inputField.clear());
		inputField.setOnKeyReleased(e -> handleKey(inputField, e.getCode()));
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
	
	public void handleKey(TextArea field, KeyCode kc){
		field.setText(kc.toString());
	}
	public void createTextField(){
		
	}

}
