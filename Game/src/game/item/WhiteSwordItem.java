package game.item;
import game.Frame;
import game.GameWorld;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class WhiteSwordItem extends Item 
{

	public WhiteSwordItem(GameWorld gameWorld) throws SlickException 
	{
		super(gameWorld, new Frame[] {	new Frame(new Image("images/whitesword_item_0.png"), 80),	},
				750, 500, ItemType.SWORD);
	}

}
