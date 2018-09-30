package game.item;
import game.Frame;
import game.GameWorld;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class PegasusBootsItem extends Item 
{
	/* the only animation this item has */

	public PegasusBootsItem(GameWorld gameWorld) throws SlickException 
	{
		super(gameWorld, new Frame[] {	new Frame(new Image("images/pegasusboots_item_0.png"), 80)	}, 750, 250, ItemType.SPEED_1);
	}
}
