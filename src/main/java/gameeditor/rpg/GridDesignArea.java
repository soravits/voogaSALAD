package gameeditor.rpg;

import java.util.ArrayList;

import gameeditor.commanddetails.ISelectDetail;

import gameeditor.objects.GameObjectView;
import gameeditor.view.AbstractDesignArea;
import gameeditor.view.interfaces.IDesignArea;
import gameeditor.view.interfaces.IGameEditorView;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author John Martin
 *
 */

public class GridDesignArea extends AbstractDesignArea implements IDesignArea, IGridDesignArea {
    
    private CellGrid myCellGrid;
    private ArrayList<Cell> myCells = new ArrayList<Cell>();
    private Cell myHoverCell;
    private Cell myClickCell;
    private ArrayList<Cell> mySelectedCells = new ArrayList<Cell>();
    private ArrayList<Cell> myNewSelectedCells = new ArrayList<Cell>();
    private ImageView myBG;
    
    public GridDesignArea() {
    	super();
        myScrollPane.setOnKeyPressed((e) -> handleKeyPress(e.getCode()));
        myScrollPane.setOnKeyReleased((e) -> handleKeyRelease(e.getCode()));
        myCellGrid = new CellGrid(0, 0, 40, (int) (1.5*AREA_WIDTH/40), (int) (1.5*AREA_HEIGHT/40), false, this);
        myCells = myCellGrid.getCells();
        for (Cell cell : myCells){
        	myPane.getChildren().add(cell.getRect());
        }
        myPane.setOnMouseMoved(e -> handleHover(e.getX(), e.getY()));
        myPane.setOnMousePressed(e -> handlePress(e.getX(), e.getY()));
        myPane.setOnMouseDragged(e -> handleDrag(e.getX(), e.getY()));
        myPane.setOnMouseReleased(e -> handleRelease(e.getX(), e.getY()));
        myScrollPane.setContent(myPane);
    }   
    
    private void handleHover(double x, double y){
//    	Cell cellOver = findCell(x, y);
//    	if (cellOver != null && myHoverCell != null && cellOver != myHoverCell){
//    		myHoverCell.resetColor();
//    		myHoverCell = cellOver;
//    		myHoverCell.setColor();
//    	} else if (cellOver != null){
//    		myHoverCell = cellOver;
//    		myHoverCell.setColor();
//    	}
    }

    private void handlePress(double x, double y){
    	Cell cell = findCell(x, y);
    	if (myKeyCode != KeyCode.SHIFT){
        	resetCells();
    	}
    	if (clickEnabled){
    		myClickCell = cell;
    		startX = x;
            startY = y;
            mySelectionArea = new Rectangle(x, y, 0, 0);
            mySelectionArea.setFill(Color.AQUAMARINE);
            mySelectionArea.setOpacity(0.1);
            myPane.getChildren().add(mySelectionArea);
    	}
    }
    
    private void invertSelection(Cell cell){
    	if (cell != null && mySelectedCells.contains(cell)){
    		mySelectedCells.remove(cell);
    		myNewSelectedCells.add(cell);
    		cell.resetColor();
    	} else if (cell != null && !mySelectedCells.contains(cell)){
    		mySelectedCells.add(cell);
    		myNewSelectedCells.add(cell);
    		cell.setColor();
    	}
    }

    private void handleRelease(double x, double y) {
    	Cell myReleaseCell = findCell(x, y);
        if (dragged){
        	selectCells(startX, startY, endX, endY);
            myPane.getChildren().remove(mySelectionArea);
            mySelectionArea = null;
        }
        if (myClickCell == myReleaseCell && !myNewSelectedCells.contains(myClickCell)){
        	invertSelection(myClickCell);
        }
        dragged = false;
        startX = -1;
        startY = -1;
        endX = 0;
        endY = 0;
        myClickCell = null;
    }

    private void handleDrag(double x, double y) {
    	if (startX != -1 && startY != -1){
    		startX = Math.min(startX, x);
            startY = Math.min(startY, y);
            endX = Math.max(endX, x);
	        endY = Math.max(endY, y);
            mySelectionArea.setX(startX);
            mySelectionArea.setY(startY);
            mySelectionArea.setWidth(endX-startX);
            mySelectionArea.setHeight(endY-startY);
            if (!dragged){
                dragged = true;
            }
        }
    }

    private void handleKeyPress(KeyCode code){
        myKeyCode = code;
    }

