package game.item;
import game.Frame;
import game.GameWorld;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class RedPotionItem extends Item 
{
	/* the only animation this item has */
	public RedPotionItem(GameWorld gameWorld) throws SlickException 
	{
		super(	gameWorld, new Frame[] {	new Frame(new Image("images/potion_item_0.png"), 80),
											new Frame(new Image("images/potion_item_1.png"), 80),
											new Frame(new Image("images/potion_item_2.png"), 80),
											new Frame(new Image("images/potion_item_3.png"), 80)},
				500, 500, ItemType.HALF_POTION);
	}
}
