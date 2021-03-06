package game.room;
import game.GameWorld;
import game.item.HeartContainerItem;
//import game.item.PegasusBootsItem;
//import game.item.Portal;
//import game.item.RedPotionItem;
//import game.item.WhiteSwordItem;

import game.item.Portal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.pathfinding.PathFindingContext;


public class ZeldaPortalRoom extends TiledImageRoom {
	
	private static Image WALL_TEST, DUNGEON_FLOOR;

	public ZeldaPortalRoom(GameWorld gameWorld) {	this(gameWorld, 0, 0);	}

	public ZeldaPortalRoom(GameWorld gameWorld, int row, int col) 
	{
		super(gameWorld, row, col);
		
		setRoomType(RoomType.ZELDA_ROOM_PORTAL);
		this.setDifficultyRating(0);
		loadMapFromFile(System.getProperty("user.dir") + "/src/rooms/test_room_1.txt");
		try 
		{
			WALL_TEST = new Image("images/bg_tile_60x60_2.png");
			DUNGEON_FLOOR = new Image("images/dungeonfloor1.png");
		} catch (SlickException e) {
			
			e.printStackTrace();
		}
		
		try {
			getItemList().add(new Portal(getGameWorld()));
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	private void loadMapFromFile(String filePath) {
		try {
			int[][] tiles = super.getTiles();
			Scanner s = new Scanner(new File(filePath));
			int currentRow = 0;
			while(s.hasNextLine()) {
				tiles[currentRow++] = convertToIntArray(s.nextLine());
				
			}
			s.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}

	private int[] convertToIntArray(String line) {
		//System.out.println(line);
		//System.out.println("\t" + java.util.Arrays.toString(line.split(",")));
		String[] strings = line.split(",");
		int[] ret = new int[strings.length];
		for(int i = 0; i < strings.length; ++i)
			ret[i] = Integer.parseInt(strings[i]);
		return ret;
	}

	@Override
	public void drawBackground(GameContainer container, Graphics g) {
		int[][] tiles = super.getTiles();
		for(int i = 0; i < tiles.length; ++i) {
			for(int j = 0; j < tiles[i].length; ++j) {
				g.drawImage(getTileFromValue(tiles[i][j]), j * 60, i * 60, getRoomColor());
			}
		}
	}	

	private Image getTileFromValue(int value) {
		
		switch(value) {
		case (0) : return WALL_TEST;
		case (1) : return DUNGEON_FLOOR;
		default: return null;
		}
	}

	public int getWidthInTiles() {
		
		return getaStarMapWidth();
	}

	public int getHeightInTiles() {
		
		return getaStarMapHeight();
	}

	public void pathFinderVisited(int x, int y) {
		
		
	}

	public boolean blocked(PathFindingContext context, int tx, int ty) {
		return getaStarMap()[ty][tx] != 0;
	}

	public float getCost(PathFindingContext context, int tx, int ty) {
		return 1.0f;
	}
	
	

}
