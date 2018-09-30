package game.item;
import game.Animation;
import game.Frame;
import game.GameWorld;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

/* used to generalize an item entity */
/* Extend this class with your own items */
/* add items to appropriate Room classes */
public abstract class Item 
{
	public ItemType item_type;
	public float locationX, locationY;
	protected Animation movingAnimation;

	/* everything drawn should have a boundingBox */
	/* Used for hit detection */
	private Rectangle boundingBox;

	/* a reference to gameworld if needed */
	private GameWorld gameWorld;
	
	private Animation currentAnimation;

	public Item(GameWorld gameWorld)
	{
		this.setGameWorld(gameWorld);
	}
	
	public Item(GameWorld gameWorld, Frame [] frames, float locationX, float locationY, ItemType itemType) throws SlickException 
	{
		this.setGameWorld(gameWorld);	
		//Frame[] frames = new Frame[] {	new Frame(new Image(Image), 80)	};
		movingAnimation = new Animation(frames, true);
		setCurrentAnimation(movingAnimation);

		this.locationX = locationX;
		this.locationY = locationY;
		set_item_type(ItemType.HEART);

		/* set the bounding box of this enemy */
		Rectangle r = new Rectangle(locationX, locationY, movingAnimation.getSprite().getWidth(), movingAnimation.getSprite().getHeight());
		
		setBoundingBox(r);
}

	/* override this method in your classes */
	public void update(GameContainer container, int delta)	throws SlickException 
	{movingAnimation.update(delta);	}

	/* override this method in your classes */
	public void render(GameContainer container, Graphics g)	throws SlickException
	{
		g.drawImage(movingAnimation.getSprite(), getBoundingBox().getX(), getBoundingBox().getY());
		g.draw(getBoundingBox());
	}

	/* Getter and Setter methods follow */

	public Rectangle getBoundingBox() 								{	return boundingBox;							}
	
	public void setBoundingBox(Rectangle boundingBox) 				{	this.boundingBox = boundingBox;				}

	public GameWorld getGameWorld() 								{	return gameWorld;							}

	public void setGameWorld(GameWorld gameWorld) 					{	this.gameWorld = gameWorld;					}

	public Animation getCurrentAnimation() 							{	return currentAnimation;					}

	public void setCurrentAnimation(Animation currentAnimation) 	{	this.currentAnimation = currentAnimation;	}
	
	public void set_item_type(ItemType item_type)					{	this.item_type = item_type;					}
	
	public ItemType get_item_type()									{	return item_type;							}
}
