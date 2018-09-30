package game.room;
import game.GameWorld;
import game.enemy.Enemy;
import game.item.Item;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public abstract class Room implements TileBasedMap {
	
	private static boolean DRAW_A_STAR_GRID = false;
	private int row, col;
	
	private RoomType roomType;
	private Image roomImage;
	private Color roomColor;

	private Rectangle boundingBox;
	
	private ArrayList<Enemy> enemyList;
	private ArrayList<Item> itemList;
	
	private Door[] doors;


	private int numDoors;
	private GameWorld gameWorld;
	
	private int difficultyRating;
	
	/* aStarMap used for a* pathfinding */
	/* 1 denotes an obstruction in the path */
	private static int[][] aStarMap;
	
	/* used for precision of a* pathfinding */
	/* Greater = more precision, more calculation time */
	private  int aStarMapWidth = 40;
	private  int aStarMapHeight = 40;
	
	private float gridWidth;
	private float gridHeight;
	private int distanceFromSpawn;
	
	public Room(GameWorld gameWorld) {
		/*this.setGameWorld(gameWorld);
		
		doors = new Door[4];
		setNumDoors(0);
		initRoomImage();
		initRoomColor();
		initRoomType();
		initRoomPlayableBoundingBox();
		enemyList = new ArrayList<Enemy>();
		itemList = new ArrayList<Item>();
		
		aStarMap = new int[aStarMapHeight][aStarMapWidth];
		gridWidth = getBoundingBox().getWidth() / getaStarMapWidth();
		gridHeight = getBoundingBox().getHeight() / getaStarMapHeight();*/
		this(gameWorld, 0, 0);
	}
	
	public Room(GameWorld gameWorld, int row, int col) {
		this.setGameWorld(gameWorld);
		this.row = row;
		this.col = col;
		
		doors = new Door[4];
		setNumDoors(0);
		initRoomImage();
		initRoomColor();
		initRoomType();
		initRoomPlayableBoundingBox();
		enemyList = new ArrayList<Enemy>();
		itemList = new ArrayList<Item>();
		
		this.difficultyRating = 0;
		
		aStarMap = new int[aStarMapHeight][aStarMapWidth];
		gridWidth = getBoundingBox().getWidth() / getaStarMapWidth();
		gridHeight = getBoundingBox().getHeight() / getaStarMapHeight();
	}
	
	public void createDoor(int doorSide) {
		doors[doorSide] = new Door(doorSide, this);
		setNumDoors(getNumDoors() + 1);
	}

	protected abstract void initRoomImage();
	protected abstract void initRoomColor();
	protected abstract void initRoomType();
	protected abstract void initRoomPlayableBoundingBox();

	public void update(GameContainer container, int delta)
			throws SlickException {
		
		updateAStar();
		
		Iterator<Enemy> enemyIterator = getEnemyList().iterator();
		
		while(enemyIterator.hasNext()) {
			Enemy e = enemyIterator.next();
			e.update(container, delta);
			if(e.getCurrentHealth() <= 0)
				enemyIterator.remove();
		}
		for(Item i: getItemList()) {
			i.update(container, delta);
		}
		
		
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		// draw room
		if(getRoomImage() != null)
			g.drawImage(getRoomImage(), 0, 0, getRoomColor());

		// draw bounding box for testing
		g.draw(getBoundingBox());
		
		/* draw doors */
		for(Door d: doors) {
			if(d != null) {
				d.render(g);
			}
		}
		
		/* draw enemies in this room */
		for(Enemy e: getEnemyList()) {
			e.render(container, g);
		}
		
		/* draw items in this room */
		for(Item i: getItemList()) {
			i.render(container, g);
		}
		
		if(DRAW_A_STAR_GRID) {
			/* draw aStarMap */
			g.setColor(Color.red);
			
			for(int row = 0; row < getaStarMapHeight(); ++row) {
				for(int col = 0; col < getaStarMapWidth(); ++col) {
					g.drawRect(getBoundingBox().getX() + gridWidth * col, getBoundingBox().getY() + gridHeight * row, gridWidth, gridHeight);
					if(aStarMap[row][col] != 0) {
						g.setColor(new Color(255, 0, 0, 70));
						g.fillRect(getBoundingBox().getX() + gridWidth * col, getBoundingBox().getY() + gridHeight * row, gridWidth, gridHeight);
						
						g.setColor(Color.red);
						
					}
				}
			}
		}
	}
	
	public void updateAStar() {
		
		aStarMap = new int[aStarMapHeight][aStarMapWidth];
		
		for(Enemy e: getGameWorld().getCurrentRoom().getEnemyList()) {
			//System.out.println("Updating a* for: " + e);
			addObstruction(e.getBoundingBox());
		}
		
		for(Item i: getGameWorld().getCurrentRoom().getItemList()) {
			addObstruction(i.getBoundingBox());
		}
		
		//addObstruction(getGameWorld().getPlayer().getBoundingBox());
		
	}


	public void addObstruction(Rectangle obstruction) {
		
		float px = obstruction.getX() - getBoundingBox().getX();
		float py = obstruction.getY() - getBoundingBox().getY();
		
		float px_t = px;
		float py_t = py;
		
		for(py = py_t;  ; py += gridHeight) {
			for(px = px_t;  ; px += gridWidth) {
				/*System.out.println("\tpx: " + px);
				System.out.println("\tpy: " + py);
				System.out.println("\trow: "+ (py / gridHeight));
				System.out.println("\tcol: " + (px / gridWidth));*/
				
			
				
				int row = (int) (py / gridHeight);
				int col = (int) (px / gridWidth);
				if(row >= 0 && col >= 0 && row < aStarMap.length && col < aStarMap[row].length)
					aStarMap[row][col] = 1;
				
				//System.out.println();

				if(px > px_t + obstruction.getWidth())
					break;
			}
			if (py > py_t + obstruction.getHeight()) 
				break;
		}
		//System.out.println("Finish");
		//updateAStar();
	}
	
	public int getColFromXCoord(float xCoord) {
		return (int) ((xCoord - getBoundingBox().getX()) / gridWidth);
	}
	
	public int getRowFromYCoord(float yCoord) {
		return (int) ((yCoord - getBoundingBox().getY()) / gridHeight);
	}

	public float getXCoordFromCol(int c) {
		return getBoundingBox().getX() + c * gridWidth;
	}
	
	public float getYCoordFromRow(int r) {
		return getBoundingBox().getY() + r * gridHeight;
	}

	/*
	 * return the playable bounding box
	 */
	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	/*
	 * set the playable bounding box
	 */
	public void setBoundingBox(Rectangle boundingBox) {
		this.boundingBox = boundingBox;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public Image getRoomImage() {
		return roomImage;
	}

	public void setRoomImage(Image roomImage) {
		this.roomImage = roomImage;
	}

	public Color getRoomColor() {
		return roomColor;
	}

	public void setRoomColor(Color roomColor) {
		this.roomColor = roomColor;
	}

	public ArrayList<Enemy> getEnemyList() {
		return enemyList;
	}
	
	public ArrayList<Item> getItemList() {
		return itemList;
	}



	public boolean hasDoor(int doorSide) {
		return doors[doorSide] != null;
	}

	public int getNumDoors() {
		return numDoors;
	}

	public void setNumDoors(int numDoors) {
		this.numDoors = numDoors;
	}
	
	public Door[] getDoors() {
		return doors;
	}
	
	public void setDoors(Door[] newDoors) {
		this.doors = newDoors;
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	public static int[][] getaStarMap() {
		return aStarMap;
	}

	public static void setaStarMap(int[][] aStarMap) {
		Room.aStarMap = aStarMap;
	}
	
	public int getaStarMapWidth() {
		return aStarMapWidth;
	}

	public void setaStarMapWidth(int aStarMapWidth) {
		this.aStarMapWidth = aStarMapWidth;
	}

	public int getaStarMapHeight() {
		return aStarMapHeight;
	}

	public void setaStarMapHeight(int aStarMapHeight) {
		this.aStarMapHeight = aStarMapHeight;
	}

	public String toString() {
		return "Room: (" + getRow() + ", " + getCol() + ") Distance:" + getDistanceFromSpawn();
	}

	public void setDistanceFromSpawn(int distance) {
		this.distanceFromSpawn = distance;
	}

	public int getDistanceFromSpawn() {
		return this.distanceFromSpawn;
	}

	public int getDifficultyRating() {
		return difficultyRating;
	}

	public void setDifficultyRating(int difficultyRating) {
		this.difficultyRating = difficultyRating;
	}


}
