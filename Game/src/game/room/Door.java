package game.room;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

/*
 * A door object that is associated with a room.
 * Each door object is either a top door, bottom door, left door or right door.
 */
public class Door {
	
	public static final int TOP_DOOR = 0, RIGHT_DOOR = 1, BOTTOM_DOOR = 2, LEFT_DOOR = 3;
	
	//the side of the room this door is in
	private int doorSide;
	
	// the room this door is in
	private Room containingRoom;
	
	// the image of the door
	private Image doorImage;
	
	// the bounding box (hitbox) of this door
	private Rectangle boundingBox;
	
	/*
	 * Creates a door object on a specified side within a specified room.
	 * 
	 * @param	doorSide	the side this door is attached to
	 * @param	room		the room this door is in
	 */
	public Door(int doorSide, Room room) {
		this.doorSide = doorSide;
		this.containingRoom = room;
		
		initDoorResources();
	}

	/*
	 * Initializes any door resources from external files
	 * Also initializes the bounding box for this door within the map
	 */
	private void initDoorResources() {
		try {
			Rectangle playableArea = containingRoom.getBoundingBox();
			float playableCenterX = playableArea.getCenterX();
			float playableCenterY = playableArea.getCenterY();
			float playableX = playableArea.getX();
			float playableY = playableArea.getY();
			
			if(doorSide == TOP_DOOR) {
				doorImage = new Image("images/door_top_dungeon.png");
				float doorX = playableCenterX - doorImage.getWidth()/2.0f;
				float doorY = playableY - doorImage.getHeight();
				boundingBox = new Rectangle(doorX, doorY+3, doorImage.getWidth(), doorImage.getHeight());
			} else if(doorSide == RIGHT_DOOR) {
				doorImage = new Image("images/door_right_dungeon.png");
				float doorX = playableX + playableArea.getWidth();
				float doorY = playableCenterY - doorImage.getHeight()/2.0f;
				boundingBox = new Rectangle(doorX - 2, doorY, doorImage.getWidth(), doorImage.getHeight());
			} else if(doorSide == BOTTOM_DOOR) {
				doorImage = new Image("images/door_bottom_dungeon.png");
				float doorX = playableCenterX - doorImage.getWidth()/2.0f;
				float doorY = playableY + playableArea.getHeight() ;
				boundingBox = new Rectangle(doorX, doorY-3, doorImage.getWidth(), doorImage.getHeight());
			} else if(doorSide == LEFT_DOOR) {
				doorImage = new Image("images/door_left_dungeon.png");
				float doorX = playableX - doorImage.getWidth();
				float doorY = playableCenterY - doorImage.getHeight()/2.0f;
				boundingBox = new Rectangle(doorX + 2, doorY, doorImage.getWidth(), doorImage.getHeight());
			}
		} catch (SlickException se) {
			se.printStackTrace();
		}
	}
	
	/*
	 * Returns the side this door is attached to.
	 * Returns either Door.TOP_DOOR, Door.BOTTOM_DOOR, Door.LEFT_DOOR, Door.RIGHT_DOOR
	 */
	public int getDoorSide() {
		return doorSide;
	}

	/*
	 * Sets the door to the specified side. 
	 * 
	 * @param	doorSide	the side this door will be attached to
	 */
	public void setDoorSide(int doorSide) {
		this.doorSide = doorSide;
	}

	/*
	 * Returns the bounding box (hitbox) of this door
	 * 
	 */
	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	/*
	 * Sets the bounding box (hitbox) of this door
	 * 
	 * @param	boundingBox	the bounding box of this door
	 */
	public void setBoundingBox(Rectangle boundingBox) {
		this.boundingBox = boundingBox;
	}

	/*
	 * Renders this door to the canvas
	 */
	public void render(Graphics g) {
		g.drawImage(doorImage, boundingBox.getX(), boundingBox.getY());
		g.draw(boundingBox);
	}
	
	
	public String toString() {
		String ret = "";
		switch (getDoorSide()) {
		case TOP_DOOR: ret += "TOP_DOOR"; break;
		case BOTTOM_DOOR: ret += "BOTTOM_DOOR"; break;
		case RIGHT_DOOR: ret += "RIGHT_DOOR"; break;
		case LEFT_DOOR: ret += "LEFT_DOOR"; break;
		}
		return ret;
	}
	
}