    private void handleKeyRelease(KeyCode code){
        if (code == KeyCode.BACK_SPACE && mySelectedSprite != null){
            // TODO: Remove from backend
            mySelectedSprite.removeSelf();
        }
        myKeyCode = null;
    }
    
	private void resetCells(){
    	for (Cell cell : mySelectedCells){
    		cell.resetColor();
    	}
    	mySelectedCells.clear();
    }
    
    private Cell findCell(double x, double y){
    	Rectangle test = new Rectangle(x, y, 1, 1);
    	for (Cell cell : myCells){
    		if (test.getBoundsInParent().intersects(cell.getRect().getBoundsInParent())){
    			return cell;
    		}
    	}
    	return null;
    }

    public ScrollPane getScrollPane(){
        return myScrollPane;
    }

    public void setBackground(ImageView bg){
    	myBG = bg;
        ObservableList<Node> currentChildren = myPane.getChildren();
        ArrayList<Node> children = new ArrayList<Node>();
        for (Node child : currentChildren){
            if(child.getId()==null || !(child.getId().equals(IGameEditorView.BACKGROUND_IMAGE_ID))){
                children.add(child);
            }
        }
        myPane.getChildren().clear();
        myBG.setLayoutX(0);
        myBG.setLayoutY(0);
        myBG.setPreserveRatio(true);
        bgUpdate();
        myPane.widthProperty().addListener(e -> bgUpdate());
        myPane.heightProperty().addListener(e -> bgUpdate());
        myPane.getChildren().add(bg);
        myPane.getChildren().addAll(children);
    }
    
    private void bgUpdate(){
        double imgWidth = myBG.getImage().getWidth();
        double imgHeight = myBG.getImage().getWidth();
        double widthRatio = myPane.getWidth()/imgWidth;
        double heightRatio = myPane.getHeight()/imgHeight;
        double ratio = Math.max(widthRatio, heightRatio);
        double fitWidth = imgWidth*ratio;
        double fitHeight = imgHeight*ratio;
        myBG.setFitWidth(fitWidth);
        myBG.setFitHeight(fitHeight);
    	myBG.setFitHeight(myPane.getHeight());
    }

    @Override
    public void removeSprite(GameObjectView sprite) {
    	for (Cell cell : myCells){
    		if (cell.getSprite() == sprite){
    			cell.removeSprite();
    		}
    	}
        mySprites.remove(sprite);
        myPane.getChildren().remove(sprite.getImageView());
    }
    
    @Override
    public void removeSpriteFromCell(Cell cell) {
    	cell.removeSprite();
    }

    @Override
    public void enableClick(ISelectDetail sd) {
        mySelectDetail = sd;
        clickEnabled = true;	
    }

    @Override
    public void disableClick() {
        clickEnabled = true;	
    }

    public void initSelectDetail2(GameObjectView sprite){
        if (clickEnabled){
            mySelectDetail.switchSelectStyle(sprite);
            mySelectDetail.initLevel2(sprite);
        }
    }

    private void selectCells(double minX, double minY, double maxX, double maxY){
        Rectangle test = new Rectangle(minX, minY, maxX-minX, maxY-minY);
        for (Cell cell : myCells){
            if(test.getBoundsInParent().intersects(cell.getRect().getBoundsInParent())){
                invertSelection(cell);
            }
        }
    }
    
    public Pane getPane(){
    	return myPane;
    }

	@Override
	public Cell getHoverCell() {
		return myHoverCell;
	}

	@Override
	public ArrayList<Cell> getSelectedCells() {
		return mySelectedCells;
	}

	@Override
	public void addSprite(GameObjectView gameObject) {
		if (mySelectedCells != null){
			for (int i = 0; i < mySelectedCells.size(); i++){
				Cell cell = mySelectedCells.get(i);
				if (i == 0){
					addSprite(gameObject, cell);
				} else {
					addSprite(new GameObjectView(gameObject, 0, 0), cell);
				}
			}
		}
	}
	
    @Override
    public void addSprite(GameObjectView sprite, Cell cell) {
    	cell.addSprite(sprite);
    }
    

    @Override
    public void addAvatar(GameObjectView gov) {
        myAvatars.add(gov);
        mySprites.add(gov);
        ArrayList<Cell> myCells = myCellGrid.getCells();
        int index = (myCells.size()-1)/2;
        if (myCells.size() > 0){
            addSprite(gov, myCells.get(index));
        }
    }

}
