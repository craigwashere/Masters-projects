package game.item;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import game.Animation;
import game.Frame;
import game.GameWorld;

public class Portal extends Item
{
	/* the only animation this item has */
//	private Animation movingAnimation;
	private int rotation;
	private boolean isActive = false;
	private Rectangle drawingRectangle;
	
	public Portal(GameWorld gameWorld) throws SlickException 
	{
		super(gameWorld, new Frame[] {	new Frame(new Image("images/portal.png"), 80)	}, 513, 360, ItemType.PORTAL);	
		initResources();
	}

	private void initResources() 
	{
		int location_x = 513;
		int location_y = 360;

		/* set the bounding box of this enemy */
		Rectangle r = new Rectangle(0,0,1,1);//location_x, location_y, movingAnimation.getSprite().getWidth(), movingAnimation.getSprite().getHeight());
		drawingRectangle = new Rectangle(location_x, location_y, movingAnimation.getSprite().getWidth(), movingAnimation.getSprite().getHeight());
		
		//initially, portal is inactive, so we don't need a bounding box
		setBoundingBox(r);
	}
	
	public void update(GameContainer container, int delta)	throws SlickException 
	{	
		super.update(container, delta);
		
		//movingAnimation.update(delta);
		rotation = (rotation + (delta/2)) % 360;
	}

	/* override this method in your classes */
	public void render(GameContainer container, Graphics g) throws SlickException 
	{
		if (isActive)
		{
			g.pushTransform();
				g.rotate(drawingRectangle.getCenterX(), drawingRectangle.getCenterY(), rotation);
				g.drawImage(movingAnimation.getSprite(), drawingRectangle.getX(), drawingRectangle.getY());
				g.draw(getBoundingBox());
			g.popTransform();	
		}
		else
			g.drawImage(movingAnimation.getSprite(), drawingRectangle.getX(), drawingRectangle.getY());
	}
	
	public void setActiveStatus(boolean activeFlag)	
	{
		isActive = activeFlag;
		if (activeFlag == true)
			setBoundingBox(drawingRectangle);
	}
	
	public boolean getActiveStatus()				{	return isActive;		}
}
