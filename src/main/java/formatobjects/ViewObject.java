package formatobjects;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import value.ActualValue;
import value.FormatValue;
import value.ViewObjectPropertyValue;

public class ViewObject extends FormatObject
{
	private String viewObjectID;
	private Node node;
	private ViewObjectPropertyValue width;
	private ViewObjectPropertyValue height;
	
	
	public ViewObject(String viewObjectID)
	{
		super();
		this.viewObjectID = viewObjectID;
		this.width = new ViewObjectPropertyValue(getWidth(), new ActualValue(0));
		this.height = new ViewObjectPropertyValue(getHeight(), new ActualValue(0));
		
	}
	
	public ViewObject(Node node, String viewObjectID)
	{
		this(viewObjectID);
		connectNode(node);
	}
	
	/**
	 * THIS METHOD IS CURRENTLY UNUSED/ It's used for testing
	 * @return
	 */
	public String getViewObjectID()
	{
		return viewObjectID;
	}
	
	public void connectNode(Node node)
	{
		this.node = node;
	}
	
	public void updateDimensions()
	{
		if(node instanceof Region)
		{
			Region region = ((Region)node);
			double widthValue = region.getWidth() + region.insetsProperty().getValue().getLeft() + region.insetsProperty().getValue().getRight();
			getWidth().setValue(new ActualValue(widthValue));
			double heightValue = region.getHeight() + region.insetsProperty().getValue().getBottom() + region.insetsProperty().getValue().getTop();
			getHeight().setValue(new ActualValue(heightValue));
		}
		else if(!(node instanceof ImageView))
		{
			double widthValue = node.layoutBoundsProperty().getValue().getWidth();
			getWidth().setValue(new ActualValue(widthValue));
			double heightValue = node.layoutBoundsProperty().getValue().getHeight();
			getHeight().setValue(new ActualValue(heightValue));
		}
	}
	
	public Node renderNode()
	{
		updateWidth();
		updateHeight();
		updateX();
		updateY();
		
		return getNode();
	}
	public Node getNode()
	{
		return node;
	}
	
	@Override
	public void setWidth(FormatValue formatValue)
	{
		width.setValue(formatValue);
	}
	
	@Override
	public void setHeight(FormatValue formatValue)
	{
		height.setValue(formatValue);
	}

	private void updateWidth()
	{
		if(width.hasValueToUpdate())
		{
			double widthValue = width.getValue();
			if(node instanceof Region)
			{
				((Region)node).setMinWidth(widthValue);
				((Region)node).setPrefWidth(widthValue);
				((Region)node).setMaxWidth(widthValue);
			}
			else if(node instanceof ImageView)
			{
				((ImageView)node).setFitWidth(widthValue);
				node.minWidth(widthValue);
				node.prefWidth(widthValue);
				node.maxWidth(widthValue);
			}
			else
			{
				node.minWidth(widthValue);
				node.prefWidth(widthValue);
				node.maxWidth(widthValue);
			}
		}
	}
	
	private void updateHeight()
	{
		if(height.hasValueToUpdate())
		{
			double heightValue = height.getValue();
			if(node instanceof Region)
			{
				((Region)node).setMinHeight(heightValue);
				((Region)node).setPrefHeight(heightValue);
				((Region)node).setMaxHeight(heightValue);
			}
			else if(node instanceof ImageView)
			{
				((ImageView)node).setFitHeight(heightValue);
				node.minHeight(heightValue);
				node.prefHeight(heightValue);
				node.maxHeight(heightValue);
			}
			else
			{
				node.minHeight(heightValue);
				node.prefHeight(heightValue);
				node.maxHeight(heightValue);
			}
		}
	}
	
	private void updateX()
	{
		if(getX().hasValidValue())
		{
			node.setLayoutX(getX().getValue());
		}
	}
	
	private void updateY()
	{
		if(getY().hasValidValue())
		{
			node.setLayoutY(getY().getValue());
		}
	}

}
