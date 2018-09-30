package game.room;
import game.GameWorld;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;


public abstract class TiledImageRoom extends Room {
	
	
	// reference table to drawing tiles
	private int[][] tiles;
	
	/*
	 * 
	 */
	public TiledImageRoom(GameWorld gameWorld) {
		this(gameWorld, 0, 0);
		
		
	}
	
	public TiledImageRoom(GameWorld gameWorld, int row, int col) {
		super(gameWorld, row, col);
		
		//20 rows and 15 columns accommodates for 60px x 60px tiles
		tiles = new int[20][15]; 
	}
	

	public int[][] getTiles() {
		return tiles;
	}


	
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		
		// draw room
		drawBackground(container, g);
		super.render(container, g);
	}


	public abstract void drawBackground(GameContainer container, Graphics g);




	@Override
	protected void initRoomImage() {
		// not needed for tiled rooms

	}

	@Override
	protected void initRoomColor() {
		Random r = GameWorld.RAND;
		/* Set the room color */
		setRoomColor(new Color((float) Math.max(r.nextFloat(), .6),
				(float) Math.max(r.nextFloat(), .6), (float) Math.max(
						r.nextFloat(), .6)));
	}

	@Override
	protected void initRoomType() {
		/* Set the room type */
		setRoomType(RoomType.ZELDA_ROOM_NORMAL);
	}

	@Override
	protected void initRoomPlayableBoundingBox() {
		/* Set the room's playable area */
		setBoundingBox(new Rectangle(3*60, 2*60, 14*60, 11*60));
	}

}
