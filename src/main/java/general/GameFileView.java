package general;

import general.interfaces.IGameFileView;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.event.EventType;

/**
 * @author Ryan Bergamini
 */


public class GameFileView implements IGameFileView
{
	private GameFile gameFile;
	private Pane view;
	private Rectangle gameView;
	private Paint gameViewColor;
	private boolean isSelected;
	private NodeFactory myFactory = new NodeFactory();

	public GameFileView(GameFile gameFile)
	{
		this.gameFile = gameFile;
		this.view = createView();
		this.isSelected = false;
		configureEventHandlers();
	}

	/**
	 * Regardless of purpose for the GameFileView, a GameFileViewEvent.VIEW_CLICKED_ON event
	 * should always be fired whenever the view is clicked on
	 */
	private void configureEventHandlers()
	{
		gameView.setOnMouseClicked(e -> fireViewClickedOnEvent());
	}

	@Override
	public void fireDeleteEvent()
	{
		view.fireEvent(new GameFileViewEvent(GameFileViewEvent.REMOVE_FROM_GALLERY, this));
	}

	@Override
	public void addEventHandlerToGameView(EventType<? extends Event> eventType, EventHandler<Event> eventHandler)
	{
		gameView.addEventHandler(eventType, eventHandler);
	}

	@Override
	public void highlight()
	{
		gameView.setFill(Color.YELLOW);
	}

	@Override
	public void dehighlight()
	{
		if(!isSelected)
		{
			gameView.setFill(gameViewColor);
		}
	}

	@Override
	public void deselect() {
		isSelected = false;
		dehighlight();
	}

	@Override
	public void select()
	{
		isSelected = true;
		highlight();
	}

	private void fireViewClickedOnEvent()
	{
		gameView.fireEvent(new GameFileViewEvent(GameFileViewEvent.VIEW_CLICKED_ON, this));
	}

	private Pane createView()
	{
		Pane view = new Pane();
		Label name = new Label(gameFile.getGameName());
		name.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		double rectWidth = name.getText().length() * name.getFont().getSize();
		Rectangle rect = new Rectangle(rectWidth, 85);
		int randVal = (int)(Math.random() * 3);
		if(randVal == 0)
		{
			rect.setFill(Color.MEDIUMPURPLE);
		}
		else if(randVal == 1)
		{
			rect.setFill(Color.DEEPPINK);
		}
		else
		{
			rect.setFill(Color.BLUEVIOLET);
		}
		gameViewColor = rect.getFill();
		gameView = rect;
		Tooltip Trect = myFactory.makeTooltip("GalleryItem");
		Tooltip.install(rect, Trect);
		view.getChildren().add(gameView);
		
		view.getChildren().add(name);
		return view;
	}

	@Override
	public GameFile getGameFile()
	{
		return gameFile;
	}

	@Override
	public Node getNode()
	{
		return view;
	}
}