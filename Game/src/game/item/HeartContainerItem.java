package game.item;
import game.Frame;
import game.GameWorld;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class HeartContainerItem extends Item 
{
	/* the only animation this item has */

	public HeartContainerItem(GameWorld gameWorld) throws SlickException 
	{
		super(gameWorld, new Frame[] {	new Frame(new Image("images/heartcontainer_item_0.png"), 80)	},
				500, 250, ItemType.HEART);
	}
}
