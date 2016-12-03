package objects;

/**
 * This is an abstract class that provides the template for the Game Blocks that will be used throughout VOOGA.
 * The four boolean values correspond to the four sides; "true" means character can pass through, "false" means impenetrable.
 * 
 * @author Ray Song
 *
 */
public abstract class AbstractGameBlock extends AbstractGameObject{
	
	protected boolean west;
	protected boolean east;
	protected boolean north;
	protected boolean south;
	
	private String imageFileName;
	
	public AbstractGameBlock(double xPosition, double yPosition, double width, double height, String imageFileName) {
		super(xPosition, yPosition, width, height);
		this.imageFileName = imageFileName;
	}
	
	public String getImageFileName() {
		return imageFileName;
	}

	
	
}
